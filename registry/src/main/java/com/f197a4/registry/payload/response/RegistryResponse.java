package com.f197a4.registry.payload.response;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter @Getter
@RequiredArgsConstructor @AllArgsConstructor
public class RegistryResponse {    
    @NonNull
    private String recipientName;

    private List<RegistryResponseItem> items = new ArrayList<>();

    @Override
    public String toString() {
        return "Recipient: "+recipientName+", items: "+items;
    }
}
