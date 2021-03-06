package com.f197a4.registry.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.f197a4.registry.service.ExchangeService;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Table(name = "products")
@Entity
@Getter @Setter
@RequiredArgsConstructor @NoArgsConstructor @AllArgsConstructor
public class Product {
    public Product(@NonNull String name2, @NonNull Integer priceHuf2, @NonNull Set<Category> categories2) {
        name = name2;
        priceHuf = priceHuf2;
        categories = categories2;
    }

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private Integer priceHuf;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name="product_category",
        joinColumns = @JoinColumn(name="product_id"),
        inverseJoinColumns = @JoinColumn(name="category_id")
    )
    @NonNull
    private Set<Category> categories = new HashSet<>();

    @JsonInclude
    @Transient
    private Double priceEur;

    @PostLoad
    public void setPriceEur() {
        priceEur = ExchangeService.exchange(priceHuf, "eur");
    }

    @Override
    public String toString() {
        return id+". Name: "+name+", price: "+priceHuf+" HUF ("+priceEur+"EUR), categories: "+categories;
    }
}
