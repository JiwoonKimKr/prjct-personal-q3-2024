package com.givemetreat.invoice;

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
/*
	20:00_11 08 2024
	결제하기 버튼이 눌렸을 때 productId 와 quantity 등의 정보를 invoiceBO로 넘겨야
	invoiceBO에서는 productInvoiceBO 통해서 접근해야,
		productInvoiceBO에서 productBufferBO로 접근;
		결제 페이지로 넘어온 수량(quantity)는 Reserved 표시해야; 결제 취소하면 원상 복귀
		결제 페이지로 넘어온 수량이 현재 재고보다 적거나 같은지 체크해야;
*/
	
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
	
	@GetMapping("/payment-specific-item-view")
	public String paymentSpecificItemView(@RequestParam int productId
										, @RequestParam int quantity
										, HttpSession session
										, Model model) {
		//상품 상세 페이지에서 바로 단일 품목만 결제 페이지로 넘어온 경우
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null) {
			return "redirect:user/sign-in-view";
		}
		List<ProductShoppingCartVO> listItems =
				invoiceBO.getProductByProductIdAndQuantity(productId, quantity);
		
		model.addAttribute("listItems", listItems);
		
		return "invoice/payment";
	}
	
	@GetMapping("/delivery-status-view")
	public String deliveryStatusView(HttpSession session
									, Model model) {
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null) {
			return "redirect:user/sign-in-view";
		}
		
		List<InvoiceVO> listInvoiceVOs = invoiceBO.getListInvoicesById(userId);		
		
		model.addAttribute("listInvoices", listInvoiceVOs);
		
		return "invoice/deliveryStatus";
	}
	
	@GetMapping("/delivery-status-detail/{invoiceId}")
	public String invoiceSpecificDeliveryStatus(@PathVariable Integer invoiceId
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
}
