package com.jssv.globalinvoice.controller;

import com.jssv.globalinvoice.dto.InvoiceDTO;
import com.jssv.globalinvoice.dto.InvoiceRequestDTO;
import com.jssv.globalinvoice.dto.UserDTO;
import com.jssv.globalinvoice.dto.WrapperResponse;
import com.jssv.globalinvoice.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<WrapperResponse<Page<InvoiceDTO>>> findAll(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "5") int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<InvoiceDTO> page = invoiceService.findAll(pageable, search);
        return new WrapperResponse<>(page, true, "success").createResponse(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WrapperResponse<InvoiceDTO>> findById(@PathVariable Integer id) {
        InvoiceDTO dto = invoiceService.findById(id);
        return new WrapperResponse<>(dto, true, "success").createResponse(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('OPERADOR')")
    @PostMapping
    public ResponseEntity<WrapperResponse<InvoiceDTO>> create(
            @Valid @RequestBody InvoiceRequestDTO obj) {

        InvoiceDTO created = invoiceService.create(obj);

        return new WrapperResponse<>(
                created,
                true,
                "success"
        ).createResponse(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('OPERADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<WrapperResponse<InvoiceDTO>> update(
            @PathVariable Integer id,
            @Valid @RequestBody InvoiceDTO obj) {

        InvoiceDTO updated =
                invoiceService.update(id, obj);

        return new WrapperResponse<>(
                updated,
                true,
                "success"
        ).createResponse(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('OPERADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<WrapperResponse<Void>> delete(
            @PathVariable Integer id) {

        invoiceService.delete(id);

        return new WrapperResponse<Void>(
                null,
                true,
                "Factura eliminada correctamente"
        ).createResponse(HttpStatus.NO_CONTENT);
    }
}
