package com.jssv.globalinvoice.service.tax;

import com.jssv.globalinvoice.enums.InvoiceTypeEnum;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class ExportTaxStrategy implements InvoiceTaxStrategy {
    @Override
    public InvoiceTypeEnum getType() {
        return InvoiceTypeEnum.EXPORTACION;
    }

    @Override
    public TaxCalculationResult calculate(BigDecimal subtotal) {

        BigDecimal iva = BigDecimal.ZERO;

        BigDecimal withholding = BigDecimal.ZERO;

        BigDecimal total = subtotal
                .add(iva)
                .subtract(withholding)
                .setScale(2, RoundingMode.HALF_UP);

        return TaxCalculationResult.builder()
                .iva(iva)
                .withholding(withholding)
                .total(total)
                .build();
    }
}