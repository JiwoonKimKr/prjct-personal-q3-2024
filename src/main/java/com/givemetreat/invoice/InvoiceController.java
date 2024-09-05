package com.givemetreat.invoice;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.givemetreat.invoice.bo.InvoiceBO;
import com.givemetreat.invoice.domain.InvoiceVO;
import com.givemetreat.productInvoice.domain.ItemOrderedVO;
import com.givemetreat.productShoppingCart.domain.ProductShoppingCartVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Tag(name = "Invoice Controller", description = "[Client] Invoice Controller 주문 페이지 관련 컨트롤러")
@RequestMapping("/invoice")
@RequiredArgsConstructor
@Controller
public class InvoiceController {
	private final InvoiceBO invoiceBO;
	
	//장바구니에서 넘어온 productShoppingCart 목록들을 뿌려야
	@Operation(summary = "paymentView() 장바구니에서 결제 페이지로 이동", description = "장바구니 페이지에서 결제 페이지로 이동")
	@Parameters({
		@Parameter(name = "<HttpSession> session", description = "session")
		, @Parameter(name = "<Model> model", description = "MVC Model")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "403", description = "redirect:user/sign-in-view")
		, @ApiResponse(responseCode = "200", description = "/invoice/payment.html"
																	+"<br> Model Attributtes"
																	+"<br>, List&lt;ProductShoppingCartVO&gt; \"listItems\""
		, content = @Content(mediaType = "TEXT_HTML", schema = @Schema(implementation = ProductShoppingCartVO.class)))
	})
	@GetMapping("/payment-view")
	public String paymentView(HttpSession session
							, Model model) {
		//장바구니 페이지에서 넘어온 경우
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null) {
			return "redirect:/user/sign-in-view";
		}
		List<ProductShoppingCartVO> listItems = invoiceBO.getListProductsFromCartByUserId(userId);
		
		model.addAttribute("listItems", listItems);
		
		return "invoice/payment";
	}
	
	//상품 상세 페이지에서 바로 단일 품목만 결제 페이지로 넘어온 경우
	@Operation(summary = "paymentSpecificItemView() 단일 품목에서 바로 결제 페이지로 이동", description = "단일 품목에서 바로 결제 페이지로 이동")
	@Parameters({
		@Parameter(name = "<int> productId", description = "상품(product) PK", example = "10")
		, @Parameter(name = "<int> quantity", description = "해당 상품 수량", example = "5")
		, @Parameter(name = "<HttpSession> session", description = "session")
		, @Parameter(name = "<Model> model", description = "MVC Model")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "403", description = "redirect:user/sign-in-view")
		, @ApiResponse(responseCode = "200", description = "/invoice/payment.html"
																	+"<br> Model Attributtes"
																	+"<br>, List&lt;ProductShoppingCartVO&gt; \"listItems\""
		, content = @Content(mediaType = "TEXT_HTML", schema = @Schema(implementation = ProductShoppingCartVO.class)))
	})
	@GetMapping("/payment-specific-item-view")
	public String paymentSpecificItemView(@RequestParam int productId
										, @RequestParam int quantity
										, HttpSession session
										, Model model) {
		
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null) {
			return "redirect:/user/sign-in-view";
		}
		List<ProductShoppingCartVO> listItems =
				invoiceBO.getProductByProductIdAndQuantity(productId, quantity);
		
		model.addAttribute("listItems", listItems);
		
		return "invoice/payment";
	}
	
	//배송 조회 목록
	@Operation(summary = "deliveryStatusView() 배송 목록 조회 페이지", description = "배송 목록 조회 페이지")
	@Parameters({
		@Parameter(name = "<HttpSession> session", description = "session")
		, @Parameter(name = "<Model> model", description = "MVC Model")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "403", description = "redirect:user/sign-in-view")
		, @ApiResponse(responseCode = "200", description = "/invoice/deliveryStatus.html"
																	+"<br> Model Attributtes"
																	+"<br>, List&lt;InvoiceVO&gt; \"listInvoiceVOs\""
		, content = @Content(mediaType = "TEXT_HTML", schema = @Schema(implementation = InvoiceVO.class)))
	})
	@GetMapping("/delivery-status-view")
	public String deliveryStatusView(HttpSession session
									, Model model) {
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null) {
			return "redirect:/user/sign-in-view";
		}
		
		List<InvoiceVO> listInvoiceVOs = invoiceBO.getListInvoicesByIdDeliveryNotFinished(userId);		
		
		model.addAttribute("listInvoices", listInvoiceVOs);
		
		return "invoice/deliveryStatus";
	}
	
	//배송 조회 상세 조회
	@Operation(summary = "invoiceDeliveryStatusDetailView() 배송 상세조회 페이지", description = "배송 상세조회 페이지; 배송 목록 페이지에서 이동")
	@Parameters({
		@Parameter(name = "<Integer> invoiceId", description = "[PathVariable] 주문(인보이스) PK", example = "10")
		, @Parameter(name = "<HttpSession> session", description = "session")
		, @Parameter(name = "<Model> model", description = "MVC Model")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "403", description = "redirect:user/sign-in-view")
		, @ApiResponse(responseCode = "200", description = "/invoice/deliveryStatusDetail.html"
																	+"<br> Model Attributtes"
																	+"<br>, &lt;InvoiceVO&gt; \"invoice\""
																	+"<br>, List&lt;ItemOrderedVO&gt; \"listInvoiceVOs\""
		, content = @Content(mediaType = "TEXT_HTML", schema = @Schema(allOf = {InvoiceVO.class, ItemOrderedVO.class})))
	})
	@GetMapping("/delivery-status-detail/{invoiceId}")
	public String invoiceDeliveryStatusDetailView(@PathVariable Integer invoiceId
												, HttpSession session
												, Model model) {
		
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null) {
			return "redirect:/user/sign-in-view";
		}
		
		InvoiceVO invoice = invoiceBO.getInvoiceById(invoiceId);
		List<ItemOrderedVO> listItems = invoiceBO.getItemsOrderedByUserIdAndInvoiceId(userId, invoiceId);
		
		model.addAttribute("invoice", invoice);
		model.addAttribute("listItems", listItems);
		return "invoice/deliveryStatusDetail";
	}
	
	//주문 내역 조회
	@Operation(summary = "invoiceListView() 주문(인보이스) 목록 조회 페이지", description = "주문(인보이스) 목록 조회 페이지")
	@Parameters({
		@Parameter(name = "<String> statusDelivery", description = "배송 상태 (enum type)", example = "PackingFinished")
		, @Parameter(name = "<LocalDateTime> timeSince", description = "특정시간 부터(시작 구간)", example = "2024-08-01T06:30:45")
		, @Parameter(name = "<LocalDateTime> timeUntil", description = "특정시간 까지(종료 구간)", example = "2024-08-31T18:00:00")
		, @Parameter(name = "<Model> model", description = "MVC Model")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "403", description = "redirect:user/sign-in-view")
		, @ApiResponse(responseCode = "200", description = "/invoice/deliveryStatusDetail.html"
																	+"<br> Model Attributtes"
																	+"<br>, List&lt;InvoiceVO&gt; \"listInvoices\""
		, content = @Content(mediaType = "TEXT_HTML", schema = @Schema(implementation = InvoiceVO.class)))
	})
	@GetMapping("/invoice-list-view")
	public String invoiceListView(HttpSession session
								, @RequestParam(required = false) String statusDelivery
								, @RequestParam(required = false) LocalDateTime	timeSince
								, @RequestParam(required = false) LocalDateTime	timeUntil			
								, Model model) {
		
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null) {
			return "redirect:user/sign-in-view";
		}
		
		List<InvoiceVO> listInvoiceVOs = invoiceBO.getInvoices(null
															, userId
															, null
															, null
															, null
															, null
															, statusDelivery
															, null
															, null
															, null
															, null, null
															, timeSince, timeUntil);		
		
		model.addAttribute("listInvoices", listInvoiceVOs);
		
		return "invoice/invoiceList";
	}
	
	//주문 내역 상세 조회
	@Operation(summary = "invoiceSpecificDetailView() 주문(invoice) 상세조회 페이지", description = "주문 상세조회 페이지; 주문(invoice) 목록 페이지에서 이동")
	@Parameters({
		@Parameter(name = "<Integer> invoiceId", description = "[PathVariable] 주문(인보이스) PK", example = "10")
		, @Parameter(name = "<HttpSession> session", description = "session")
		, @Parameter(name = "<Model> model", description = "MVC Model")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "403", description = "redirect:user/sign-in-view")
		, @ApiResponse(responseCode = "200", description = "/invoice/invoiceDetail.html"
																	+"<br> Model Attributtes"
																	+"<br>, &lt;InvoiceVO&gt; \"invoice\""
																	+"<br>, List&lt;ItemOrderedVO&gt; \"listInvoiceVOs\""
		, content = @Content(mediaType = "TEXT_HTML", schema = @Schema(allOf = {InvoiceVO.class, ItemOrderedVO.class})))
	})
	@GetMapping("/invoice-detail/{invoiceId}")
	public String invoiceSpecificDetailView(@PathVariable Integer invoiceId
											, HttpSession session
											, Model model) {
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null) {
			return "redirect:/user/sign-in-view";
		}
		
		InvoiceVO invoice = invoiceBO.getInvoiceById(invoiceId);
		List<ItemOrderedVO> listItems = invoiceBO.getItemsOrderedByUserIdAndInvoiceId(userId, invoiceId);
		
		model.addAttribute("invoice", invoice);
		model.addAttribute("listItems", listItems);
		return "invoice/invoiceDetail";
	}
	
}
