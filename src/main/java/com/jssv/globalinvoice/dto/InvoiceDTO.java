package com.jssv.globalinvoice.dto;

import com.jssv.globalinvoice.enums.InvoiceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class InvoiceDTO {

    private Integer id;
    private String consecutive;
    private InvoiceTypeEnum type;
    private BigDecimal subtotal;
    private BigDecimal iva;
    private BigDecimal withholding;
    private BigDecimal total;
    private LocalDate created_at;
    private String created_by;
}
