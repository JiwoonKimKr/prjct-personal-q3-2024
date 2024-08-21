package com.givemetreat.invoice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.givemetreat.invoice.bo.InvoiceBO;
import com.givemetreat.invoice.domain.InvoiceEntity;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/invoice")
@RestController
public class InvoiceRestController {
	private final InvoiceBO invoiceBO;

	//포트원 결제 적용으로 수정_21 08 2024
	@PostMapping("/payment-validation")
	public Map<String, Object> payment(@RequestParam String listItemsOrdered
										, @RequestParam Integer payment
										, @RequestParam String buyerName
										, @RequestParam String buyerPhoneNumber
										, @RequestParam String receiverName
										, @RequestParam String receiverPhoneNumber
										, @RequestParam String address
										, HttpSession session){
		Map<String, Object> result = new HashMap<>();

		log.info("[InvoiceRestController payment()]"
		+ " @RequestParam jsonString:{}"
		+ ", payment:{}"
		+ ", buyerName:{}"
		+ ", buyerPhoneNumber:{}"
		+ ", receiverName:{}"
		+ ", receiverPhoneNumber:{}"
		+ ", address:{}"
		, listItemsOrdered
		, payment, buyerName, buyerPhoneNumber, receiverName, receiverPhoneNumber, address);
		
		Integer userId = (Integer) session.getAttribute("userId");
		
		if(userId == null) {
			log.info("[InvoiceRestController payment()] userId is null now. Sign-In needed.");
			result.put("code", 403);
			result.put("error_message", "로그인 후 이용 가능합니다.");
			return result;			
		}
		
		
		InvoiceEntity invoice = invoiceBO.generateInvoiceFromJsonString(listItemsOrdered
																				, userId
																				, payment
																				, buyerName
																				, buyerPhoneNumber
																				, receiverName
																				, receiverPhoneNumber
																				, address);
		
		//결제 요청 실패 시
		if(ObjectUtils.isEmpty(invoice) == true) {
			log.info("[InvoiceRestController payment()] current invoice got failed to get paid.");
			result.put("code", 500);
			result.put("error_message", "주문 송장(Invoice) 생성 시도가 실패하였습니다.");
			return result;			
		}
		
		log.info("[InvoiceRestController payment()] current invoice has success to get paid.");
		result.put("code", 200);
		result.put("result", "주문 송장(Invoice) 생성 시도가 성공하였습니다.");
		result.put("storeId", "store-2ef6fb64-16b3-406b-9080-f581ef4541f4");
		result.put("channelKey", "channel-key-6855e0fa-5ce5-4e9c-a5c7-ff0aa14e2dd6");
		//★★★★★productId; givemetreat-{invoice.id}-{invoice.createAt} 
		result.put("paymentId", String.format("givemetreat-%s",invoice.getId()));
		result.put("orderName", String.format("orderName-%s-%s", invoice.getId(), invoice.getCreatedAt()));
		result.put("totalAmount", invoice.getPayment());
		result.put("fullName", invoice.getBuyerName());
		result.put("email", session.getAttribute("loginId"));
		result.put("phoneNumber", invoice.getBuyerPhoneNumber());
		result.put("invoiceId", invoice.getId());
		//★★★★★구매자 주소 1; addressLine1 ; 일반주소 
		result.put("addressLine1", "서울특별시 강남구");
		//★★★★★구매자 주소 2; addressLine2 ; 일반주소 		
		result.put("addressLine2", "강남대로94길 13");
		
		return result;
	}
	
	@PostMapping("/delete-invoice")
	public Map<String, Object> deleteInvoice(int invoiceId, HttpSession session) {
		Map<String, Object> result = new HashMap<>();
		
		InvoiceEntity invoice = invoiceBO.getEntityById(invoiceId);
		if(ObjectUtils.isEmpty(invoice)) {
			log.info("[InvoiceRestController deleteInvoice()] invoice failed to be found. invoice Id:{}", invoiceId);
			result.put("code", 500);
			result.put("error_message", "기존 주문이 삭제되지 않았습니다. 관리자에게 문의하세요.");
			return result;
		}
		
		invoiceBO.deleteInvoiceAndResetBuffer(invoice, (int)session.getAttribute("userId"));
				
		log.info("[InvoiceRestController deleteInvoice()] current invoice get Deleted. invoice Id:{}", invoiceId);
		result.put("code", 200);
		result.put("result", "주문 송장(Invoice)이 삭제되었습니다.");
		return result;
	}
}
