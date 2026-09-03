package com.jssv.globalinvoice.repository;

import com.jssv.globalinvoice.dto.TotalFacturasDTO;
import com.jssv.globalinvoice.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    @Query("""
        SELECT i
        FROM Invoice i
        WHERE
            LOWER(i.consecutive) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(i.createdBy) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(i.type) LIKE LOWER(CONCAT('%', :search, '%'))
        """)
    Page<Invoice> search(
            @Param("search") String search,
            Pageable pageable
    );

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
                    COALESCE(MAX(CAST(SUBSTRING(consecutive, 5) AS UNSIGNED)), 0),
                    6,
                    '0'
            ) AS siguiente_consecutivo
            FROM invoices;
            """, nativeQuery = true)
    int getLastConsecutiveNumber();
}
