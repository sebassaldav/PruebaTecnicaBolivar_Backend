package com.jssv.globalinvoice.mapper;

import com.jssv.globalinvoice.dto.RoleDTO;
import com.jssv.globalinvoice.dto.UserDTO;
import com.jssv.globalinvoice.entity.Role;
import com.jssv.globalinvoice.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper extends GenericMapper<User, UserDTO> {

    @Override
    public UserDTO toDTO(User entity) {
        if (entity == null) {
            return null;
        }
        List<RoleDTO> rolesDto = entity.getRoles().stream()
                .map(rol -> new RoleDTO(rol.getId(), rol.getNombre()))
                .collect(Collectors.toList());

        return UserDTO.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .active(entity.isActive())
                .roles(rolesDto)
                .build();
    }

    @Override
    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        Set<Role> roles = dto.getRoles() != null ?
                dto.getRoles().stream()
                        .map(rolDTO -> Role.builder()
                                .id(rolDTO.getId())
                                .nombre(rolDTO.getNombre())
                                .build()
                        )
                        .collect(Collectors.toSet())
                        : new HashSet<>();

        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .active(dto.isActivo())
                .roles(roles)
                .build();
    }
}
