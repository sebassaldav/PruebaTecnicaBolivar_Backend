package com.jssv.globalinvoice.dto;

import com.jssv.globalinvoice.enums.InvoiceTypeEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class InvoiceRequestDTO {

    private String customsCode;

    @NotNull(message = "El tipo de factura es obligatorio")
    private InvoiceTypeEnum type;

    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.1", inclusive = false, message = "El subtotal debe ser mayor que cero")
    private BigDecimal subtotal;
}