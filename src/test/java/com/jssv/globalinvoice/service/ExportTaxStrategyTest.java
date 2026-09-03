package com.jssv.globalinvoice.service;

import com.jssv.globalinvoice.service.tax.ExportTaxStrategy;
import com.jssv.globalinvoice.service.tax.TaxCalculationResult;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExportTaxStrategyTest {

    @Test
    void shouldReturnOriginalAmountForExportTax() {
        ExportTaxStrategy strategy = new ExportTaxStrategy();

        TaxCalculationResult result = strategy.calculate(new BigDecimal("2500.50"));

        assertEquals(new BigDecimal("2500.50"), result.getTotal());
    }
}
