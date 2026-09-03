package com.jssv.globalinvoice.service;

import com.jssv.globalinvoice.service.tax.GovernmentTaxStrategy;
import com.jssv.globalinvoice.service.tax.TaxCalculationResult;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GovernmentTaxStrategyTest {

    @Test
    void shouldApplyVatAndRetainTax() {
        GovernmentTaxStrategy strategy = new GovernmentTaxStrategy();

        TaxCalculationResult result = strategy.calculate(new BigDecimal("1000"));

        assertEquals(new BigDecimal("1130.50"), result.getTotal());
    }
}
