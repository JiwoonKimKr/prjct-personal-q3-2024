package com.givemetreat.invoice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.givemetreat.invoice.domain.HasCanceled;
import com.givemetreat.invoice.domain.InvoiceEntity;
import com.givemetreat.invoice.domain.StatusDelivery;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {

	List<InvoiceEntity> findInvoiceByHasCanceledAndStatusDeliveryInOrderByIdDesc(HasCanceled hasCanceled, List<StatusDelivery> listString);

	InvoiceEntity findInvoiceByIdAndUserId(int invoiceId, int userId);

	List<InvoiceEntity> findByUserIdAndHasCanceledAndStatusDeliveryNotInOrderByIdDesc(Integer userId
																					, HasCanceled hasCanceled
																					, List<StatusDelivery> listString);

	List<InvoiceEntity> findByUserIdOrderByIdDesc(Integer userId);

}
