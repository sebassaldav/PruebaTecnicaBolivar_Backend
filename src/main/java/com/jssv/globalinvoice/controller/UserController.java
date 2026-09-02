package com.jssv.globalinvoice.controller;

import com.jssv.globalinvoice.dto.UserDTO;
import com.jssv.globalinvoice.dto.WrapperResponse;
import com.jssv.globalinvoice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<WrapperResponse<Page<UserDTO>>> findAll(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "limit", required = false, defaultValue = "5") int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<UserDTO> page = service.findAll(pageable, search);
        return new WrapperResponse<>(page, true, "success").createResponse(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<WrapperResponse<UserDTO>> findById(@PathVariable Integer id) {
        UserDTO dto = service.findById(id);
        return new WrapperResponse<>(dto, true, "success").createResponse(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<WrapperResponse<UserDTO>> create(@Valid @RequestBody UserDTO obj) {
        UserDTO created = service.create(obj);
        return new WrapperResponse<>(created, true, "success").createResponse(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<WrapperResponse<UserDTO>> update(@PathVariable Integer id, @Valid @RequestBody UserDTO obj) {
        UserDTO edited = service.update(id, obj);
        return new WrapperResponse<>(edited, true, "success").createResponse(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<WrapperResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return new WrapperResponse<Void>(null, true, "success").createResponse(HttpStatus.OK);
    }
}