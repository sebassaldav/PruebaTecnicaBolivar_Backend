package com.jssv.globalinvoice.service;

import com.jssv.globalinvoice.enums.InvoiceTypeEnum;
import com.jssv.globalinvoice.exception.GeneralException;
import com.jssv.globalinvoice.service.tax.ExportTaxStrategy;
import com.jssv.globalinvoice.service.tax.GovernmentTaxStrategy;
import com.jssv.globalinvoice.service.tax.InvoiceTaxStrategy;
import com.jssv.globalinvoice.service.tax.NationalTaxStrategy;
import com.jssv.globalinvoice.service.tax.TaxStrategyFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaxStrategyFactoryTest {

    @Test
    void shouldReturnCorrectStrategyForEachTaxType() {
        TaxStrategyFactory factory = new TaxStrategyFactory(List.of(
                new NationalTaxStrategy(),
                new GovernmentTaxStrategy(),
                new ExportTaxStrategy()
        ));

        InvoiceTaxStrategy national = factory.getStrategy(InvoiceTypeEnum.NACIONAL);
        InvoiceTaxStrategy government = factory.getStrategy(InvoiceTypeEnum.GUBERNAMENTAL);
        InvoiceTaxStrategy export = factory.getStrategy(InvoiceTypeEnum.EXPORTACION);

        assertInstanceOf(NationalTaxStrategy.class, national);
        assertInstanceOf(GovernmentTaxStrategy.class, government);
        assertInstanceOf(ExportTaxStrategy.class, export);
    }

    @Test
    void shouldThrowForUnsupportedTaxType() {
        TaxStrategyFactory factory = new TaxStrategyFactory(List.of(
                new NationalTaxStrategy(),
                new GovernmentTaxStrategy(),
                new ExportTaxStrategy()
        ));

        GeneralException ex = assertThrows(GeneralException.class,
                () -> factory.getStrategy(null));

        assertTrue(ex.getMessage().contains("tipo de factura"));
    }
}
