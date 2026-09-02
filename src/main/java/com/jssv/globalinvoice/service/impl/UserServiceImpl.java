package com.jssv.globalinvoice.service.impl;


import com.jssv.globalinvoice.dto.UserDTO;
import com.jssv.globalinvoice.entity.User;
import com.jssv.globalinvoice.exception.NoDataFoundException;
import com.jssv.globalinvoice.exception.ValidateException;
import com.jssv.globalinvoice.mapper.UserMapper;
import com.jssv.globalinvoice.repository.UserRepository;
import com.jssv.globalinvoice.service.UserService;
import com.jssv.globalinvoice.validator.UserValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable, String search) {
        Page<User> usuarios;
        if (search == null || search.trim().isEmpty()) {
            usuarios = repository.findAll(pageable);
        } else{
            usuarios = repository.findByEmailContainingIgnoreCase(pageable, search);
        }
        return new PageImpl<>(
                usuarios.getContent().stream()
                        .map(mapper::toDTO)
                        .collect(Collectors.toList()),
                pageable,
                usuarios.getTotalElements()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO findById(Integer id) {
        User entidad = repository.findById(id).orElseThrow(
                () -> new NoDataFoundException("No existe un registro con ese ID."));
        return mapper.toDTO(entidad);
    }

    @Override
    public UserDTO create(UserDTO obj) {
        UserValidator.save(obj);
        if (repository.findByEmail(obj.getEmail()).isPresent()) {
            throw new ValidateException("El email ya está registrado a otro usuario");
        }
        User usuario = mapper.toEntity(obj);
        usuario.setPassword(passwordEncoder.encode(obj.getPassword()));
        User saved = repository.save(usuario);
        return mapper.toDTO(saved);
    }

    @Override
    public UserDTO update(Integer id, UserDTO obj) {
        UserValidator.save(obj);

        User usuarioActual = repository.findById(id)
                .orElseThrow(() -> new NoDataFoundException("No existe un registro con ese ID"));

        User entidad = mapper.toEntity(obj);

        //Actualizar campos directamente de la entidad existente
        usuarioActual.setEmail(entidad.getEmail());
        usuarioActual.setActive(entidad.isActive());
        usuarioActual.setRoles(entidad.getRoles());

        if (obj.getPassword() != null && !obj.getPassword().isEmpty()) {
            usuarioActual.setPassword(passwordEncoder.encode(obj.getPassword()));
        }

        User saved = repository.save(entidad);
        return mapper.toDTO(saved);

    }

    @Override
    public void delete(Integer id) {
        User entidad = repository.findById(id).orElseThrow(
                () -> new NoDataFoundException("No existe un registro con ese ID.")
        );
        repository.delete(entidad);
    }
}
