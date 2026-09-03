package com.jssv.globalinvoice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvoiceTypeEnum {

    NACIONAL("NACIONAL"),
    EXPORTACION("EXPORTACION"),
    GUBERNAMENTAL("GUBERNAMENTAL");

    private final String descripcion;
}
