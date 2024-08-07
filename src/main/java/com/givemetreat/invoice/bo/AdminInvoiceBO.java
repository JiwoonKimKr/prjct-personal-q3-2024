package com.givemetreat.invoice.bo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.givemetreat.invoice.domain.AdminInvoiceVO;
import com.givemetreat.invoice.domain.Invoice;
import com.givemetreat.invoice.domain.InvoiceEntity;
import com.givemetreat.invoice.mapper.InvoiceMapper;
import com.givemetreat.invoice.repository.InvoiceRepository;
import com.givemetreat.product.bo.AdminProductBO;
import com.givemetreat.product.domain.AdminProductVO;
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
		
		List<InvoiceEntity> listInvoiceLatest = invoiceRepository.
						findInvoiceByHasCanceledAndStatusDeliveryOrderByIdDesc(0, "PackingFinished");
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
		
		Map<AdminProductVO, Integer> mapItemsOrdered = new HashMap<>();
		
		for(ProductInvoiceEntity itemOrdered : listProductInvoices) {
			int productId = itemOrdered.getProductId();
			
			AdminProductVO productVO = adminProductBO.getProduct(productId, null, null, null, null).get(0);
			
			//이미 존재하는 동일한 제품인 경우 갯수만 하나만 더 추가시킴
			if(mapItemsOrdered.containsKey(productVO)) {
				mapItemsOrdered.put(productVO, mapItemsOrdered.get(productVO) + 1);
				continue; // 여기서 반복문 안 넘기면 1로 초기화 된다 ㅠㅠ
			}
			
			mapItemsOrdered.put(productVO, 1);
		}
		
		List<AdminProductInvoiceVO> listVOs = new ArrayList<>();
		
		//<Product, Integer>의 Map에서 VO를 가진 List로 변환;
		Set<AdminProductVO> keys = mapItemsOrdered.keySet();
		Iterator<AdminProductVO> iter =keys.iterator();
		while(iter.hasNext()) {
			AdminProductVO itemCur = iter.next();
			
			listVOs.add(new AdminProductInvoiceVO(itemCur, mapItemsOrdered.get(itemCur)));
		}
		
		return listVOs;
	}

	//★★★★★ 필드 하나만 선택해서 조회하도록 코딩해야;
	public List<AdminInvoiceVO> getInvoices(Integer invoiceId
										, Integer userId
										, Integer payment
										, String paymentType
										, String company
										, String monthlyInstallment
										, Integer hasCanceled
										, String buyerName
										, String buyerPhoneNumber
										, String statusDelivery
										, String receiverName
										, String receiverPhoneNumber
										, String address
										, LocalDateTime createdAt
										, LocalDateTime updatedAt) {
		List<Invoice> listInvoices = invoiceMapper.selectInvoices( invoiceId
											, userId
											, payment
											, paymentType
											, company
											, monthlyInstallment
											, hasCanceled
											, buyerName
											, buyerPhoneNumber
											, statusDelivery
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
