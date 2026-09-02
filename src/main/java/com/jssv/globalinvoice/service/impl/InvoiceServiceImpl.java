package com.jssv.globalinvoice.service.impl;

import com.jssv.globalinvoice.dto.InvoiceDTO;
import com.jssv.globalinvoice.dto.TotalFacturasDTO;
import com.jssv.globalinvoice.entity.Invoice;
import com.jssv.globalinvoice.exception.NoDataFoundException;
import com.jssv.globalinvoice.mapper.InvoiceMapper;
import com.jssv.globalinvoice.mapper.TotalFacturasMapper;
import com.jssv.globalinvoice.repository.InvoiceRepository;
import com.jssv.globalinvoice.service.InvoiceService;
import com.jssv.globalinvoice.service.tax.InvoiceTaxStrategy;
import com.jssv.globalinvoice.service.tax.TaxCalculationResult;
import com.jssv.globalinvoice.service.tax.TaxStrategyFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final TaxStrategyFactory taxStrategyFactory;
    private final InvoiceMapper invoiceMapper;
    private final TotalFacturasMapper totalFacturasMapper;
    private final InvoiceRepository invoiceRepository;


    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceDTO> findAll(Pageable pageable, String search) {

        Page<Invoice> invoices =
                invoiceRepository.findAll(pageable);

        return invoices.map(invoiceMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceDTO findById(Integer id) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() ->
                        new NoDataFoundException(
                                "No se encuentra la factura registrada con id: " + id
                        )
                );
        return invoiceMapper.toDTO(invoice);
    }

    @Override
    public InvoiceDTO create(InvoiceDTO obj) {

        InvoiceTaxStrategy strategy =
                taxStrategyFactory.getStrategy(obj.getType());

        TaxCalculationResult calculation =
                strategy.calculate(obj.getSubtotal());

        Invoice invoice = invoiceMapper.toEntity(obj);

        invoice.setConsecutive(getLastConsecutiveNumber());
        invoice.setIva(calculation.getIva());
        invoice.setWithholding(calculation.getWithholding());
        invoice.setTotal(calculation.getTotal());
        invoice.setCreatedAt(LocalDate.now(ZoneId.systemDefault()));
        invoice.setCreatedBy(getAuthenticatedUserEmail());

        Invoice savedInvoice =
                invoiceRepository.save(invoice);

        return invoiceMapper.toDTO(savedInvoice);
    }

    @Override
    public InvoiceDTO update(Integer integer, InvoiceDTO obj) {
        return null;
    }

    @Override
    public void delete(Integer integer) {
    }

    @Transactional
    public List<TotalFacturasDTO> findGroupedByType(){
        return invoiceRepository.findGroupedByType();
    }

    private String getAuthenticatedUserEmail() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }

    private String getLastConsecutiveNumber(){

        int lastConsecutiveNumber = invoiceRepository.getLastConsecutiveNumber();

        if (lastConsecutiveNumber < 0 || lastConsecutiveNumber == 0) {
            lastConsecutiveNumber = 000001;
        }

        return String.format("FAC-%06d", lastConsecutiveNumber + 1);
    }
}
