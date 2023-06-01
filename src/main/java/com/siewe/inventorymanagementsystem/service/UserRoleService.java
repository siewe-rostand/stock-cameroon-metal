package com.siewe.inventorymanagementsystem.service;

import com.siewe.inventorymanagementsystem.dto.RoleDto;
import com.siewe.inventorymanagementsystem.dto.UserDto;
import com.siewe.inventorymanagementsystem.dto.UserRoleDto;
import com.siewe.inventorymanagementsystem.model.UserRole;
import com.siewe.inventorymanagementsystem.repository.RoleRepository;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import com.siewe.inventorymanagementsystem.repository.UserRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRoleService {
    private final Logger log = LoggerFactory.getLogger(UserRoleService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    public UserRoleDto save( UserRoleDto userRoleDto){
        log.debug("Request to save User role : {}", userRoleDto);
        UserRole userRole = new UserRole();

        userRole.setUser(userRepository.findByEmail(userRoleDto.getUsername()));
        userRole.setRole(roleRepository.findByName(userRoleDto.getRoleName()));
        return  userRoleDto.createDto(userRole);
    }
}
