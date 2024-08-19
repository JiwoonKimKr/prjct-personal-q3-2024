package com.givemetreat.invoice.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.givemetreat.invoice.domain.Invoice;

@Mapper
public interface InvoiceMapper {

	//Field 하나만 선택해서 검색하도록 진행되어야;
	List<Invoice> selectInvoices(
							@Param("id") Integer invoiceId
							, @Param("userId") Integer userId
							, @Param("payment") Integer payment
							, @Param("paymentType") String paymentType
							, @Param("company") String company
							, @Param("monthlyInstallment") String monthlyInstallment
							, @Param("hasCanceled") Integer hasCanceled
							, @Param("buyerName") String buyerName
							, @Param("buyerPhoneNumber") String buyerPhoneNumber
							, @Param("statusDelivery") String statusDelivery
							, @Param("receiverName") String receiverName
							, @Param("receiverPhoneNumber") String receiverPhoneNumber
							, @Param("address") String address
							, @Param("createdAt") LocalDateTime createdAt
							, @Param("updatedAt") LocalDateTime updatedAt);

	List<Invoice> selectInvoicesBetweenDates(@Param("id") Integer invoiceId
											, @Param("userId") Integer userId
											, @Param("payment") Integer payment
											, @Param("paymentType") String paymentType
											, @Param("company") String company
											, @Param("monthlyInstallment") String monthlyInstallment
											, @Param("hasCanceled") Integer hasCanceled
											, @Param("buyerName") String buyerName
											, @Param("buyerPhoneNumber") String buyerPhoneNumber
											, @Param("statusDelivery") String statusDelivery
											, @Param("receiverName") String receiverName
											, @Param("receiverPhoneNumber") String receiverPhoneNumber
											, @Param("address") String address
											, @Param("createdAtSince") LocalDateTime createdAtSince
											, @Param("createdAtUntil") LocalDateTime createdAtUntil
											, @Param("updatedAtSince") LocalDateTime updatedAtSince
											, @Param("updatedAtUntil") LocalDateTime updatedAtUntil);

	List<Invoice> selectInvoicesForPaging(
							@Param("id") Integer invoiceId
							, @Param("userId") Integer userId
							, @Param("payment") Integer payment
							, @Param("paymentType") String paymentType
							, @Param("company") String company
							, @Param("monthlyInstallment") String monthlyInstallment
							, @Param("hasCanceled") Integer hasCanceled
							, @Param("buyerName") String buyerName
							, @Param("buyerPhoneNumber") String buyerPhoneNumber
							, @Param("statusDelivery") String statusDelivery
							, @Param("receiverName") String receiverName
							, @Param("receiverPhoneNumber") String receiverPhoneNumber
							, @Param("address") String address
							, @Param("createdAtSince") LocalDateTime createdAtSince
							, @Param("createdAtUntil") LocalDateTime createdAtUntil
							, @Param("updatedAtSince") LocalDateTime updatedAtSince
							, @Param("updatedAtUntil") LocalDateTime updatedAtUntil
							, @Param("direction") String direction
							, @Param("idRequested") Integer idRequested
							, @Param("limit") Integer limit);

}
