package com.f197a4.registry.repository;

import java.util.Optional;
import java.util.List;

import com.f197a4.registry.domain.RegistryItem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistryItemRepo extends JpaRepository<RegistryItem,Long> {
    List<RegistryItem> findRegistryItemByRecipientId(Long id);//to find registry of a user

    List<RegistryItem> findRegistryItemByBuyerId(Long id);// to find what a user bought
}
