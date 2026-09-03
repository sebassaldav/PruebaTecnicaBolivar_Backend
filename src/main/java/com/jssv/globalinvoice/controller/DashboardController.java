package com.jssv.globalinvoice.controller;

import com.jssv.globalinvoice.dto.InvoiceDTO;
import com.jssv.globalinvoice.dto.TotalFacturasDTO;
import com.jssv.globalinvoice.dto.WrapperResponse;
import com.jssv.globalinvoice.service.InvoiceService;
import com.jssv.globalinvoice.service.impl.InvoiceServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/dashboard")
public class DashboardController {

    private final InvoiceService invoiceService;

    @PreAuthorize("hasRole('AUDITOR')")
    @GetMapping
    public ResponseEntity<WrapperResponse<List<TotalFacturasDTO>>> findGruopedByType(
    ) {
        List<TotalFacturasDTO> dto = invoiceService.findGroupedByType();
        return new WrapperResponse<>(dto, true, "success").createResponse(HttpStatus.OK);
    }
}
