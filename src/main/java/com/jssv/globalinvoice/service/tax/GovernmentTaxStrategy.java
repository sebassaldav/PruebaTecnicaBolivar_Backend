package com.jssv.globalinvoice.service.tax;

import com.jssv.globalinvoice.enums.InvoiceTypeEnum;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class GovernmentTaxStrategy implements InvoiceTaxStrategy {
    private static final BigDecimal IVA_RATE =
            new BigDecimal("0.19");

    private static final BigDecimal WITHHOLDING_RATE =
            new BigDecimal("0.05");

    @Override
    public InvoiceTypeEnum getType() {
        return InvoiceTypeEnum.GUBERNAMENTAL;
    }

    @Override
    public TaxCalculationResult calculate(BigDecimal subtotal) {

        BigDecimal iva = subtotal
                .multiply(IVA_RATE)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal withholding = subtotal
                .add(iva)
                .multiply(WITHHOLDING_RATE)
                .setScale(2, RoundingMode.HALF_UP);

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
