package com.jssv.globalinvoice.service;

import com.jssv.globalinvoice.dto.InvoiceDTO;
import com.jssv.globalinvoice.dto.InvoiceRequestDTO;
import com.jssv.globalinvoice.entity.Invoice;
import com.jssv.globalinvoice.enums.InvoiceTypeEnum;
import com.jssv.globalinvoice.mapper.InvoiceMapper;
import com.jssv.globalinvoice.mapper.TotalFacturasMapper;
import com.jssv.globalinvoice.repository.InvoiceRepository;
import com.jssv.globalinvoice.service.impl.InvoiceServiceImpl;
import com.jssv.globalinvoice.service.integration.NumberToWordsService;
import com.jssv.globalinvoice.service.tax.InvoiceTaxStrategy;
import com.jssv.globalinvoice.service.tax.TaxCalculationResult;
import com.jssv.globalinvoice.service.tax.TaxStrategyFactory;
import com.jssv.globalinvoice.exception.NoDataFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Mock
    private TaxStrategyFactory taxStrategyFactory;

    @Mock
    private InvoiceMapper invoiceMapper;

    @Mock
    private TotalFacturasMapper totalFacturasMapper;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private NumberToWordsService numberToWordsService;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Test
    void create_shouldGenerateInvoiceAndSetTotalInWords() {
        InvoiceTaxStrategy strategy = mock(InvoiceTaxStrategy.class);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("user@test.com", "pass")
        );

        InvoiceRequestDTO request = InvoiceRequestDTO.builder()
                .customsCode("ABC123")
                .type(InvoiceTypeEnum.NACIONAL)
                .subtotal(new BigDecimal("1000"))
                .build();

        when(taxStrategyFactory.getStrategy(InvoiceTypeEnum.NACIONAL)).thenReturn(strategy);
        when(strategy.calculate(new BigDecimal("1000")))
                .thenReturn(TaxCalculationResult.builder()
                        .iva(new BigDecimal("190.00"))
                        .withholding(BigDecimal.ZERO)
                        .total(new BigDecimal("1190.00"))
                        .build());
        when(invoiceRepository.getLastConsecutiveNumber()).thenReturn(15);
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice invoice = invocation.getArgument(0);
            invoice.setId(30);
            return invoice;
        });
        when(invoiceMapper.toDTO(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice invoice = invocation.getArgument(0);
            return InvoiceDTO.builder()
                    .id(invoice.getId())
                    .consecutive(invoice.getConsecutive())
                    .customsCode(invoice.getCustomsCode())
                    .type(invoice.getType())
                    .subtotal(invoice.getSubtotal())
                    .iva(invoice.getIva())
                    .withholding(invoice.getWithholding())
                    .total(invoice.getTotal())
                    .createdAt(invoice.getCreatedAt())
                    .createdBy(invoice.getCreatedBy())
                    .build();
        });
        when(numberToWordsService.convert(new BigDecimal("1190.00"))).thenReturn("one thousand one hundred ninety");

        InvoiceDTO result = invoiceService.create(request);

        assertEquals("FAC-000016", result.getConsecutive());
        assertEquals("ABC123", result.getCustomsCode());
        assertEquals("one thousand one hundred ninety", result.getTotalInWords());
        assertEquals("user@test.com", result.getCreatedBy());
        verify(invoiceRepository).save(any(Invoice.class));
    }

    @Test
    void findById_shouldReturnInvoiceWithWords() {
        Invoice invoice = Invoice.builder()
                .id(11)
                .consecutive("FAC-000011")
                .customsCode("XYZ")
                .type(InvoiceTypeEnum.NACIONAL)
                .subtotal(new BigDecimal("1000"))
                .total(new BigDecimal("1190.00"))
                .createdAt(LocalDate.now())
                .createdBy("user@test.com")
                .build();

        InvoiceDTO dto = InvoiceDTO.builder()
                .id(11)
                .consecutive("FAC-000011")
                .customsCode("XYZ")
                .type(InvoiceTypeEnum.NACIONAL)
                .subtotal(new BigDecimal("1000"))
                .total(new BigDecimal("1190.00"))
                .createdAt(invoice.getCreatedAt())
                .createdBy("user@test.com")
                .build();

        when(invoiceRepository.findById(11)).thenReturn(Optional.of(invoice));
        when(invoiceMapper.toDTO(invoice)).thenReturn(dto);
        when(numberToWordsService.convert(new BigDecimal("1190.00"))).thenReturn("one thousand one hundred ninety");

        InvoiceDTO result = invoiceService.findById(11);

        assertEquals("FAC-000011", result.getConsecutive());
        assertEquals("one thousand one hundred ninety", result.getTotalInWords());
    }

    @Test
    void findAll_shouldReturnPageWithTotalInWords() {
        Invoice invoice = Invoice.builder()
                .id(5)
                .consecutive("FAC-000005")
                .customsCode("QWE")
                .type(InvoiceTypeEnum.EXPORTACION)
                .subtotal(new BigDecimal("2000"))
                .total(new BigDecimal("2000.00"))
                .createdAt(LocalDate.now())
                .createdBy("user@test.com")
                .build();

        InvoiceDTO dto = InvoiceDTO.builder()
                .id(5)
                .consecutive("FAC-000005")
                .customsCode("QWE")
                .type(InvoiceTypeEnum.EXPORTACION)
                .subtotal(new BigDecimal("2000"))
                .total(new BigDecimal("2000.00"))
                .createdAt(invoice.getCreatedAt())
                .createdBy("user@test.com")
                .build();

        when(invoiceRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(invoice), PageRequest.of(0, 10), 1));
        when(invoiceMapper.toDTO(invoice)).thenReturn(dto);
        when(numberToWordsService.convert(new BigDecimal("2000.00"))).thenReturn("two thousand");

        Page<InvoiceDTO> result = invoiceService.findAll(PageRequest.of(0, 10), null);

        assertEquals(1, result.getTotalElements());
        assertEquals("two thousand", result.getContent().get(0).getTotalInWords());
    }

    @Test
    void update_shouldApplyTaxAndPersistInvoice() {
        Invoice invoice = Invoice.builder()
                .id(7)
                .consecutive("FAC-000007")
                .customsCode("OLD")
                .type(InvoiceTypeEnum.NACIONAL)
                .subtotal(new BigDecimal("500"))
                .total(new BigDecimal("595.00"))
                .createdAt(LocalDate.now())
                .createdBy("user@test.com")
                .build();

        InvoiceDTO request = InvoiceDTO.builder()
                .customsCode("NEW")
                .type(InvoiceTypeEnum.NACIONAL)
                .subtotal(new BigDecimal("500"))
                .build();

        InvoiceTaxStrategy strategy = mock(InvoiceTaxStrategy.class);

        when(invoiceRepository.findById(7)).thenReturn(Optional.of(invoice));
        when(taxStrategyFactory.getStrategy(InvoiceTypeEnum.NACIONAL)).thenReturn(strategy);
        when(strategy.calculate(new BigDecimal("500")))
                .thenReturn(TaxCalculationResult.builder().iva(new BigDecimal("95.00")).withholding(BigDecimal.ZERO).total(new BigDecimal("595.00")).build());
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);
        when(invoiceMapper.toDTO(invoice)).thenReturn(InvoiceDTO.builder().id(7).consecutive("FAC-000007").customsCode("NEW").type(InvoiceTypeEnum.NACIONAL).subtotal(new BigDecimal("500")).iva(new BigDecimal("95.00")).withholding(BigDecimal.ZERO).total(new BigDecimal("595.00")).createdAt(invoice.getCreatedAt()).createdBy("user@test.com").build());

        InvoiceDTO result = invoiceService.update(7, request);

        assertEquals("NEW", result.getCustomsCode());
        assertEquals(new BigDecimal("595.00"), result.getTotal());
    }

    @Test
    void delete_shouldDeleteInvoiceWhenExists() {
        when(invoiceRepository.existsById(4)).thenReturn(true);

        invoiceService.delete(4);

        verify(invoiceRepository).deleteById(4);
    }

    @Test
    void delete_shouldThrowWhenInvoiceDoesNotExist() {
        when(invoiceRepository.existsById(99)).thenReturn(false);

        NoDataFoundException ex = assertThrows(NoDataFoundException.class, () -> invoiceService.delete(99));

        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    void findGroupedByType_shouldReturnTotals() {
        when(invoiceRepository.findGroupedByType()).thenReturn(List.of());

        List<?> result = invoiceService.findGroupedByType();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
