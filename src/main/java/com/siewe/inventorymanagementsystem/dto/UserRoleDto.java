package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.UserRole;
import lombok.Data;

@Data
public class UserRoleDto {

    private Long refId;
    private Long roleId;
    private Long userId;
    private String roleName;
    private String username;

    public UserRoleDto createDto(UserRole userRole){
        UserRoleDto userRoleDto = new UserRoleDto();

        if (userRole != null){
            userRoleDto.setRefId(userRole.getRefId());
            userRoleDto.setRoleId(userRole.getRole().getRoleId());
            userRoleDto.setRoleName(userRole.getRole().getName());
            userRoleDto.setUserId(userRole.getUser().getUserId());
            userRoleDto.setUsername(userRole.getUser().getEmail());
        }
        return userRoleDto;
    }
}
