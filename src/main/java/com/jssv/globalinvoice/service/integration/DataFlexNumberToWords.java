package com.jssv.globalinvoice.service.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataFlexNumberToWords implements NumberToWordsService {

    @Override
    public String convert(BigDecimal value) {
        return "";
    }
}
