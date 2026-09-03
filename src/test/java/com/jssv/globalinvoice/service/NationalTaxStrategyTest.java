package com.jssv.globalinvoice.service;

import com.jssv.globalinvoice.service.tax.NationalTaxStrategy;
import com.jssv.globalinvoice.service.tax.TaxCalculationResult;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NationalTaxStrategyTest {

    @Test
    void shouldApplyNacionalTaxAndReturnTotal() {
        NationalTaxStrategy strategy = new NationalTaxStrategy();

        TaxCalculationResult result = strategy.calculate(new BigDecimal("1000"));

        assertEquals(new BigDecimal("1190.00"), result.getTotal());
    }
}
