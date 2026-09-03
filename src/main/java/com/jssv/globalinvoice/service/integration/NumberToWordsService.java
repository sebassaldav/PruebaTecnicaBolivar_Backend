package com.jssv.globalinvoice.service.integration;

import java.math.BigDecimal;

public interface NumberToWordsService {

    String convert(BigDecimal value);
}
