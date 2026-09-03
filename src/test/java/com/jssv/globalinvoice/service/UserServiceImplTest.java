package com.jssv.globalinvoice.service;

import com.jssv.globalinvoice.dto.RoleDTO;
import com.jssv.globalinvoice.dto.UserDTO;
import com.jssv.globalinvoice.entity.User;
import com.jssv.globalinvoice.exception.NoDataFoundException;
import com.jssv.globalinvoice.exception.ValidateException;
import com.jssv.globalinvoice.mapper.UserMapper;
import com.jssv.globalinvoice.repository.UserRepository;
import com.jssv.globalinvoice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findAll_shouldReturnMappedPage() {
        User user = User.builder().id(1).email("a@b.com").active(true).build();
        UserDTO dto = UserDTO.builder().id(1).email("a@b.com").active(true).roles(List.of()).build();

        when(repository.findAll(any(org.springframework.data.domain.Pageable.class))).thenReturn(new PageImpl<>(List.of(user)));
        when(mapper.toDTO(user)).thenReturn(dto);

        Page<UserDTO> result = userService.findAll(PageRequest.of(0, 10), null);

        assertEquals(1, result.getTotalElements());
        assertEquals("a@b.com", result.getContent().getFirst().getEmail());
    }

    @Test
    void create_shouldEncodePasswordAndPersistUser() {
        UserDTO dto = UserDTO.builder().email("new@user.com").password("123456").active(true).roles(List.of(new RoleDTO(1, "AUDITOR"))).build();
        User entity = User.builder().email("new@user.com").password("encoded").active(true).build();
        User saved = User.builder().id(7).email("new@user.com").password("encoded").active(true).build();
        UserDTO savedDto = UserDTO.builder().id(7).email("new@user.com").active(true).roles(List.of(new RoleDTO(1, "AUDITOR"))).build();

        when(repository.findByEmail("new@user.com")).thenReturn(Optional.empty());
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(passwordEncoder.encode("123456")).thenReturn("encoded");
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toDTO(saved)).thenReturn(savedDto);

        UserDTO result = userService.create(dto);

        assertEquals(7, result.getId());
        assertEquals("new@user.com", result.getEmail());
        verify(passwordEncoder).encode("123456");
        verify(repository).save(entity);
    }

    @Test
    void findById_shouldReturnUserWhenExists() {
        User user = User.builder().id(3).email("u@x.com").active(true).build();
        UserDTO dto = UserDTO.builder().id(3).email("u@x.com").active(true).roles(List.of()).build();

        when(repository.findById(3)).thenReturn(Optional.of(user));
        when(mapper.toDTO(user)).thenReturn(dto);

        UserDTO result = userService.findById(3);

        assertEquals("u@x.com", result.getEmail());
    }

    @Test
    void findAll_shouldSupportSearchTerm() {
        User user = User.builder().id(5).email("search@user.com").active(true).build();
        UserDTO dto = UserDTO.builder().id(5).email("search@user.com").active(true).roles(List.of()).build();

        when(repository.findByEmailContainingIgnoreCase(any(PageRequest.class), eq("search")))
                .thenReturn(new PageImpl<>(List.of(user)));
        when(mapper.toDTO(user)).thenReturn(dto);

        Page<UserDTO> result = userService.findAll(PageRequest.of(0, 10), "search");

        assertEquals(1, result.getTotalElements());
        assertEquals("search@user.com", result.getContent().get(0).getEmail());
    }

    @Test
    void create_shouldThrowWhenEmailAlreadyExists() {
        UserDTO dto = UserDTO.builder().email("dup@user.com").password("123456").active(true).roles(List.of()).build();

        when(repository.findByEmail("dup@user.com")).thenReturn(Optional.of(User.builder().id(9).email("dup@user.com").build()));

        ValidateException ex = assertThrows(ValidateException.class, () -> userService.create(dto));
        assertTrue(ex.getMessage().contains("email"));
    }

    @Test
    void update_shouldUpdateCurrentUserAndEncodePassword() {
        UserDTO dto = UserDTO.builder().email("updated@user.com").password("newPass").active(false).roles(List.of()).build();
        User current = User.builder().id(5).email("old@user.com").active(true).build();
        User mapped = User.builder().email("updated@user.com").active(false).build();
        User saved = User.builder().id(5).email("updated@user.com").active(false).build();

        when(repository.findById(5)).thenReturn(Optional.of(current));
        when(mapper.toEntity(dto)).thenReturn(mapped);
        when(passwordEncoder.encode("newPass")).thenReturn("encoded-new");
        when(repository.save(any(User.class))).thenReturn(saved);
        when(mapper.toDTO(saved)).thenReturn(UserDTO.builder().id(5).email("updated@user.com").active(false).roles(List.of()).build());

        UserDTO result = userService.update(5, dto);

        assertEquals("updated@user.com", result.getEmail());
        verify(repository).save(any(User.class));
    }

    @Test
    void delete_shouldDeleteExistingUser() {
        User user = User.builder().id(13).email("del@test.com").active(true).build();

        when(repository.findById(13)).thenReturn(Optional.of(user));

        userService.delete(13);

        verify(repository).delete(user);
    }

    @Test
    void delete_shouldThrowWhenIdDoesNotExist() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        NoDataFoundException ex = assertThrows(NoDataFoundException.class, () -> userService.delete(99));
        assertTrue(ex.getMessage().contains("ID"));
    }
}
