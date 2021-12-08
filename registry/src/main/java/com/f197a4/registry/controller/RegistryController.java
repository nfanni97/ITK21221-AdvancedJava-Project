package com.f197a4.registry.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.f197a4.registry.domain.Product;
import com.f197a4.registry.domain.RegistryItem;
import com.f197a4.registry.domain.security.User;
import com.f197a4.registry.exception.ProductException;
import com.f197a4.registry.payload.request.AddRegistryItemRequest;
import com.f197a4.registry.payload.request.BuyRequest;
import com.f197a4.registry.payload.response.BuyResponse;
import com.f197a4.registry.payload.response.RegistryResponse;
import com.f197a4.registry.payload.response.RegistryResponseItem;
import com.f197a4.registry.repository.ProductRepo;
import com.f197a4.registry.repository.RegistryItemRepo;
import com.f197a4.registry.repository.UserRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/content/registries")
public class RegistryController {

    private Logger logger = LoggerFactory.getLogger(RegistryController.class);

    @Autowired
    UserRepo userRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    RegistryItemRepo registryItemRepo;
    
    @GetMapping("/registry/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public RegistryResponse getRegistryOfUser(@PathVariable Long id) {
        //logger.info("Getting registry of user")
        checkUserExists(id);
        List<RegistryItem> registry = registryItemRepo.findRegistryItemByRecipientId(id);
        RegistryResponse resp = new RegistryResponse(userRepo.getById(id).getUsername());
        resp.setItems(registry.stream().map(item -> {
            String buyerStr;
            String productStr = item.getItem().getName();
            if(item.getBuyer() != null) {
                buyerStr = item.getBuyer().getUsername();
            } else {
                buyerStr = "N/A";
            }
            return new RegistryResponseItem(buyerStr, productStr);
        }).collect(Collectors.toList()));
        return resp;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<RegistryResponse> getAllRegistries() {
        List<User> users = userRepo.findAll();
        List<RegistryResponse> resp = new ArrayList<>();
        for(User u: users) {
            resp.add(getRegistryOfUser(u.getId()));
        }
        return resp;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public RegistryResponse addProductToRegistry(@RequestBody AddRegistryItemRequest requestBody) {
        long productId = requestBody.getProductId();
        // check if that id exists
        if(!productRepo.existsById(productId)) {
            logger.error("The product with id {} does not exist.", productId);
            throw new ProductException(productId,"not found");
        }
        
        // add to registry of current user and save
        User currentUser = getCurrentUser();
        // create new registryItem
        RegistryItem item = new RegistryItem(
            currentUser,
            productRepo.getById(productId)
        );
        List<RegistryItem> currentRegistry = currentUser.getRegistry();
        currentRegistry.add(item);
        currentUser.setRegistry(currentRegistry);
        userRepo.saveAndFlush(currentUser);

        // return udpated registry
        return getRegistryOfUser(currentUser.getId());
    }

    private User getCurrentUser() {
        UserDetails currentUserDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepo.findByUsername(currentUserDetails.getUsername()).get();
        return currentUser;
    }

    @PostMapping("/buy")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public BuyResponse buyProduct(@RequestBody BuyRequest buyRequest) {
        // check that recipient exists
        long recipientId = buyRequest.getRecipientId();
        checkUserExists(recipientId);
        // check that registry of recipient contains product in question and is not spoken for
        long productId = buyRequest.getProductId();
        List<RegistryItem> recipientRegistry = registryItemRepo.findRegistryItemByRecipientId(recipientId);
        List<RegistryItem> recipientRegistryCurrentProduct = recipientRegistry.stream().filter(item -> item.getId().equals(productId)).collect(Collectors.toList());
        if(recipientRegistryCurrentProduct.isEmpty()) {
            throw new ProductException(productId, "not found");
        }
        if(recipientRegistryCurrentProduct.get(0).getBuyer() != null) {
            throw new ProductException(productId, "already bought for "+recipientId);
        }
        // create registryItem
        RegistryItem item = recipientRegistryCurrentProduct.get(0);
        User currentUser = getCurrentUser();
        item.setBuyer(currentUser);
        // add to current user's bought list and save
        List<RegistryItem> currentBought = currentUser.getBought();
        currentBought.add(item);
        userRepo.saveAndFlush(currentUser);
        // create and return buyResponse
        return new BuyResponse(
            userRepo.getById(recipientId).getUsername(),
            productRepo.getById(productId)
        );
    }

    private void checkUserExists(Long id) {
        if(!userRepo.existsById(id)) {
            throw new UsernameNotFoundException("User with id "+id+" could not be found");
        }
    }

    @DeleteMapping("/unbuy")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public BuyResponse unbuyProduct(@RequestBody BuyRequest buyRequest) {
        // check that recipient exists
        long recipientId = buyRequest.getRecipientId();
        checkUserExists(recipientId);
        // check that registry of recipient contains product in question and is spoken for by current user
        long productId = buyRequest.getProductId();
        List<RegistryItem> recipientRegistry = registryItemRepo.findRegistryItemByRecipientId(recipientId);
        List<RegistryItem> recipientRegistryCurrentProduct = recipientRegistry.stream().filter(item -> item.getId().equals(productId)).collect(Collectors.toList());
        if(recipientRegistryCurrentProduct.isEmpty()) {
            throw new ProductException(productId,"not found");
        }
        User buyer = recipientRegistryCurrentProduct.get(0).getBuyer();
        User currentUser = getCurrentUser();
        if(buyer == null) {
            throw new ProductException(productId, "not yet bought for "+recipientId);
        }
        if(!currentUser.equals(buyer)) {
            throw new ProductException(productId,"the buyer is not "+currentUser.getId());
        }
        // create registryItem
        RegistryItem item = recipientRegistryCurrentProduct.get(0);
        item.setBuyer(null);
        // remove from bought list and save
        List<RegistryItem> currentBought = currentUser.getBought();
        currentBought.removeIf(i -> i.getId().equals(productId));
        currentUser.setBought(currentBought);
        userRepo.saveAndFlush(currentUser);
        return new BuyResponse(
            userRepo.getById(recipientId).getUsername(),
            productRepo.getById(productId)
        );
    }

    @GetMapping("/i-bought")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<RegistryResponse> boughtByCurrentUser() {
        User currentUser = getCurrentUser();
        Map<User,List<Product>> boughtMap = new HashMap<>();
        for(RegistryItem item: currentUser.getBought()) {
            if(!boughtMap.containsKey(item.getRecipient())) {
                boughtMap.put(item.getRecipient(), new ArrayList<Product>());
            }
            boughtMap.get(item.getRecipient()).add(item.getItem());
        }
        // create response object
        List<RegistryResponse> resp = boughtMap.entrySet().stream().map(entry -> {
            List<RegistryResponseItem> items = entry.getValue().stream().map(item -> {
                return new RegistryResponseItem(currentUser.getUsername(), item.getName());
            }).collect(Collectors.toList());
            return new RegistryResponse(entry.getKey().getUsername(),items);
        }).collect(Collectors.toList());
        return resp;
    }
}
