package com.jssv.globalinvoice.service.tax;

import com.jssv.globalinvoice.enums.InvoiceTypeEnum;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class NationalTaxStrategy implements InvoiceTaxStrategy {

    private static final BigDecimal IVA_RATE =
            new BigDecimal("0.19");

    @Override
    public InvoiceTypeEnum getType() {
        return InvoiceTypeEnum.NACIONAL;
    }

    @Override
    public TaxCalculationResult calculate(BigDecimal subtotal) {

        BigDecimal iva = subtotal
                .multiply(IVA_RATE)
                .setScale(2, RoundingMode.HALF_UP);

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