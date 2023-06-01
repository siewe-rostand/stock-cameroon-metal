package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole,Long> {
}
