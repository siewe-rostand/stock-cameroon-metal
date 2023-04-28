package com.siewe.inventorymanagementsystem.security;

import com.siewe.inventorymanagementsystem.model.Role;
import com.siewe.inventorymanagementsystem.model.User;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class CustomUserDetailsService  implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        if (login.trim().isEmpty()) {
            throw new UsernameNotFoundException("username is empty");
        }

        User user = userRepository.findByUsername(login);

        if (user == null) {
            throw new UsernameNotFoundException("User " + login + " not found");
        }

        Boolean deleted = false;
        if(user.getDeleted() != null)
            deleted = user.getDeleted();

        return org.springframework.security.core.userdetails.User
                .withUsername(login)
                .password(user.getPassword())
                .authorities(getGrantedAuthorities(user))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.getActivated() || deleted)
                .build();
    }

    private List<GrantedAuthority> getGrantedAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }
        return authorities;
    }
}
