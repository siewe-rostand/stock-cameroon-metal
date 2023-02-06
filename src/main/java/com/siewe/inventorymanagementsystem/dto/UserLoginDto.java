package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.UserLogin;
import lombok.Data;

@Data
public class UserLoginDto {
    private Long id;
    private Long userId;
    private String login;
    private String user;
    private String dateTime;

    public UserLoginDto createDTO(UserLogin userLogin) {
        UserLoginDto userLoginDto = new UserLoginDto();
        if(userLogin != null){
            userLoginDto.setId(userLogin.getId());
            userLoginDto.setDateTime(userLogin.getDateTime());

            if(userLogin.getUser() != null){
                userLoginDto.setUserId(userLogin.getUser().getUserId());
                userLoginDto.setLogin(userLogin.getUser().getUsername());
                userLoginDto.setUser(userLogin.getUser().getName());
            }
        }
        return userLoginDto;
    }
}
