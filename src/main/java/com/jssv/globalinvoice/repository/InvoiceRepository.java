package com.jssv.globalinvoice.repository;

import com.jssv.globalinvoice.dto.TotalFacturasDTO;
import com.jssv.globalinvoice.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    @Query(value = """
            SELECT i.type, SUM(i.total) as totalAmount
            FROM Invoice i
            GROUP BY i.type
            ORDER BY i.type ASC
            """)
    List<TotalFacturasDTO> findGroupedByType();

    @Query(value = """
            SELECT
                LPAD(
                    COALESCE(MAX(CAST(SUBSTRING(consecutive, 5) AS UNSIGNED)), 0) + 1,
                    6,
                    '0'
            ) AS siguiente_consecutivo
            FROM invoices;
            """, nativeQuery = true)
    int getLastConsecutiveNumber();
}
