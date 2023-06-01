package com.siewe.inventorymanagementsystem.security.jwt;

import com.siewe.inventorymanagementsystem.model.Role;
import com.siewe.inventorymanagementsystem.model.User;
import com.siewe.inventorymanagementsystem.repository.UserRepository;
import com.siewe.inventorymanagementsystem.security.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TokenProvider {

    private final String secretKey;

    private final long tokenValidityInMilliseconds;

    private final UserDetailsService userService;

    @Autowired
    private UserRepository userRepository;

    public TokenProvider(UserDetailsService userService) {
        this.secretKey = Base64.getEncoder().encodeToString(SecurityConstants.SECRET.getBytes());
        this.tokenValidityInMilliseconds = SecurityConstants.EXPIRATION_TIME;
        //this.tokenValidityInMilliseconds = 60000;  //one minute
        this.userService = userService;
    }

    public String createToken(String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + this.tokenValidityInMilliseconds);

        User user = userRepository.findByEmail(email);
        List<String> roles = new ArrayList<>();
        if(user != null){
            for(Role role: user.getRoles()){
                roles.add(role.getName());
            }
        }


        return Jwts.builder().setId(UUID.randomUUID().toString()).setSubject(email)
                .setIssuedAt(now).signWith(SignatureAlgorithm.HS512, this.secretKey)
                .setExpiration(validity).claim("id", user.getUserId())
                .claim("login", user.getEmail())
                .claim("lastName", user.getLastname())
                .claim("firstName", user.getFirstname())
                .claim("telephone", user.getTelephone())
                .claim("roles", roles).compact();
    }

    public Authentication getAuthentication(String token) {
        String username = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token)
                .getBody().getSubject();
        UserDetails userDetails = this.userService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
    }
}
