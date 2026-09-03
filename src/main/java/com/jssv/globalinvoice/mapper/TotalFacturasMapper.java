package com.jssv.globalinvoice.mapper;

import com.jssv.globalinvoice.dto.InvoiceDTO;
import com.jssv.globalinvoice.dto.TotalFacturasDTO;
import com.jssv.globalinvoice.entity.Invoice;
import org.springframework.stereotype.Component;

@Component
public class TotalFacturasMapper  extends GenericMapper<Invoice, TotalFacturasDTO> {
    @Override
    public TotalFacturasDTO toDTO(Invoice entity) {
        if (entity == null) {
            return null;
        }
        return TotalFacturasDTO.builder()
                .type(entity.getType())
                .total(entity.getTotal())
                .build();
    }

    @Override
    public Invoice toEntity(TotalFacturasDTO dto) {
        if (dto == null) {
            return null;
        }
        return Invoice.builder()
                .type(dto.getType())
                .total(dto.getTotal())
                .build();
    }
}
