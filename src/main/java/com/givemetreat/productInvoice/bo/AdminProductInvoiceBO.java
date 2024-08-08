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
public class AdminProductInvoiceBO {
	private final ProductInvoiceRepository productInvoiceRepository;
	
	/**
	 * invoiceId로만 product_invoice DB 테이블에서 record를 찾을 수 있지만,
	 * 검증 차원에서 userId를 추가;
	 * @param invoiceId
	 * @param userId
	 * @return
	 */
	public List<ProductInvoiceEntity> getProductInvoicesByInvoiceIdAndUserId(int invoiceId, int userId) {
		log.info("[AdminProductInvoiceBO getProductInvoicesByInvoiceIdAndUserId()] invoiceId:{}, userId:{}", invoiceId, userId);
		
		return productInvoiceRepository.findProductInvoiceByInvoiceIdAndUserId(invoiceId, userId);
	}

}
