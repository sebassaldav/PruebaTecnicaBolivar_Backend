package com.jssv.globalinvoice.mapper;

import com.jssv.globalinvoice.dto.InvoiceDTO;
import com.jssv.globalinvoice.entity.Invoice;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper extends GenericMapper<Invoice, InvoiceDTO> {
    @Override
    public InvoiceDTO toDTO(Invoice entity) {
        if (entity == null) {
            return null;
        }
        return InvoiceDTO.builder()
                .id(entity.getId())
                .consecutive(entity.getConsecutive() == null || entity.getConsecutive().isBlank() ? "" : entity.getConsecutive())
                .customsCode(entity.getCustomsCode() == null || entity.getCustomsCode().isBlank() ? "" : entity.getCustomsCode())
                .type(entity.getType())
                .subtotal(entity.getSubtotal())
                .iva(entity.getIva())
                .withholding(entity.getWithholding())
                .total(entity.getTotal())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .build();
    }

    @Override
    public Invoice toEntity(InvoiceDTO dto) {
        if (dto == null) {
            return null;
        }

        return Invoice.builder()
                .id(dto.getId())
                .consecutive(dto.getConsecutive() == null || dto.getConsecutive().isBlank() ? "" : dto.getConsecutive())
                .customsCode(dto.getCustomsCode() == null || dto.getCustomsCode().isBlank() ? "" : dto.getCustomsCode())
                .type(dto.getType())
                .subtotal(dto.getSubtotal())
                .iva(dto.getIva())
                .withholding(dto.getWithholding())
                .total(dto.getTotal())
                .createdAt(dto.getCreatedAt())
                .createdBy(dto.getCreatedBy())
                .build();
    }
}
