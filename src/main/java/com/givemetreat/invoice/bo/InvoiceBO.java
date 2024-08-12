package com.givemetreat.invoice.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.givemetreat.invoice.domain.InvoiceDto;
import com.givemetreat.invoice.domain.ItemOrderedDto;
import com.givemetreat.product.bo.ProductBO;
import com.givemetreat.product.domain.ProductVO;
import com.givemetreat.productShoppingCart.bo.ProductShoppingCartBO;
import com.givemetreat.productShoppingCart.domain.ProductShoppingCartVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class InvoiceBO {
	private final ProductShoppingCartBO productShoppingCartBO;
	private final ProductBO productBO;
	
	@Transactional
	public List<ProductShoppingCartVO> getListProductsFromCartByUserId(Integer userId) {
		return productShoppingCartBO.getProductsByUserId(userId);
	}

	@Transactional
	public List<ProductShoppingCartVO> getProductByProductIdAndQuantity(int productId, int quantity) {
		ProductVO product = productBO.getProducts(productId, null, null, null, null).get(0);
		ProductShoppingCartVO vo = new ProductShoppingCartVO(-1, product, quantity);
		List<ProductShoppingCartVO> listVOs = new ArrayList<>();
		listVOs.add(vo);
		return listVOs;
	}

	/**
	 *  결제 요청 메소드
	 * @param listItemsOrdered
	 * @param paymentType
	 * @param company
	 * @param monthlyInstallment
	 * @param buyerName
	 * @param buyerPhoneNumber
	 * @param statusDelivery
	 * @param receiverName
	 * @param receiverPhoneNumber
	 * @param address
	 * @return Boolean
	 */
	@Transactional
	public Boolean generateInvoice( List<ItemOrderedDto> listItemsOrdered
									, Integer payment
									, String paymentType
									, String company
									, String monthlyInstallment
									, String buyerName
									, String buyerPhoneNumber
									, String receiverName
									, String receiverPhoneNumber
									, String address){
		//요청 금액과 총 금액이 동일한지 점검
		int sum = 0;
		for(ItemOrderedDto mapItem : listItemsOrdered) {
			Boolean hasChecked = (Boolean) mapItem.getChecked().equals("checked");
			if(hasChecked == false) {
				log.warn("[InvoiceBO generateInvoice()] Unchecked Item got submitted to server.");
				return false;
			}
			
			Integer productId = (Integer) mapItem.getProductId();
			Integer quantity = (Integer) mapItem.getQuantity();
			Integer price = (Integer) mapItem.getPrice();
			int costCurrent = price * quantity;
			
			int priceProduct = productBO.getProducts(productId, null, null, null, null).get(0).getPrice();
			if(costCurrent != priceProduct * costCurrent) {
				log.warn("[InvoiceBO generateInvoice()] current order doesn't match with price of product. productId:{}", productId);
				return false;
			}
			
			sum += costCurrent;
		}
		
		if(sum != payment) {
			log.warn("[InvoiceBO generateInvoice()] payment failed to pass validation. payment:{}", payment);
		}
		
		//장바구니 내 정보와 일치하는지 
		
		//결제 요청; ★★★★★나중에 PG사 연동시켜서 확인해봐야!
		Boolean hasPaymentSuccessed = true;
		if(hasPaymentSuccessed == false) {
			return false;
		}
		
		//결제 성공 확인 후 Invoice 생성
			//Invoice 생성후 productInvoice 생성
				//productBuffer에 reserved 표시
		
		
		
		return true;
	}

	@Transactional
	public Boolean generateInvoiceFromJsonString(String jsonString) {
		log.info("[InvoiceBO generateInvoiceFromJsonString()] dto has arrived :{}", jsonString);
		
		ObjectMapper objectMapper = new ObjectMapper();
		InvoiceDto invoiceDto = null;
		try {
			invoiceDto = objectMapper.readValue(jsonString, InvoiceDto.class);
		} catch (JsonProcessingException e) {
			log.warn("[InvoiceBO generateInvoiceFromJsonString()] failed to generated InvoiceDto. json String :{}", jsonString);
		}
		return false;
	}

}
