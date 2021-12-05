package com.f197a4.registry.repository;

import java.util.Optional;

import com.f197a4.registry.domain.security.ERole;
import com.f197a4.registry.domain.security.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role,Long> {
    Optional<Role> findByName(ERole name);
}
