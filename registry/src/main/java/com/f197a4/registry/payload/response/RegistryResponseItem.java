package com.f197a4.registry.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor @RequiredArgsConstructor
public class RegistryResponseItem {

    private String buyerName;

    @NonNull
    private String productName;
}
