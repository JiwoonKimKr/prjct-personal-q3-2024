package com.givemetreat.productUserInterested.bo;

import org.springframework.stereotype.Service;

import com.givemetreat.productUserInterested.domain.ProductUserInterestedEntity;
import com.givemetreat.productUserInterested.repository.ProductUserInterestedRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductUserInterestedBO {
	private final ProductUserInterestedRepository productUserInterestedRepository;

	@Transactional
	public ProductUserInterestedEntity getListProductsFavoredLatest(Integer userId) {
		return productUserInterestedRepository.findTop1ByUserIdOrderByCreatedAtDesc(userId);
	}
	
	/**
	 * 
	 * ProductController productDetailView()로 넘어오는 경우에는
	 * fromClicked=true로 기재해야
	 * 
	 * InvoiceBO generateInvoiceFromJsonString()로 넘어오는 경우에는,
	 * fromPayment=true, size = countOfItemsOrdered로 기재해야
	 * 
	 * @param userId
	 * @param productId
	 * @param fromClicked
	 * @param fromPayment
	 * @param size
	 * @return
	 */
	@Transactional
	public ProductUserInterestedEntity addRecordForProductUserInterested(int userId
																		, int productId
																		, Boolean fromClicked
																		, Boolean fromPayment
																		, Integer size) {
		return productUserInterestedRepository.save(ProductUserInterestedEntity.builder()
																				.userId(userId)
																				.productId(productId)
																				.fromClicked(fromClicked)
																				.fromPayment(fromPayment)
																				.countOrdered(size)
																				.build());
	}
}
