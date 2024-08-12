package com.givemetreat.invoice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.givemetreat.invoice.bo.InvoiceBO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/invoice")
@RestController
public class InvoiceRestController {
	private final InvoiceBO invoiceBO;
	//결제 요청; ★★★★★나중에 PG사 연동시켜서 확인해봐야!
	
	
	//@PostMapping(value="/payment", produces = "application/json; charset=utf8")
	@PostMapping("/payment-validation")
	public Map<String, Object> payment(@RequestParam String listItemsOrdered
										, HttpSession session){
		Map<String, Object> result = new HashMap<>();

		log.info("[InvoiceRestController payment()]"
		+ " RequestBody jsonString:{}", listItemsOrdered);
		
		Integer userId = (Integer) session.getAttribute("userId");
		
		if(userId == null) {
			log.info("[InvoiceRestController payment()] userId is null now. Sign-In needed.");
			result.put("code", 403);
			result.put("error_message", "로그인 후 이용 가능합니다.");
			return result;			
		}
		
		
		Boolean getPaymentSuccessMessage = invoiceBO.generateInvoiceFromJsonString(listItemsOrdered);
		
		//결제 요청 실패 시
		if(getPaymentSuccessMessage == false) {
			log.info("[InvoiceRestController payment()] current invoice got failed to get paid.");
			result.put("code", 500);
			result.put("error_message", "결제 시도가 실패하였습니다.");
			return result;			
		}
		
		log.info("[InvoiceRestController payment()] current invoice has success to get paid.");
		result.put("code", 200);
		result.put("error_message", "결제 시도가 성공하였습니다.");
		
		return result;
	}
}
