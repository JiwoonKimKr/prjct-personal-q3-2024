package com.givemetreat.invoice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.givemetreat.invoice.domain.InvoiceEntity;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {

	List<InvoiceEntity> findInvoiceByHasCanceledAndStatusDeliveryInOrderByIdDesc(int i, List<String> listString);

	InvoiceEntity findInvoiceByIdAndUserId(int invoiceId, int userId);



}
