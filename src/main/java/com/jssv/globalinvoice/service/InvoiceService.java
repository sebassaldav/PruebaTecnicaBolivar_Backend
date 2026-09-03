package com.jssv.globalinvoice.service;

import com.jssv.globalinvoice.dto.InvoiceDTO;
import com.jssv.globalinvoice.dto.InvoiceRequestDTO;
import com.jssv.globalinvoice.dto.TotalFacturasDTO;

import java.util.List;

public interface InvoiceService extends PageableService<InvoiceDTO, Integer> {

    InvoiceDTO create(InvoiceRequestDTO obj);

    List<TotalFacturasDTO> findGroupedByType();
}
