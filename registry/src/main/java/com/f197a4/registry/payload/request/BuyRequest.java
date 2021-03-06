package com.f197a4.registry.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class BuyRequest {
    @NonNull
    private Long productId;

    @NonNull
    private Long recipientId;
}
