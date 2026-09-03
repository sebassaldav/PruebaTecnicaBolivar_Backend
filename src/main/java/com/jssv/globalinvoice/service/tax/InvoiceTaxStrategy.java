package com.jssv.globalinvoice.service.tax;

import com.jssv.globalinvoice.enums.InvoiceTypeEnum;

import java.math.BigDecimal;

public interface InvoiceTaxStrategy {

    InvoiceTypeEnum getType();

    TaxCalculationResult calculate(BigDecimal subtotal);
}