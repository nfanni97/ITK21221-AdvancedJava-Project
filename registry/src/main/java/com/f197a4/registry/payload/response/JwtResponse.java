package com.f197a4.registry.payload.response;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter @Setter
public class JwtResponse {
    @NonNull
    private String token;

    private String type = "Bearer";

    @NonNull
    private Long id;

    @NonNull
    private String username;

    @NonNull
    private List<String> roles;
}
