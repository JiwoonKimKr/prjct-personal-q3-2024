package com.givemetreat.productShoppingCart;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
public class ShoppingCartRestController {
	private final ProductShoppingCartBO productShoppingCartBO;
	
	
	@PostMapping("/add-product")
	public Map<String, Object> AddProduct(@RequestParam int productId
										, @RequestParam int quantity
										, @RequestParam(required = false) Integer cartItemId
										, HttpSession session){
		Map<String, Object> result = new HashMap<>();
		
		Integer userId = (Integer) session.getAttribute("userId");
		
		if(userId == null) {
			log.info("[UserShoppingCartRestController addProduct()] userId is null now. Sign-In needed.");
			result.put("code", 403);
			result.put("error_message", "로그인 후 이용 가능합니다.");
			return result;			
		}
		
		ProductShoppingCartEntity itemEnlisted = productShoppingCartBO.updateQuantity(userId
															, productId
															, quantity
															, cartItemId);
		
		if(itemEnlisted == null) {
			log.info("[UserShoppingCartRestController addProduct()] current item got failed to get enlisted in cart.");
			result.put("code", 500);
			result.put("error_message", "해당 상품과 수량이 장바구니에 담기지 못하였습니다.");
			return result;
		}
		
		
		log.info("[UserShoppingCartRestController addProduct()] success to enlist current item in cart.");
		result.put("code", 200);
		result.put("success", "해당 상품이 장바구니에 담겼습니다.");
		result.put("quantity", itemEnlisted.getQuantity());
		
		return result;
	}

	@GetMapping("/substract-product")
	public Map<String, Object> substractProduct(@RequestParam int productId
									, @RequestParam int quantity
									, @RequestParam(required = false) Integer cartItemId
									, HttpSession session){
		Map<String, Object> result = new HashMap<>();
		
		Integer userId = (Integer) session.getAttribute("userId");
		
		if(userId == null) {
		log.info("[UserShoppingCartRestController substractProduct()] userId is null now. Sign-In needed.");
		result.put("code", 403);
		result.put("error_message", "로그인 후 이용 가능합니다.");
		return result;			
		}
		
		ProductShoppingCartEntity itemEnlisted = productShoppingCartBO.updateQuantity(userId
										, productId
										, quantity
										, cartItemId);
		
		if(itemEnlisted == null) {
		log.info("[UserShoppingCartRestController substractProduct()] current item got failed to get enlisted in cart.");
		result.put("code", 500);
		result.put("error_message", "해당 상품과 수량을 장바구니에서 수정하지 못하였습니다..");
		return result;
		}
		
		log.info("[UserShoppingCartRestController substractProduct()] success to enlist current item in cart.");
		result.put("code", 200);
		result.put("success", "해당 상품 및 수량을 장바구니에서 수정하였습니다.");
		result.put("quantity", itemEnlisted.getQuantity());
		
		return result;
	}
	
	@PostMapping("/delete-product")
	public Map<String, Object> deleteProduct(@RequestParam int productId
												, @RequestParam Integer cartItemId
												, HttpSession session){
		Map<String, Object> result = new HashMap<>();
		
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null) {
		log.info("[UserShoppingCartRestController deleteProduct()] userId is null now. Sign-In needed.");
		result.put("code", 403);
		result.put("error_message", "로그인 후 이용 가능합니다.");
		return result;			
		}
		
		ProductShoppingCartEntity itemEnlisted = productShoppingCartBO.updateQuantity(userId
										, productId
										, 0
										, cartItemId);
		
		if(itemEnlisted == null || itemEnlisted.getQuantity() != 0) {
		log.info("[UserShoppingCartRestController deleteProduct()] current item got failed to get deleted in cart.");
		result.put("code", 500);
		result.put("error_message", "해당 상품과 수량을 장바구니에서 삭제하지 못하였습니다.");
		return result;
		}
		
		log.info("[UserShoppingCartRestController deleteProduct()] success to delete current item in cart.");
		result.put("code", 200);
		result.put("success", "해당 상품 및 수량을 장바구니에서 삭제하였습니다.");
		result.put("quantity", itemEnlisted.getQuantity());
		
		return result;		
	}
	
	@DeleteMapping("/empty-cart")
	public Map<String, Object> emptyCart(HttpSession session){
		Map<String, Object> result = new HashMap<>();
		
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null) {
			log.info("[UserShoppingCartRestController emptyCart()] userId is null now. Sign-In needed.");
			result.put("code", 403);
			result.put("error_message", "로그인 후 이용 가능합니다.");
			return result;			
		}
		
		Boolean hasDeleted = productShoppingCartBO.emptyCart(userId);
		
		if(hasDeleted == false) {
			log.info("[UserShoppingCartRestController emptyCart()] cart is empty.");
			result.put("code", 500);
			result.put("error_message", "장바구니가 비어있는 상태입니다.");
			return result;
		}
		
		log.info("[UserShoppingCartRestController emptyCart()] success to delete current item in cart.");
		result.put("code", 200);
		result.put("success", "장바구니를 비웠습니다.");
		
		return result;		
	}	
}
