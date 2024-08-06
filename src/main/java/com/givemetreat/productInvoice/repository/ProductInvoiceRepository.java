package com.givemetreat.productInvoice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.givemetreat.productInvoice.domain.ProductInvoiceEntity;

public interface ProductInvoiceRepository extends JpaRepository<ProductInvoiceEntity, Integer> {

	List<ProductInvoiceEntity> findProductInvoiceByInvoiceIdAndUserId(int invoiceId, int userId);

}
