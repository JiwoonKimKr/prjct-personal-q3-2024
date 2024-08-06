package com.givemetreat.invoice.bo;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.givemetreat.invoice.domain.AdminInvoiceVO;
import com.givemetreat.invoice.domain.InvoiceEntity;
import com.givemetreat.invoice.repository.InvoiceRepository;
import com.givemetreat.product.bo.AdminProductBO;
import com.givemetreat.product.domain.Product;
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
		
		Map<Product, Integer> mapItemsOrdered = new HashMap<>();
		
		for(ProductInvoiceEntity itemOrdered : listProductInvoices) {
			int productId = itemOrdered.getProductId();
			
			Product product = adminProductBO.getProduct(productId, null, null, null, null).get(0);
			
			//이미 존재하는 동일한 제품인 경우 갯수만 하나 더 증가시키도록 코딩해야;
			if(mapItemsOrdered.containsKey(product)) {
				mapItemsOrdered.put(product, mapItemsOrdered.get(product) + 1);
			}
			
			mapItemsOrdered.put(product, 1);
		}
		
		List<AdminProductInvoiceVO> listVOs = new ArrayList<>();
		
		//<Product, Integer>의 Map에서 VO를 가진 List로 변환;
		Set<Product> keys = mapItemsOrdered.keySet();
		Iterator<Product> iter =keys.iterator();
		while(iter.hasNext()) {
			Product itemCur = iter.next();
			
			listVOs.add(new AdminProductInvoiceVO(itemCur, mapItemsOrdered.get(itemCur)));
		}
		
		return listVOs;
	}

}
