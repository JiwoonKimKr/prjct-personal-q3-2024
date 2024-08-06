package com.givemetreat.productInvoice.bo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.givemetreat.productInvoice.domain.ProductInvoiceEntity;
import com.givemetreat.productInvoice.repository.ProductInvoiceRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminProductInvoiceBO {
	private final ProductInvoiceRepository productInvoiceRepository;
	
	public List<ProductInvoiceEntity> getProductInvoicesByInvoiceIdAndUserId(int invoiceId, int userId) {
		return productInvoiceRepository.findProductInvoiceByInvoiceIdAndUserId(invoiceId, userId);
	}

}
