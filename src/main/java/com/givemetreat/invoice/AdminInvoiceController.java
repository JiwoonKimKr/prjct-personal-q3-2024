package com.givemetreat.invoice;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.givemetreat.invoice.bo.AdminInvoiceBO;
import com.givemetreat.invoice.domain.AdminInvoiceVO;
import com.givemetreat.productInvoice.domain.AdminProductInvoiceVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RequestMapping("/admin/invoice")
@RequiredArgsConstructor
@Controller
public class AdminInvoiceController {
	private final AdminInvoiceBO adminInvoiceBO;
	
	@GetMapping("/invoices-latest-view")
	public String invoicesLatestView(Model model) {
		
		//최근 주문 목록 중 배송 준비 필요한 목록들 뿌려야; 택배 상차 완료 X & 주문 취소 X 
		List<AdminInvoiceVO> ListInvoicesLatest = adminInvoiceBO.getListInvoicesPayedRecently();
		
		model.addAttribute("ListInvoicesLatest", ListInvoicesLatest);
		
		return "admin/invoice/invoiceLatest";
	}
	
	@GetMapping("/invoice-latest-detail-view")
	public String invoicelatestDetailView(@RequestParam int invoiceId
										, @RequestParam int userId
										, Model model){
		log.info("[AdminInvoiceController: invoicelatestDetailView() Requested] invoiceId:{}, userId:{}", invoiceId, userId);
		
		List<AdminProductInvoiceVO> listProductVOs = adminInvoiceBO.getProductInvoicesByInvoiceIdAndUserId(invoiceId, userId);
		AdminInvoiceVO invoice = adminInvoiceBO.getInvoiceByInvoiceIdAndUserId(invoiceId, userId);
		
		model.addAttribute("listProducts", listProductVOs);
		model.addAttribute("invoice", invoice);
		
		return "admin/invoice/invoiceLatestDetail";
	}
}
