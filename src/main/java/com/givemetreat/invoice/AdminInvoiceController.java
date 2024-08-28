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
import com.givemetreat.invoice.domain.InvoiceVO;
import com.givemetreat.productInvoice.domain.AdminProductInvoiceVO;
import com.givemetreat.productInvoice.domain.ItemOrderedVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Tag(name = "Admin Invoice Controller", description = "[Admin] Invoice Controller; 관리자 페이지 주문(invoice) 조회 페이지 컨트롤러")
@Slf4j
@RequestMapping("/admin/invoice")
@RequiredArgsConstructor
@Controller
public class AdminInvoiceController {
	private final AdminInvoiceBO adminInvoiceBO;
	
	//최근 결제완료 주문내역 리스트 조회 페이지
	@Operation(summary = "invoicesLatestView() 최근 결제완료 주문내역 조회 페이지", description = "최근 결제완료 주문내역 조회 페이지; 내림차순")
	@Parameters({
		@Parameter(name = "<Model> model", description = "MVC Model")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "/admin/invoice/invoiceLatest.html"
																	+"<br> Model Attributtes"
																	+"<br>, List&lt;AdminInvoiceVO&gt; \"ListInvoicesLatest\""
		, content = @Content(mediaType = "TEXT_HTML", schema = @Schema(implementation = AdminInvoiceVO.class)))
	})
	@GetMapping("/invoices-latest-view")
	public String invoicesLatestView(Model model) {
		
		//최근 주문 목록 중 배송 준비 필요한 목록들 뿌려야; 택배 상차 완료 X & 주문 취소 X 
		List<AdminInvoiceVO> ListInvoicesLatest = adminInvoiceBO.getListInvoicesPayedRecently();
		
		model.addAttribute("ListInvoicesLatest", ListInvoicesLatest);
		
		return "admin/invoice/invoiceLatest";
	}
	
	//최근 결제완료 주문내역 조회 페이지에서 특정 항목 클릭 후 넘어온, 주문 상세 정보 조회 페이지
	@Operation(summary = "invoicelatestDetailView() 최근 결제완료 주문 상세조회 페이지", description = "최근 결제완료 주문내역 조회 페이지; 최근 결제완료 주문 목록 페이지에서 넘어옴")
	@Parameters({
		@Parameter(name = "<int> invoiceId", description = "주문(인보이스) PK", example = "10")
		, @Parameter(name = "<int> userId", description = "사용자 PK", example = "5")
		, @Parameter(name = "<Model> model", description = "MVC Model")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "/admin/invoice/invoiceLatestDetail.html"
																	+"<br> Model Attributtes"
																	+"<br>, &lt;invoice&gt; \"invoice\""
																	+"<br>, List&lt;AdminProductInvoiceVO&gt; \"listProducts\""
		, content = @Content(mediaType = "TEXT_HTML", schema = @Schema(allOf = {InvoiceVO.class, ItemOrderedVO.class})))
	})
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
	@Operation(summary = "invoiceEntireView() 주문(invoice) 전체조회 페이지", description = "주문 전체 조회 페이지; 필터링 중복 가능;")
	@Parameters({
		@Parameter(name = "<Integer> invoiceId", description = "주문(invoice) pk", example = "10")
		, @Parameter(name = "<Integer> userId", description = "사용자 PK", example = "5")
		, @Parameter(name = "<Integer> paymentFrom", description = "결제 금액, 해당 금액 이상; 원 단위", example = "25000")
		, @Parameter(name = "<Integer> paymentUntil", description = "결제 금액, 해당 금액 이하; 원 단위", example = "50000")
		, @Parameter(name = "<Integer> hasCanceled", description = "결제 취소 여부 (enum type: true(1=결제취소), false(0=결제완료))", example = "0")
		, @Parameter(name = "<String> buyerName", description = "구매자 성명", example = "유재석")
		, @Parameter(name = "<String> buyerPhoneNumber", description = "구매자 연락처(휴대폰)", example = "01012345678")
		, @Parameter(name = "<String> receiverName", description = "수령자 성명", example = "조세호")
		, @Parameter(name = "<String> receiverPhoneNumber", description = "수령자 연락처(휴대폰)", example = "01087654321")
		, @Parameter(name = "<String> address", description = "배송 주소", example = "서울시 서초구 강남구 역삼동")
		, @Parameter(name = "<LocalDateTime> createdAtSince", description = "생성 시간 시작 구간")
		, @Parameter(name = "<LocalDateTime> createdAtUntil", description = "생성 시간 종료 구간")
		, @Parameter(name = "<LocalDateTime> updatedAtSince", description = "수정 시간 시작 구간")
		, @Parameter(name = "<LocalDateTime> updatedAtUntil", description = "수정 시간 종료 구간")
		, @Parameter(name = "<String> direction", description = "(direction과 idRequested) 페이지 방향; 'prev'와 'next' 둘 중 하나")
		, @Parameter(name = "<Integer> idRequested", description = "(direction과 idRequested) 요청받은 id(pk)값")
		, @Parameter(name = "<Integer> pageCurrent", description = "(pageCurrent과 pageRequested) 현재 페이지 번호", example = "0")
		, @Parameter(name = "<Integer> pageRequested", description = "(pageCurrent과 pageRequested) 요청받은 페이지 번호", example = "3")
		, @Parameter(name = "<Model> model", description = "MVC Model")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "/product/productList.html"
																	+"<br> Model Attributtes"
																	+"<br>, List&lt;AdminProductVO&gt; listInvoices"
																	+"<br>, &lt;Integer&gt; numberPageCurrent"
																	+"<br>, &lt;Integer&gt; numberPageMax"
																	+"<br>, &lt;Integer&gt; limit"
																	+"<br>, &lt;Integer&gt; idFirst"
																	+"<br>, &lt;Integer&gt; idLast"
		, content = @Content(mediaType = "TEXT_HTML", schema = @Schema(implementation = AdminInvoiceVO.class)))
	})
	@GetMapping("/invoices-entire-view")
	public String invoiceEntireView(@RequestParam(required = false) Integer invoiceId
										, @RequestParam(required = false) Integer userId 
										, @RequestParam(required = false) Integer paymentFrom
										, @RequestParam(required = false) Integer paymentUntil
										/* 포트원 적용으로 삭제_21 08 2024
										, @RequestParam(required = false) String paymentType
										, @RequestParam(required = false) String company
										, @RequestParam(required = false) String monthlyInstallment
										*/
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
																			, paymentFrom
																			, paymentUntil
																			/* 포트원 적용으로 삭제_21 08 2024
																			, paymentType
																			, company
																			, monthlyInstallment
																			*/
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
	@Operation(summary = "invoiceEntireDetailView() 주문(invoice) 전체목록 상세조회 페이지", description = "주문(invoice) 전체목록 상세조회 페이지; 주문(invoice) 전체목록 페이지에서 이동")
	@Parameters({
		@Parameter(name = "<int> invoiceId", description = "주문(인보이스) PK", example = "10")
		, @Parameter(name = "<int> userId", description = "사용자 PK", example = "5")
		, @Parameter(name = "<Model> model", description = "MVC Model")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "/invoice/invoiceEntireDetail.html"
																	+"<br> Model Attributtes"
																	+"<br>, &lt;AdminInvoiceVO&gt; \"invoice\""
																	+"<br>, List&lt;AdminProductInvoiceVO&gt; \"listProducts\""
		, content = @Content(mediaType = "TEXT_HTML", schema = @Schema(allOf = {AdminInvoiceVO.class, AdminProductInvoiceVO.class})))
	})
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
