package com.givemetreat.invoice.bo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.givemetreat.common.validation.InvoiceParamsValidation;
import com.givemetreat.invoice.domain.Invoice;
import com.givemetreat.invoice.domain.InvoiceEntity;
import com.givemetreat.invoice.domain.InvoiceVO;
import com.givemetreat.invoice.mapper.InvoiceMapper;
import com.givemetreat.invoice.repository.InvoiceRepository;
import com.givemetreat.product.bo.ProductBO;
import com.givemetreat.product.domain.ProductVO;
import com.givemetreat.productBuffer.bo.ProductBufferBO;
import com.givemetreat.productBuffer.domain.ProductBufferEntity;
import com.givemetreat.productInvoice.bo.ProductInvoiceBO;
import com.givemetreat.productInvoice.domain.ItemOrderedDto;
import com.givemetreat.productInvoice.domain.ItemOrderedVO;
import com.givemetreat.productInvoice.domain.ProductInvoiceEntity;
import com.givemetreat.productShoppingCart.bo.ProductShoppingCartBO;
import com.givemetreat.productShoppingCart.domain.ProductShoppingCartVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class InvoiceBO {
	private final InvoiceRepository invoiceRepository;
	private final InvoiceMapper invoiceMapper;
	
	private final ProductBO productBO;
	private final ProductInvoiceBO productInvoiceBO;
	private final ProductBufferBO productBufferBO;
	private final ProductShoppingCartBO productShoppingCartBO;
	
	public List<InvoiceVO> getInvoices(Integer invoiceId
										, Integer userId
										, Integer payment
										/* 포트원 결제 방식 도입으로 해당 Column 삭제_21 08 2024
										, String paymentType
										, String company
										, String monthlyInstallment
										*/
										, Integer hasCanceled
										, String buyerName
										, String buyerPhoneNumber
										, String statusDelivery
										, String receiverName
										, String receiverPhoneNumber
										, String address
										, LocalDateTime createdAtSince , LocalDateTime createdAtUntil 
										, LocalDateTime updatedAtSince , LocalDateTime updatedAtUntil) {
		List<Invoice> list = invoiceMapper.selectInvoicesBetweenDates( invoiceId
																, userId
																, payment
																/* 포트원 결제 방식 도입으로 해당 Column 삭제_21 08 2024
																, paymentType
																, company
																, monthlyInstallment
																*/
																, hasCanceled
																, buyerName
																, buyerPhoneNumber
																, statusDelivery
																, receiverName
																, receiverPhoneNumber
																, address
																, createdAtSince, createdAtUntil
																, updatedAtSince, updatedAtUntil);
		
		List<InvoiceVO> listVOs = list.stream()
								.map(entity -> new InvoiceVO(entity))
								.collect(Collectors.toList());
		return listVOs;
	}	
	@Transactional
	public List<InvoiceVO> getListInvoicesByUserId(Integer userId) {
		List<InvoiceEntity> list = invoiceRepository.findByUserIdOrderByIdDesc(userId);
		List<InvoiceVO> listVOs = list.stream()
									.map(entity -> new InvoiceVO(entity))
									.collect(Collectors.toList());
		return listVOs;
	}	
	
	@Transactional
	public InvoiceEntity getEntityById(Integer invoiceId) {
		return invoiceRepository.findById(invoiceId).orElse(null);
	}
	
	@Transactional
	public InvoiceVO getInvoiceById(Integer invoiceId) {
		InvoiceEntity entity= invoiceRepository.findById(invoiceId).orElse(null);
		return new InvoiceVO(entity);
	}	
	
	@Transactional
	public List<InvoiceVO> getListInvoicesByIdDeliveryNotFinished(Integer userId) {
		List<String> listString = new ArrayList<>(Arrays.asList("DeliveryFinished"));
		
		List<InvoiceEntity> list = invoiceRepository.findByUserIdAndHasCanceledAndStatusDeliveryNotInOrderByIdDesc(userId, 0, listString);
		List<InvoiceVO> listVOs = list.stream().map(entity -> new InvoiceVO(entity)).collect(Collectors.toList());
		return listVOs;
	}
	
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

	@Transactional
	public InvoiceEntity generateInvoiceFromJsonString(String jsonString
												, int userId
												, Integer payment
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
		for(ItemOrderedDto item: listItemOrderedDto) {
			int productId = item.getProductId();
			
			//productBuffer에서 count함수로 해당 물품 가용 재고 확인하기
			//현재 가용 재고가 주문 수량보다 값이 커야한다!
			Integer bufferAvailbale = productBufferBO.getCountAvailableByProductIdAndReserved(productId, false);
			
			if(ObjectUtils.isEmpty(bufferAvailbale) || bufferAvailbale < item.getQuantity()) {
				log.warn("[InvoiceBO generateInvoiceFromJsonString()]" 
						+ " current item's stock is lower than quantity from order."
						+ " productId:{}", productId);
				return null;
			}
		}
		
		//Total Cost Validation;
		if(payment != sumCost || payment != sumServerSide) {
			log.warn("[InvoiceBO generateInvoiceFromJsonString()]" 
					+ " Current passed from request doesn't match with cost summary."
					+ " userId:{}, payment from Client:{}, total cost from Ordered Info:{}, total cost from Server"
					, userId, payment, sumCost, sumServerSide);
			return null;
		}
		
		//validating each parameters for Invoice before sending payment invoice for PG Company
		
		Boolean hasParamsforInvoiceValidated = InvoiceParamsValidation.getParamsValidated(payment
																						, buyerName
																						, buyerPhoneNumber
																						, receiverName
																						, receiverPhoneNumber
																						, address);
		
		if(hasParamsforInvoiceValidated == false) {
			log.warn("[InvoiceBO generateInvoiceFromJsonString()]" 
					+ " Some of values from Request Parameters failed to get validated."
					+ " userId:{}", userId);
			return null;
		}
		
		//Building Invoice Entity
		InvoiceEntity invoice = InvoiceEntity.builder()
											.userId(userId)
											.payment(payment)
											.hasCanceled(0) //취소 안된, 결제 완료 상태일 때는 0
											.buyerName(buyerName)
											.buyerPhoneNumber(buyerPhoneNumber)
											.statusDelivery("PaymentBilled")
											.receiverName(receiverName)
											.receiverPhoneNumber(receiverPhoneNumber)
											.address(address)
											.build();
		invoiceRepository.save(invoice);
		int invoiceId = invoice.getId();
		
		//ProductInvoice building
		for(ItemOrderedDto item: listItemOrderedDto) {
			int productId = item.getProductId();
			ProductInvoiceEntity itemInvoice = productInvoiceBO.addProductInvoice(userId
																		, invoiceId
																		, productId);
			if(ObjectUtils.isEmpty(itemInvoice)) {
				log.warn("[InvoiceBO generateInvoiceFromJsonString()]" 
						+ " ProductInvoiceEntity failed to get saved."
						+ " ItemOrderedDto:{}", item);
				return null;
			}
			
			int productInvoiceId = itemInvoice.getId();
			item.setProductInvoiceId(productInvoiceId);
			
			//productBuffer update!(Building 아님!)
				//기존 buffer 중 productId가 동일하고 가장 PK값이 낮은, 오래된 Buffer를 주문 수량만큼 'reserved'와 해당 productInvoiceId를 표시해야
			List<ProductBufferEntity> listItemQuantity = productBufferBO.updateProductBuffersInQuantity(productId, item.getQuantity(), productInvoiceId);
			
			if(ObjectUtils.isEmpty(listItemQuantity) || listItemQuantity.size() != item.getQuantity()) {
				log.warn("[InvoiceBO generateInvoiceFromJsonString()]" 
						+ " ProductBufferEntity failed to get saved correctly."
						+ " ItemOrderedDto:{}", item);
			}
		}
		return invoice;
	}

	public List<ItemOrderedVO> getItemsOrderedByUserIdAndInvoiceId(Integer userId, Integer invoiceId) {
		List<ProductInvoiceEntity> listProductInvoices = productInvoiceBO.getProductInvoicesByInvoiceIdAndUserId(invoiceId, userId);
		
		//1)product_buffer 갯수 Map구조로 수량 파악
		Map<ProductVO, Integer> mapProductVOs = new HashMap<>();
		
		for(ProductInvoiceEntity productInvoice : listProductInvoices) {
			int productInvoiceId = productInvoice.getId();
			int productId = productInvoice.getProductId();

			Integer count = productBufferBO.getCount(productId, true, productInvoiceId);

			if(count == null) {
				continue;
			}
			
			ProductVO productVO = productBO.getProducts(productId, null, null, null, null).get(0);
			
			//productInvoiceId productVO에 기입
			productVO.setProductInvoiceId(productInvoiceId);
			
			if(mapProductVOs.containsKey(productVO)) {
				log.warn("[InvoiceBO getProductInvoicesByInvoiceIdAndUserId()]"
						+ " item ordered in Current Invoice has shown duplicated in wrong way! invoiceId:{}", invoiceId);
				continue;
			}
			mapProductVOs.put(productVO, count);
		}
		
		//2) AdminProductInvoiceVO 리스트 반환
		List<ItemOrderedVO> listVOs = new ArrayList<>();
		
		Set<ProductVO> keys = mapProductVOs.keySet();
		Iterator<ProductVO> iter = keys.iterator();
		
		while(iter.hasNext()) {
			ProductVO itemOrdered = iter.next();
			
			listVOs.add(new ItemOrderedVO(itemOrdered
					, mapProductVOs.get(itemOrdered)));
		}
		
		return listVOs;
	}
	
	public void deleteInvoiceAndResetBuffer(InvoiceEntity invoice, int userId) {
		int invoiceId = invoice.getId();
		
		List<ProductInvoiceEntity> listProductInvoiceEntities =
				productInvoiceBO.getProductInvoicesByInvoiceIdAndUserId(invoiceId, userId);
		
		for(ProductInvoiceEntity productInvoice: listProductInvoiceEntities) {
			int productInvoiceId = productInvoice.getId();
			
			//productBuffer 초기화
			//invoice-productInvoice 테이블에 등록됬던 재고들 모두 초기화
			List<ProductBufferEntity> listBuffers = productBufferBO.resetProductBuffersByProductInvoiceId(productInvoiceId);
			if(ObjectUtils.isEmpty(listBuffers)) {
				log.warn("[InvoiceBO deleteInvoice()]"
						+ "List of ProductBuffers failed to get reset. productInvoiceId:{}", productInvoiceId);
			}
		}
		
		//ProductInvoice 삭제
		Boolean hasProductInvoiceDeleted = productInvoiceBO.deleteListProductInvoiceEntities(listProductInvoiceEntities);
		
		if(hasProductInvoiceDeleted == false) {
			log.warn("[InvoiceBO deleteInvoice()]"
					+ "List of ProductInvoices failed to get deleted. Invoice Id:{}", invoiceId);
		}
		
		//invoice 삭제
		invoiceRepository.delete(invoice);
		log.info("[InvoiceBO deleteInvoice()]"
				+ "invoice get deleted. invoiceId:{}", invoiceId);
	}

}
