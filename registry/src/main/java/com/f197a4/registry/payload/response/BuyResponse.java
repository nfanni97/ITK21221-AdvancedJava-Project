package com.f197a4.registry.payload.response;

import com.f197a4.registry.domain.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class BuyResponse {
    @NonNull
    private String recipientName;

    @NonNull
    private Product product;
}
