package com.jssv.globalinvoice.entity;

import com.jssv.globalinvoice.enums.InvoiceTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "consecutive", length = 10, nullable = false)
    private String consecutive;

    @Column(name = "customs_code", length = 20)
    private String customsCode;

    @Column(name = "type", length = 20,nullable = false)
    @Enumerated(EnumType.STRING)
    private InvoiceTypeEnum type;

    @Column(name = "subtotal", length = 20, nullable = false)
    private BigDecimal subtotal;

    @Column(name = "iva", length = 20)
    private BigDecimal iva;

    @Column(name = "withholding", length = 20)
    private BigDecimal withholding;

    @Column(name = "total", length = 20, nullable = false)
    private BigDecimal total;

    @Column(name = "created_at", length = 20, nullable = false)
    private LocalDate createdAt;

    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;
}
