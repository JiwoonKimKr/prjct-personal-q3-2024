package com.givemetreat.invoice;

import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.givemetreat.invoice.bo.AdminInvoiceBO;
import com.givemetreat.invoice.domain.AdminInvoiceVO;

import lombok.RequiredArgsConstructor;

@RequestMapping("/admin/invoice")
@RequiredArgsConstructor
@Controller
public class AdminInvoiceController {
	private final AdminInvoiceBO adminInvoiceBO;
	
	@GetMapping("/invoices-latest-view")
	public String invociesPayedRecentlyView(Model model) {
		
		//최근 주문 목록 중 배송 준비 필요한 목록들 뿌려야; 택배 상차 완료 X & 주문 취소 X 
		List<AdminInvoiceVO> ListInvoicesLatest = adminInvoiceBO.getListInvoicesPayedRecently();
		
		model.addAttribute("ListInvoicesLatest", ListInvoicesLatest);
		
		return "admin/invoice/invoiceLatest";
	}
}
