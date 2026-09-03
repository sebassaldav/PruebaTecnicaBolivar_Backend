package com.jssv.globalinvoice.dto;

import com.jssv.globalinvoice.enums.InvoiceTypeEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class InvoiceRequestDTO {

    @Pattern(
            regexp = "^[A-Za-z0-9]+$",
            message = "El código aduanero solo puede contener letras y números"
    )
    @Size(
            max = 20,
            message = "El código aduanero no puede superar los 20 caracteres"
    )
    private String customsCode;

    @NotNull(message = "El tipo de factura es obligatorio")
    private InvoiceTypeEnum type;

    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.1", inclusive = false, message = "El subtotal debe ser mayor que cero")
    private BigDecimal subtotal;
}