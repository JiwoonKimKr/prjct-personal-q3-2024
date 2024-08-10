package com.givemetreat.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.givemetreat.productShoppingCart.bo.ProductShoppingCartBO;
import com.givemetreat.productShoppingCart.domain.ProductShoppingCartEntity;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/shopping-cart")
@RequiredArgsConstructor
@RestController
public class UserShoppingCartRestController {
	private final ProductShoppingCartBO productShoppingCartBO;
	
	
	@PostMapping("/add-product")
	public Map<String, Object> addProduct(@RequestParam int productId
										, @RequestParam int quantity
										, HttpSession session){
		Map<String, Object> result = new HashMap<>();
		
		Integer userId = (Integer) session.getAttribute("userId");
		
		if(userId == null) {
			log.info("[UserShoppingCartRestController addProduct()] userId is null now. Sign-In needed.");
			result.put("code", 403);
			result.put("error_message", "로그인 후 이용 가능합니다.");
			return result;			
		}
		
		ProductShoppingCartEntity itemEnlisted = productShoppingCartBO.addProductsByProductIdAndQuantity(userId
															, productId
															, quantity);
		
		if(itemEnlisted == null) {
			log.info("[UserShoppingCartRestController addProduct()] current item got failed to get enlisted in cart.");
			result.put("code", 500);
			result.put("error_message", "해당 상품과 수량이 장바구니에 담기지 못하였습니다.");
			return result;
		}
		
		
		log.info("[UserShoppingCartRestController addProduct()] success to enlist current item in cart.");
		result.put("code", 200);
		result.put("success", "해당 상품이 장바구니에 담겼습니다.");
		
		return result;
	}
}
