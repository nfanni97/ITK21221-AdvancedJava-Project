package com.f197a4.registry.payload.request;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ProductRequest {
    @NonNull
    private String name;

    @NonNull
    private Integer priceHuf;

    @NonNull
    private Set<String> categories;
}
