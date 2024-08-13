package com.givemetreat.productInvoice.bo;

import org.springframework.stereotype.Service;

import com.givemetreat.productInvoice.domain.ProductInvoiceEntity;
import com.givemetreat.productInvoice.repository.ProductInvoiceRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductInvoiceBO {
	private final ProductInvoiceRepository productInvoiceRepository;

	public ProductInvoiceEntity addProductInvoice(int userId
												, int invoiceId
												, Integer productId) {
		return productInvoiceRepository.save(ProductInvoiceEntity.builder()
																.userId(userId)
																.invoiceId(invoiceId)
																.productId(productId)
																.build());
	}

}
