package com.jssv.globalinvoice.validator;

import com.jssv.globalinvoice.dto.UserDTO;
import jakarta.validation.ValidationException;

public class UserValidator {

    public static void save(UserDTO registro){
        if (registro.getEmail() == null || registro.getEmail().trim().isEmpty()) {
            throw new ValidationException("El email del usuario es requerido");
        }

        if (registro.getPassword() == null || registro.getPassword().trim().isEmpty()) {
            throw new ValidationException("El password es requerido");
        }

        if (registro.getRoles() == null) {
            throw new ValidationException("Es requerido por lo menos un ROL");
        }
    }
}
