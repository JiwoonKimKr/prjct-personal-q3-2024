package com.givemetreat.invoice;

import java.time.LocalDateTime;
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
	
	//최근 결제완료 주문내역 리스트 조회 페이지
	@GetMapping("/invoices-latest-view")
	public String invoicesLatestView(Model model) {
		
		//최근 주문 목록 중 배송 준비 필요한 목록들 뿌려야; 택배 상차 완료 X & 주문 취소 X 
		List<AdminInvoiceVO> ListInvoicesLatest = adminInvoiceBO.getListInvoicesPayedRecently();
		
		model.addAttribute("ListInvoicesLatest", ListInvoicesLatest);
		
		return "admin/invoice/invoiceLatest";
	}
	
	//최근 결제완료 주문내역 조회 페이지에서 특정 항목 클릭 후 넘어온, 주문 상세 정보 조회 페이지
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
	
	//전체 주문내역 조회 페이지; JPA가 아닌 MyBatis 방식 차용
	@GetMapping("/invoice-entire-list-view")
	public String invoiceEntireListView(@RequestParam(required = false) Integer invoiceId //아예 입력이 안 되는 null도 고려해서, Integer
										, @RequestParam(required = false) Integer userId //아예 입력이 안 되는 null도 고려해서 Integer
										, @RequestParam(required = false) Integer payment //아예 입력이 안 되는 null도 고려해서 Integer
										, @RequestParam(required = false) String paymentType
										, @RequestParam(required = false) String company
										, @RequestParam(required = false) String monthlyInstallment
										, @RequestParam(required = false) Integer hasCanceled //아예 입력이 안 되는 null도 고려해서 Integer
										, @RequestParam(required = false) String buyerName
										, @RequestParam(required = false) String buyerPhoneNumber
										, @RequestParam(required = false) String statusDelivery
										, @RequestParam(required = false) String receiverName
										, @RequestParam(required = false) String receiverPhoneNumber
										, @RequestParam(required = false) String address
										, @RequestParam(required = false) LocalDateTime createdAt
										, @RequestParam(required = false) LocalDateTime updatedAt
										, Model model) {
		log.info("[AdminInvoiceController: invoiceEntireListView() Requested]");
		
		List<AdminInvoiceVO> listInvoicesEntire = adminInvoiceBO.getInvoices(invoiceId
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
		
		model.addAttribute("listInvoices", listInvoicesEntire);
		return "admin/invoice/invoiceEntireList";
	}
}
