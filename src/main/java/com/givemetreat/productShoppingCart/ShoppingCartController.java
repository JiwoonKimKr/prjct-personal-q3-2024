package com.givemetreat.productShoppingCart;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.givemetreat.productShoppingCart.bo.ProductShoppingCartBO;
import com.givemetreat.productShoppingCart.domain.ProductShoppingCartVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Tag(name = "ShoppingCart Controller", description = "[Client] ShoppingCart Controller; 장바구니 컨트롤러")
@RequestMapping("/shopping-cart")
@RequiredArgsConstructor
@Controller
public class ShoppingCartController {
	private final ProductShoppingCartBO productShoppingCartBO;

	@Operation(summary = "userShoppingCartView", description = "사용자 장바구니 페이지")
	@Parameters({
		@Parameter(name = "<HttpSession> session", description = "session")
		, @Parameter(name = "<Model> model", description = "model")
	})
	@ApiResponse(responseCode = "200"
	, description = "\"shoppingCart/shoppingCart\""
					+ "<br> Model Attributes: "
					+ "<br> List &lt;ProductShoppingCartVO&gt; : listItems"
	, content = @Content(mediaType = "TEXT_HTML", schema = @Schema(implementation = ProductShoppingCartVO.class)))
	@GetMapping("/shopping-cart-view")
	public String userShoppingCartView(HttpSession session
									, Model model) {
		
		int userId = (int) session.getAttribute("userId");
		
		List<ProductShoppingCartVO> listItems = productShoppingCartBO.getProductsByUserId(userId);
		model.addAttribute("listItems", listItems);
		
		return "shoppingCart/shoppingCart";
	}
}
