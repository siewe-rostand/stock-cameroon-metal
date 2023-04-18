package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.Role;
import com.siewe.inventorymanagementsystem.model.User;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.HashSet;

@Data
public class UserDto {
    private Long id;
    @NotBlank(message = "lastname required" )
    private String lastname;
    @NotBlank(message = "firstname required" )
    private String firstname;
    private String username;
    private String fullname;
    @Email(message = "Email format please")
    private String email;
    private String city;
    private String quarter;
    private String telephone;
    private String telephone_alt;
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
        this.telephone = phone;
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

            userDto.setLastname(user.getLastname());
            userDto.setFirstname(user.getFirstname());
            userDto.setUsername(user.getFirstname() +"."+user.getLastname());
            userDto.setFullname(user.getFullName());
            userDto.setEmail(user.getEmail());
            userDto.setCity(user.getCity());
            userDto.setQuarter(user.getQuarter());

            userDto.setTelephone(user.getTelephone());
            userDto.setTelephone_alt(user.getTelephoneAlt());
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

            /**
             * make sure the role table in the database contain some roles
             */
            HashSet<String> roles = new HashSet<>();
            if (user.getRoles().size()>0) {
                for (Role role : user.getRoles())
                    roles.add(role.getName());
            }
            userDto.setRoles(roles);

            return userDto;
        }
        return null;
    }
}
