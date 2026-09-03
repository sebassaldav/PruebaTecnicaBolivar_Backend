package com.jssv.globalinvoice.dto;

import com.jssv.globalinvoice.enums.InvoiceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@Data
public class TotalFacturasDTO {

    private InvoiceTypeEnum type;
    private BigDecimal total;
}
