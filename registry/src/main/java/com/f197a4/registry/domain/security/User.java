package com.f197a4.registry.domain.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.f197a4.registry.domain.RegistryItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@RequiredArgsConstructor @NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NonNull
    private String username;

    @NonNull
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles",
        joinColumns= @JoinColumn(name="user_id"),
        inverseJoinColumns= @JoinColumn(name="role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @NonNull
    @OneToMany(mappedBy = "recipient")
    List<RegistryItem> registry = new ArrayList<>();

    @NonNull
    @OneToMany(mappedBy = "buyer")
    List<RegistryItem> bought = new ArrayList<>();
}
