package com.givemetreat.productShoppingCart;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.givemetreat.productShoppingCart.bo.ProductShoppingCartBO;
import com.givemetreat.productShoppingCart.domain.ProductShoppingCartVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequestMapping("/shopping-cart")
@RequiredArgsConstructor
@Controller
public class ShoppingCartController {
	private final ProductShoppingCartBO productShoppingCartBO;

	@GetMapping("/shopping-cart-view")
	public String userShoppingCartView(HttpSession session
									, Model model) {
		
		int userId = (int) session.getAttribute("userId");
		
		List<ProductShoppingCartVO> listItems = productShoppingCartBO.getProductsByUserId(userId);
		model.addAttribute("listItems", listItems);
		
		return "user/shoppingCart";
	}
}
