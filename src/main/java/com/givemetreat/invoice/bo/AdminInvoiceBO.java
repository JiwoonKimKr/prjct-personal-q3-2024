package com.givemetreat.invoice.bo;

import java.util.*;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.givemetreat.invoice.domain.AdminInvoiceVO;
import com.givemetreat.invoice.domain.Invoice;
import com.givemetreat.invoice.domain.InvoiceEntity;
import com.givemetreat.invoice.mapper.InvoiceMapper;
import com.givemetreat.invoice.repository.InvoiceRepository;
import com.givemetreat.product.bo.AdminProductBO;
import com.givemetreat.product.domain.AdminProductVO;
import com.givemetreat.productBuffer.bo.ProductBufferBO;
import com.givemetreat.productBuffer.domain.ProductBufferEntity;
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
	private final ProductBufferBO ProductBufferBO;

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

		//1)product_buffer 갯수 Map구조로 수량 파악
		Map<AdminProductVO, Integer> mapProductVOs = new HashMap<>();
		
		for(ProductInvoiceEntity productInvoice : listProductInvoices) {
			int productInvoiceId = productInvoice.getId();
			int productId = productInvoice.getProductId();

			ProductBufferEntity buffer = ProductBufferBO.getBuffer(productId, true, productInvoiceId);

			if(buffer == null) {
				continue;
			}
			
			AdminProductVO productVO = adminProductBO.getProduct(productId, null, null, null, null).get(0);
			
			if(mapProductVOs.containsKey(productVO)) {
				mapProductVOs.put(productVO
								, mapProductVOs.get(productVO) + 1);
				continue;
			}
			mapProductVOs.put(productVO, 1);
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
