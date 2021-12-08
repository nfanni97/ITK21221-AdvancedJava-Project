package com.f197a4.registry.payload.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter @Getter
@RequiredArgsConstructor
public class RegistryResponse {    
    @NonNull
    private String recipientName;

    private List<RegistryResponseItem> items = new ArrayList<>();
}
