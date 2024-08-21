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

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequestMapping("/invoice")
@RequiredArgsConstructor
@Controller
public class InvoiceController {
	private final InvoiceBO invoiceBO;
	
	//장바구니에서 넘어온 productShoppingCart 목록들을 뿌려야
	@GetMapping("/payment-view")
	public String paymentView(HttpSession session
							, Model model) {
		//장바구니 페이지에서 넘어온 경우
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null) {
			return "redirect:user/sign-in-view";
		}
		List<ProductShoppingCartVO> listItems = invoiceBO.getListProductsFromCartByUserId(userId);
		
		model.addAttribute("listItems", listItems);
		
		return "invoice/payment";
	}
	
	//상품 상세 페이지에서 바로 단일 품목만 결제 페이지로 넘어온 경우
	@GetMapping("/payment-specific-item-view")
	public String paymentSpecificItemView(@RequestParam int productId
										, @RequestParam int quantity
										, HttpSession session
										, Model model) {
		
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null) {
			return "redirect:user/sign-in-view";
		}
		List<ProductShoppingCartVO> listItems =
				invoiceBO.getProductByProductIdAndQuantity(productId, quantity);
		
		model.addAttribute("listItems", listItems);
		
		return "invoice/payment";
	}
	
	//배송 조회 목록
	@GetMapping("/delivery-status-view")
	public String deliveryStatusView(HttpSession session
									, Model model) {
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null) {
			return "redirect:user/sign-in-view";
		}
		
		List<InvoiceVO> listInvoiceVOs = invoiceBO.getListInvoicesByIdDeliveryNotFinished(userId);		
		
		model.addAttribute("listInvoices", listInvoiceVOs);
		
		return "invoice/deliveryStatus";
	}
	
	//배송 조회 상세 조회
	@GetMapping("/delivery-status-detail/{invoiceId}")
	public String invoiceDeliveryStatusDetailView(@PathVariable Integer invoiceId
												, HttpSession session
												, Model model) {
		
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null) {
			return "redirect:user/sign-in-view";
		}
		
		InvoiceVO invoice = invoiceBO.getInvoiceById(invoiceId);
		List<ItemOrderedVO> listItems = invoiceBO.getItemsOrderedByUserIdAndInvoiceId(userId, invoiceId);
		
		model.addAttribute("invoice", invoice);
		model.addAttribute("listItems", listItems);
		return "invoice/deliveryStatusDetail";
	}
	
	//주문 내역 조회
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
	@GetMapping("/invoice-detail/{invoiceId}")
	public String invoiceSpecificDetailView(@PathVariable Integer invoiceId
											, HttpSession session
											, Model model) {
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null) {
			return "redirect:user/sign-in-view";
		}
		
		InvoiceVO invoice = invoiceBO.getInvoiceById(invoiceId);
		List<ItemOrderedVO> listItems = invoiceBO.getItemsOrderedByUserIdAndInvoiceId(userId, invoiceId);
		
		model.addAttribute("invoice", invoice);
		model.addAttribute("listItems", listItems);
		return "invoice/invoiceDetail";
	}
	
}
