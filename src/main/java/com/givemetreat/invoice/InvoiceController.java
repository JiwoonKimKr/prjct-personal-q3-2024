package com.givemetreat.invoice;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.givemetreat.invoice.bo.InvoiceBO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequestMapping("/invoice")
@RequiredArgsConstructor
@Controller
public class InvoiceController {
	private final InvoiceBO invoiceBO;
	
	@GetMapping("/payment-view")
	public String paymentView(HttpSession session
							, Model model) {
		////★★★★★ 상품 상세 페이지에서 바로 단일 품목만 띄어야 하는 경우도 고려해야;
		
		//장바구니 페이지에서 넘어온 경우
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null) {
			return "redirect:user/sign-in-view";
		}
		
		//장바구니에서 넘어온 productShoppingCart 목록들 중 productId 와 quantity 등의 정보를 invoiceBO로 넘겨야
			//invoiceBO에서는 productInvoiceBO 통해서 접근해야,
				//productInvoiceBO에서 productBufferBO로 접근;
				//결제 페이지로 넘어온 수량(quantity)는 Reserved 표시해야; 결제 취소하면 원상 복귀
				//결제 페이지로 넘어온 수량이 현재 재고보다 적거나 같은지 체크해야;
		
		return "invoice/payment";
	}
}
