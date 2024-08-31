package com.givemetreat.invoice.bo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.givemetreat.common.generic.Page;
import com.givemetreat.common.generic.VOforIndexing;
import com.givemetreat.common.utils.IndexBinarySearchTreeUtil;
import com.givemetreat.invoice.domain.AdminInvoiceVO;
import com.givemetreat.invoice.domain.HasCanceled;
import com.givemetreat.invoice.domain.Invoice;
import com.givemetreat.invoice.domain.InvoiceEntity;
import com.givemetreat.invoice.domain.StatusDelivery;
import com.givemetreat.invoice.mapper.InvoiceMapper;
import com.givemetreat.invoice.repository.InvoiceRepository;
import com.givemetreat.product.bo.AdminProductBO;
import com.givemetreat.product.domain.AdminProductVO;
import com.givemetreat.productBuffer.bo.ProductBufferBO;
import com.givemetreat.productInvoice.bo.AdminProductInvoiceBO;
import com.givemetreat.productInvoice.domain.AdminProductInvoiceVO;
import com.givemetreat.productInvoice.domain.ProductInvoiceEntity;
import com.givemetreat.user.bo.UserBO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminInvoiceBO {
	private final InvoiceRepository invoiceRepository;
	private final InvoiceMapper invoiceMapper;
	
	private final UserBO userBO;
	private final AdminProductInvoiceBO adminProductInvoiceBO;
	private final AdminProductBO adminProductBO;
	private final ProductBufferBO productBufferBO;
	
	private final IndexBinarySearchTreeUtil indexBinarySearchTreeUtil;

	private final int LIMIT_SELECTION = 10;

	@Transactional
	public Page<AdminInvoiceVO> getInvoicesForPaging(Integer invoiceId
													, Integer userId
													, Integer paymentFrom
													, Integer paymentUntil
													/* 포트원 적용으로 삭제_21 08 2024
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
													, LocalDateTime createdAtSince
													, LocalDateTime createdAtUntil
													, LocalDateTime updatedAtSince
													, LocalDateTime updatedAtUntil
													, String direction
													, Integer idRequested
													, Integer pageCurrent
													, Integer pageRequested) {
		//direction이랑 paging이 필요한 경우
		
		// Enum Type 추가_27 08 2024
		HasCanceled hasCanceledCurrent = null;
		StatusDelivery statusCurrent = null;
		
		// 출발 금액이 상한 금액 보다 클 때 맞바꾸도록
		if(ObjectUtils.isEmpty(paymentFrom) == false 
				&& ObjectUtils.isEmpty(paymentUntil) == false) {
			paymentFrom = paymentUntil > paymentFrom ? paymentFrom : paymentUntil;
			paymentUntil = paymentFrom < paymentUntil ? paymentUntil : paymentFrom;
			log.info("[AdminInvoiceBO getInvoicesForPaging()]"
					+ " paymentFrom & paymentUntil get swapped. paymentFrom:{}, paymentUntil:{}", paymentFrom, paymentUntil);
		}
		
		if(ObjectUtils.isEmpty(hasCanceledCurrent) == false) {
			hasCanceledCurrent = HasCanceled.findIfPaymentCanceled(hasCanceled, null);
		}
		if(ObjectUtils.isEmpty(hasCanceledCurrent) == false) {
			statusCurrent = StatusDelivery.findStatusDelivery(statusDelivery, null, null);
		}
		List<Invoice> listEntitiesWhole = invoiceMapper.selectInvoicesForPaging( invoiceId
																, userId
																, paymentFrom
																, paymentUntil
																/* 포트원 적용으로 삭제_21 08 2024
																, paymentType
																, company
																, monthlyInstallment
																*/
																, hasCanceledCurrent
																, buyerName
																, buyerPhoneNumber
																, statusCurrent
																, receiverName
																, receiverPhoneNumber
																, address
																, createdAtSince
																, createdAtUntil
																, updatedAtSince
																, updatedAtUntil
																, null
																, null
																, null);
		
		if(ObjectUtils.isEmpty(listEntitiesWhole)) {
			return null;
		}
		
		List<AdminInvoiceVO> listVOs = new ArrayList<>();
		for(Invoice invoice: listEntitiesWhole) {
			AdminInvoiceVO vo = new AdminInvoiceVO(invoice);
			
			Integer userIdCur = userId;
			if(userIdCur == null) {
				userIdCur = invoice.getUserId();
			}
			vo.setLoginId(userBO.getUserById(userIdCur).getLoginId());
			listVOs.add(vo);
		}
		
		Integer index = null;
		//아무런 요청값이 없는, 메인 페이지를 띄울 때
			//요청 id가 없고, 방향도 없는 경우; 요청 id는 가장 큰 id값(첫 번째)으로, 방향은 prev로
		if(ObjectUtils.isEmpty(idRequested)
			&& ObjectUtils.isEmpty(direction)
			&& ObjectUtils.isEmpty(pageCurrent)
			&& ObjectUtils.isEmpty(pageRequested)){
			index = 0;
			idRequested = listVOs.get(0).getId();
			direction = "prev";
		}
		
		if(ObjectUtils.isEmpty(idRequested)
			&& ObjectUtils.isEmpty(direction)
			&& ObjectUtils.isEmpty(pageCurrent) == false
			&& ObjectUtils.isEmpty(pageRequested) == false){
			direction = (pageRequested - pageCurrent) <= 0 ? "prev" : "next";
			if(direction.equals("prev")) {
				index = (pageRequested - 1) * LIMIT_SELECTION;
				index = index > 0 ? index : 0;
			} else if(direction.equals("next")) {
				index = pageRequested * LIMIT_SELECTION - 1;
				index = index <= listVOs.size() - 1 ? index : listVOs.size() - 1; 
			}
			idRequested = listVOs.get(index).getId();
		}
		
		//index 아직 입력 안 된 경우 반복문으로 찾기
		if(index == null) {
			@SuppressWarnings("unchecked")
			List<VOforIndexing> list = (List<VOforIndexing>) (List<?>) listVOs;
			index = indexBinarySearchTreeUtil.findIndexFromList(list, idRequested);
		}
		
		Page<AdminInvoiceVO> pageInfo = new Page<AdminInvoiceVO>(listVOs
														, listVOs.get(0).getId()
														, listVOs.get(listVOs.size() - 1).getId()
														, direction
														, index
														, idRequested
														, LIMIT_SELECTION);
		
		log.info("[AdminInvoiceBO getInvoicesForPaging()] new Page<AdminInvoiceVO>:", pageInfo);
		
		return pageInfo;	
	}
	
	@Transactional
	public AdminInvoiceVO getInvoiceByInvoiceIdAndUserId(int id, int userId) {
		InvoiceEntity invoice = invoiceRepository.findInvoiceByIdAndUserId(id, userId);
		
		//사용자 loginId 찾아서 넣기
		AdminInvoiceVO vo = new AdminInvoiceVO(invoice);
		vo.setLoginId(userBO.getUserById(userId).getLoginId());
		return vo;
	}
	
	@Transactional
	public List<AdminInvoiceVO> getListInvoicesPayedRecently() {
		//주문 취소 X(DB값 0;취소하면 1) & 택배 상차 완료 X (포장까지만 완료)
		
		List<StatusDelivery> listStatus = new ArrayList<>(Arrays.asList(StatusDelivery.PaymentBilled, StatusDelivery.PackingFinished));
		
		// Enum Type 추가 _ 27 08 2024
		HasCanceled ifCanceled = HasCanceled.billed;  
		List<InvoiceEntity> listInvoiceLatest = invoiceRepository.
				findInvoiceByHasCanceledAndStatusDeliveryInOrderByIdDesc(ifCanceled, listStatus);
		//AdminInvoiceVO로 변환해서 Controller에 보낼 예정;
		List<AdminInvoiceVO> listInvoiceVO = new ArrayList<>();
		
		for(InvoiceEntity invoice: listInvoiceLatest) {
			AdminInvoiceVO vo = new AdminInvoiceVO(invoice);
			
			//사용자 loginId 찾아서 넣기
			int userId = invoice.getUserId();
			vo.setLoginId(userBO.getUserById(userId).getLoginId());
			listInvoiceVO.add(vo);
		}
		log.info("[AdminInvoiceBO: getListInvoicesPayedRecently()]"
				+ " List of InvoiceEntity translated to List of AdminInvoiceVO");
		
		return listInvoiceVO;
	}

	@Transactional
	public List<AdminProductInvoiceVO> getProductInvoicesByInvoiceIdAndUserId(int invoiceId, int userId) {
		List<ProductInvoiceEntity> listProductInvoices = adminProductInvoiceBO.getProductInvoicesByInvoiceIdAndUserId(invoiceId, userId);

		//1)product_buffer 갯수 Map구조로 수량 파악
		Map<AdminProductVO, Integer> mapProductVOs = new HashMap<>();
		
		for(ProductInvoiceEntity productInvoice : listProductInvoices) {
			int productInvoiceId = productInvoice.getId();
			int productId = productInvoice.getProductId();

			Integer count = productBufferBO.getCount(productId, true, productInvoiceId);

			if(count == null) {
				continue;
			}
			
			AdminProductVO productVO = adminProductBO.getProduct(productId, null, null, null, null).get(0);
			
			//중복된 값이 리스트에 존재한다는 것 자체가 주문이 잘못 전달 된 상황; 에러!
			if(mapProductVOs.containsKey(productVO)) {
				log.warn("[AdminInvoiceBO getProductInvoicesByInvoiceIdAndUserId()]"
						+ " item ordered in Current Invoice has shown duplicated in wrong way! invoiceId:{}", invoiceId);
				continue;
			}
			
			mapProductVOs.put(productVO, count);
		}
		
		//2) AdminProductInvoiceVO 리스트 반환
		List<AdminProductInvoiceVO> listVOs = new ArrayList<>();
		
		Set<AdminProductVO> keys = mapProductVOs.keySet();
		Iterator<AdminProductVO> iter = keys.iterator();
		
		while(iter.hasNext()) {
			AdminProductVO itemOrdered = iter.next();
			
			listVOs.add(new AdminProductInvoiceVO(itemOrdered
					, mapProductVOs.get(itemOrdered)));
		}
		
		return listVOs;
	}

	//필드 여러 개 조회할 수 있도록 MyBatis 수정할 수 있었다!_14 08 2024
	public List<AdminInvoiceVO> getInvoices(Integer invoiceId
										, Integer userId
										, Integer payment
										/* 포트원 적용으로 삭제_21 08 2024
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
										, LocalDateTime createdAt
										, LocalDateTime updatedAt) {
		// Enum Type 추가_27 08 2024
		HasCanceled hasCanceledCurrent = HasCanceled.findIfPaymentCanceled(hasCanceled, null);
		StatusDelivery statusCurrent = StatusDelivery.findStatusDelivery(statusDelivery, null, null);
		List<Invoice> listInvoices = invoiceMapper.selectInvoices( invoiceId
											, userId
											, payment
											/* 포트원 적용으로 삭제_21 08 2024
											, paymentType
											, company
											, monthlyInstallment
											*/
											, hasCanceledCurrent
											, buyerName
											, buyerPhoneNumber
											, statusCurrent
											, receiverName
											, receiverPhoneNumber
											, address
											, createdAt
											, updatedAt);
		
		List<AdminInvoiceVO> listVOs = new ArrayList<>();
		for(Invoice invoice: listInvoices) {
			AdminInvoiceVO vo = new AdminInvoiceVO(invoice);
			
			Integer userIdCur = userId;
			//사용자 loginId 찾아서 넣기
			if(userIdCur == null) {
				userIdCur = invoice.getUserId();
			}
			vo.setLoginId(userBO.getUserById(userIdCur).getLoginId());
			listVOs.add(vo);
		}
		
		return listVOs;
	}

}
