package com.jssv.globalinvoice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Integer id;
    private String email;
    private String password;
    private Boolean active;
    private List<RoleDTO> roles;

    public boolean isActivo(){
        return active;
    }
}
