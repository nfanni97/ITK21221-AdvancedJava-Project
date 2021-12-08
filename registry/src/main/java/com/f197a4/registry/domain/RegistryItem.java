package com.f197a4.registry.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.f197a4.registry.domain.security.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="registry_items")
@Getter @Setter
@NoArgsConstructor @RequiredArgsConstructor @AllArgsConstructor
public class RegistryItem {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "recipient_id",nullable=false)
    private User recipient;

    @ManyToOne
    @JoinColumn(name = "buyer_id",nullable=true)
    private User buyer;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "product_id",nullable=false)
    private Product item;
}
