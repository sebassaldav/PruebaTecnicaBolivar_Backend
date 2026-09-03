package com.jssv.globalinvoice.dto;

import com.jssv.globalinvoice.enums.InvoiceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class InvoiceDTO {

    private Integer id;
    private String consecutive;
    private String customsCode;
    private InvoiceTypeEnum type;
    private BigDecimal subtotal;
    private BigDecimal iva;
    private BigDecimal withholding;
    private BigDecimal total;
    private String totalInWords;
    private LocalDate createdAt;
    private String createdBy;
}
