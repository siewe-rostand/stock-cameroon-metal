package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.Role;
import com.siewe.inventorymanagementsystem.model.User;
import lombok.Data;

import java.util.Collection;
import java.util.HashSet;

@Data
public class UserDto {
    private Long id;
    private String login;
    private String lastname;
    private String firstname;
    private String name;
    private String address;
    private String phone;
    private String phone2;
    private String password;
    private Boolean activated;
    private String langKey;
    private String activationKey;
    private String resetKey;
    private String createdDate;
    private String resetDate;
    //private String role;
    private Collection<String> roles;
    private String badge;

    private Boolean validated;
    private String playerId;
    private Boolean deletable;
    private Boolean deleted;

    private int nbStores;

    public UserDto(String phone, String password) {
        this.phone = phone;
        this.password = password;
        this.activated = true;
    }

    public UserDto() {
    }

    public UserDto createDTO(User user) {
        if(user != null){
            UserDto userDto = new UserDto();

            userDto.setId(user.getUserId());
            userDto.setLangKey(user.getLangKey());
            userDto.setActivated(user.getActivated());
            userDto.setCreatedDate(user.getCreatedDate());
            userDto.setActivationKey(user.getActivationKey());
            userDto.setResetDate(user.getUpdatedDate());
            userDto.setResetKey(user.getResetKey());
            userDto.setPlayerId(user.getPlayerId());

            userDto.setLogin(user.getUsername());
            userDto.setLastname(user.getLastname());
            userDto.setFirstname(user.getFirstname());
            userDto.setName(user.getName());
            userDto.setAddress(user.getAddress());

            userDto.setPhone(user.getTelephone());
            userDto.setPhone2(user.getTelephoneAlt());
            userDto.setValidated(user.getValidated());
            userDto.setDeleted(user.getDeleted());

            if(user.getVentes() != null){
                if(user.getVentes().isEmpty())
                    userDto.setDeletable(true);
            }

            /*if(user.getRole() != null){
                userDto.setRole(user.getRole().getName());
                if(userDto.getRole().equals("ADMIN"))
                    userDto.setBadge("success");
                if(userDto.getRole().equals("USER"))
                    userDto.setBadge("warning");
                if(userDto.getRole().equals("USER"))
                    userDto.setBadge("primary");
            }*/

            HashSet<String> roles = new HashSet<>();
            if (user.getRoles() != null) {
                for (Role role : user.getRoles())
                    roles.add(role.getName());
            }
            userDto.setRoles(roles);

            return userDto;
        }
        return null;
    }
}
