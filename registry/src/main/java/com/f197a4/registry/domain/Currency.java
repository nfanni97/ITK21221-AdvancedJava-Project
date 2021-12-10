package com.f197a4.registry.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class Currency {
    private String name;
    private Double rate;
}
