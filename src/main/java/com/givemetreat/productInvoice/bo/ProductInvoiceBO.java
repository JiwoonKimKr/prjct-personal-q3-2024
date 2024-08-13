package com.givemetreat.productInvoice.bo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.givemetreat.productInvoice.domain.ProductInvoiceEntity;
import com.givemetreat.productInvoice.repository.ProductInvoiceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	public List<ProductInvoiceEntity> getProductInvoicesByInvoiceIdAndUserId(int invoiceId, int userId) {
		log.info("[ProductInvoiceBO getProductInvoicesByInvoiceIdAndUserId()] invoiceId:{}, userId:{}", invoiceId, userId);
		
		return productInvoiceRepository.findProductInvoiceByInvoiceIdAndUserId(invoiceId, userId);
	}
}
