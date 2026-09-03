package com.jssv.globalinvoice.service.tax;

import com.jssv.globalinvoice.enums.InvoiceTypeEnum;
import com.jssv.globalinvoice.exception.GeneralException;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class TaxStrategyFactory {

    private final Map<InvoiceTypeEnum, InvoiceTaxStrategy> strategies;

    public TaxStrategyFactory(
            List<InvoiceTaxStrategy> strategyList) {

        this.strategies = new EnumMap<>(InvoiceTypeEnum.class);

        strategyList.forEach(strategy ->
                strategies.put(strategy.getType(), strategy)
        );
    }

    public InvoiceTaxStrategy getStrategy(InvoiceTypeEnum type) {

        InvoiceTaxStrategy strategy = strategies.get(type);

        if (strategy == null) {
            throw new GeneralException(
                    "No se tiene cálculo de momento para el tipo de factura: "
                            + type
            );
        }

        return strategy;
    }
}