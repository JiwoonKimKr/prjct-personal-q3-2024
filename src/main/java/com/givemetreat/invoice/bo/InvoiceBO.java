package com.givemetreat.invoice.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.givemetreat.common.validation.InvoiceParamsValidation;
import com.givemetreat.invoice.domain.InvoiceEntity;
import com.givemetreat.invoice.domain.ItemOrderedDto;
import com.givemetreat.invoice.repository.InvoiceRepository;
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
	private final InvoiceRepository invoiceRepository;
	
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

	public Boolean generateInvoiceFromJsonString(String jsonString
												, int userId
												, Integer payment
												, String paymentType
												, String company
												, String monthlyInstallment
												, String buyerName
												, String buyerPhoneNumber
												, String receiverName
												, String receiverPhoneNumber
												, String address){
		JSONArray jsonArray = new JSONArray();
		List<ItemOrderedDto> listItemOrderedDto = new ArrayList<>();
		int sumCost = 0;
		int sumServerSide = 0;
		try {
			jsonArray = new JSONArray(jsonString);
			for(int i=0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				ItemOrderedDto item = new ItemOrderedDto();
				item.setCartItemId(Integer.parseInt(jsonObject.get("cartItemId").toString()));
				item.setProductId(Integer.parseInt(jsonObject.get("productId").toString()));
				item.setQuantity(Integer.parseInt(jsonObject.get("quantity").toString()));
				item.setPrice(Integer.parseInt(jsonObject.get("price").toString()));
				item.setChecked(jsonObject.get("checked").toString());
				log.info("[InvoiceBO generateInvoiceFromJsonString()]"
						+ " Current itemOrderedDto generated. item: {}", item.toString());
				
				sumCost += item.getPrice() * item.getQuantity();
				int priceServerSide = productBO.getProducts(item.getProductId(), null, null, null, null).get(0).getPrice();
				sumServerSide += priceServerSide * item.getQuantity();
				listItemOrderedDto.add(item);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//List<ItemOrderedDto> generated
		log.info("[InvoiceBO generateInvoiceFromJsonString()] Json String to listItemOrderedDto. listItemOrderedDto:{}", listItemOrderedDto.toString());
		
	//validation before Sending invoice to PG(Payment Gateway) company
		//product Quantity Validation
		
		
		//Total Cost Validation;
		if(payment != sumCost || payment != sumServerSide) {
			log.warn("[InvoiceBO generateInvoiceFromJsonString()]" 
					+ " Current passed from request doesn't match with cost summary."
					+ " userId:{}, payment from Client:{}, total cost from Ordered Info:{}, total cost from Server"
					, userId, payment, sumCost, sumServerSide);
			return false;
		}
		
		//validating each parameters for Invoice before sending payment invoice for PG Company
		
		Boolean hasParamsforInvoiceValidated = InvoiceParamsValidation.getParamsValidated(payment
																						, paymentType
																						, company
																						, monthlyInstallment
																						, buyerName
																						, buyerPhoneNumber
																						, receiverName
																						, receiverPhoneNumber
																						, address);
		
		if(hasParamsforInvoiceValidated == false) {
			log.warn("[InvoiceBO generateInvoiceFromJsonString()]" 
					+ " Some of values from Request Parameters failed to get validated."
					+ " userId:{}", userId);
			return false;
		}
		
		//★★★★★sending payment invoice for PG Com 
		
		
		//Building Invoice Entity
		
		InvoiceEntity invoice = InvoiceEntity.builder()
											.userId(userId)
											.payment(payment)
											.paymentType(paymentType)
											.company(company)
											.monthlyInstallment(monthlyInstallment)
											.hasCanceled(0) //취소 안된, 결제 완료 상태일 때는 0
											.buyerName(buyerName)
											.buyerPhoneNumber(buyerPhoneNumber)
											.statusDelivery("PaymentBilled")
											.receiverName(receiverName)
											.receiverPhoneNumber(receiverPhoneNumber)
											.address(address)
											.build();
		
		
		//ProductInvoice building
		
		//productBuffer building
				
		return false;
	}

}
