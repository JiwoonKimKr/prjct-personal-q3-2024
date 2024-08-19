package com.givemetreat.invoice;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.givemetreat.common.generic.Page;
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
		
		AdminInvoiceVO invoice = adminInvoiceBO.getInvoiceByInvoiceIdAndUserId(invoiceId, userId);
		List<AdminProductInvoiceVO> listProductVOs = adminInvoiceBO.getProductInvoicesByInvoiceIdAndUserId(invoiceId, userId);
		
		model.addAttribute("invoice", invoice);
		model.addAttribute("listProducts", listProductVOs);
		
		return "admin/invoice/invoiceLatestDetail";
	}
	
	//전체 주문내역 조회 페이지; JPA가 아닌 MyBatis 방식 차용
	@GetMapping("/invoices-entire-view")
	public String invoiceEntireView(@RequestParam(required = false) Integer invoiceId
										, @RequestParam(required = false) Integer userId 
										, @RequestParam(required = false) Integer payment
										, @RequestParam(required = false) String paymentType
										, @RequestParam(required = false) String company
										, @RequestParam(required = false) String monthlyInstallment
										, @RequestParam(required = false) Integer hasCanceled
										, @RequestParam(required = false) String buyerName
										, @RequestParam(required = false) String buyerPhoneNumber
										, @RequestParam(required = false) String statusDelivery
										, @RequestParam(required = false) String receiverName
										, @RequestParam(required = false) String receiverPhoneNumber
										, @RequestParam(required = false) String address
										, @RequestParam(required = false) LocalDateTime createdAtSince
										, @RequestParam(required = false) LocalDateTime createdAtUntil
										, @RequestParam(required = false) LocalDateTime updatedAtSince
										, @RequestParam(required = false) LocalDateTime updatedAtUntil
										, @RequestParam(required = false) String direction
										, @RequestParam(required = false) Integer idRequested
										, @RequestParam(required = false) Integer pageCurrent
										, @RequestParam(required = false) Integer pageRequested
										, Model model) {
		log.info("[AdminInvoiceController: invoiceEntireListView() Requested]");
		
		Page<AdminInvoiceVO> pageInfo = adminInvoiceBO.getInvoicesForPaging(invoiceId
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
																			, createdAtSince
																			, createdAtUntil
																			, updatedAtSince
																			, updatedAtUntil
																			, direction
																			, idRequested
																			, pageCurrent
																			, pageRequested);
		
		if(ObjectUtils.isEmpty(pageInfo) || ObjectUtils.isEmpty(pageInfo.getListEntities())) {
			log.info("[AdminInvoiceController invoiceEntireView()] current data get null result.");
			model.addAttribute("error_message", "해당 결과를 찾지 못 하였습니다.");	
			return "admin/invoice/invoiceEntire";
		}
		
		List<AdminInvoiceVO> listInvoicesEntire = pageInfo.generateCurrentPageList();
		
		model.addAttribute("listInvoices", listInvoicesEntire);
		model.addAttribute("numberPageCurrent", pageInfo.getNumberPageCurrent());
		model.addAttribute("numberPageMax", pageInfo.getNumberPageMax());
		model.addAttribute("limit", pageInfo.getLimit());
		model.addAttribute("idFirst", pageInfo.getIdFirst());
		model.addAttribute("idLast", pageInfo.getIdLast());			
		return "admin/invoice/invoiceEntire";
	}
	
	// 주문 전체 조회: 상세 내역 페이지
	@GetMapping("/invoice-entire-detail-view")
	public String invoiceEntireDetailView(@RequestParam int invoiceId
										, @RequestParam int userId
										, Model model){
		log.info("[AdminInvoiceController: invoiceEntireDetailView() Requested] invoiceId:{}, userId:{}", invoiceId, userId);
		
		AdminInvoiceVO invoice = adminInvoiceBO.getInvoiceByInvoiceIdAndUserId(invoiceId, userId);
		List<AdminProductInvoiceVO> listProductVOs = adminInvoiceBO.getProductInvoicesByInvoiceIdAndUserId(invoiceId, userId);
		
		model.addAttribute("invoice", invoice);
		model.addAttribute("listProducts", listProductVOs);
		
		return "admin/invoice/invoiceEntireDetail";
	}
	
}
