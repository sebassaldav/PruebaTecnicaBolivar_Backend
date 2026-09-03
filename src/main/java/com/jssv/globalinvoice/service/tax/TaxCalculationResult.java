package com.jssv.globalinvoice.service.tax;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TaxCalculationResult {

    private BigDecimal iva;
    private BigDecimal withholding;
    private BigDecimal total;
}