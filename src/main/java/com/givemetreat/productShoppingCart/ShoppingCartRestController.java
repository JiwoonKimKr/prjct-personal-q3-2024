package com.givemetreat.productShoppingCart;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.givemetreat.productBuffer.bo.ProductBufferBO;
import com.givemetreat.productShoppingCart.bo.ProductShoppingCartBO;
import com.givemetreat.productShoppingCart.domain.ProductShoppingCartEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "ShoppingCart RestController", description = "[Client] ShoppingCart RestController; 장바구니 RestAPI 컨트롤러")
@Slf4j
@RequestMapping("/shopping-cart")
@RequiredArgsConstructor
@RestController
public class ShoppingCartRestController {
	private final ProductShoppingCartBO productShoppingCartBO;
	private final ProductBufferBO productBufferBO;
	
	@Operation(summary = "updateProductQuantity() 장바구니 해당 상품 수량 업데이트", description = "사용자가 요청한 장바구니 수량에 맞게 업데이트")
	@Parameters({
		@Parameter(name = "<int> productId", description = "해당 상품 PK", example = "1")
		, @Parameter(name = "<int> quantity", description = "물품 수량")
		, @Parameter(name = "<Integer> cartItemId", description = "(비필수) 장바구니ID(ProductShoppingCartEntity의 PK)")
		, @Parameter(name = "<HttpSession> session", description = "session")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "403",  description = "error_message: \"로그인 후 이용 가능합니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500__1", description = "error_message: \"해당 상품의 판매 가능 수량이 요청 수량보다 부족합니다.\""
													+ "<br>, \"shortOfQuantity\" &lt;String&gt; 'true'"
													+ "<br>, \"quantity\"=&lt;Integer&gt; 해당 물품 가용 재고"
						, content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500__2", description = "error_message: \"해당 상품과 수량이 장바구니에 담기지 못하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200__1", description = "quantity: &lt;Integer&gt; 0", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200__2", description = "result: \"해당 상품이 장바구니에 담겼습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
	})
	@PostMapping("/update-quantity")
	public Map<String, Object> updateProductQuantity(@RequestParam int productId
										, @RequestParam int quantity
										, @RequestParam(required = false) Integer cartItemId
										, HttpSession session){
		Map<String, Object> result = new HashMap<>();
		
		Integer userId = (Integer) session.getAttribute("userId");
		
		if(userId == null) {
			log.info("[UserShoppingCartRestController updateProductQuantity()] userId is null now. Sign-In needed.");
			result.put("code", 403);
			result.put("error_message", "로그인 후 이용 가능합니다.");
			return result;			
		}
		
		//해당 재품의 재고가 요구 수량보다 적을 때, 또는 가용 재고가 0일 때
		Integer quantityAvailable = productBufferBO.getCountAvailableByProductIdAndReserved(productId, false);
		
		if(ObjectUtils.isEmpty(quantityAvailable) == false && (quantity > quantityAvailable || quantityAvailable == 0)) {
			log.warn("[UserShoppingCartRestController updateProductQuantity()] Quantity ordered is greater than stock available now."
					+ " productId:{}, quantityOrdered:{}", productId, quantity);
			
			//해당 장바구니 record 가용 최대 수량으로 변경
			productShoppingCartBO.updateQuantity(userId, productId, quantityAvailable, cartItemId);
			
			result.put("code", 500);
			result.put("shortOfQuantity", "true");
			result.put("quantity", quantityAvailable);
			result.put("error_message", "해당 상품의 판매 가능 수량이 요청 수량보다 부족합니다.");
			return result;
		}
		
		//update 수량이 0이어서 해당 record를 지워야 하는 경우
		if(quantity == 0) {
			log.warn("[UserShoppingCartRestController updateProductQuantity()] requested quantity is zero, so record will be deleted."
					+ " productId:{}, quantityOrdered:{}", productId, quantity);
			
			productShoppingCartBO.updateQuantity(userId, productId, 0, cartItemId);
			
			result.put("code", 200);
			result.put("quantity", 0);
			return result;
		}
		
		
		ProductShoppingCartEntity itemEnlisted = productShoppingCartBO.updateQuantity(userId
															, productId
															, quantity
															, cartItemId);
		
		if(itemEnlisted == null) {
			log.info("[UserShoppingCartRestController updateProductQuantity()] current item got failed to get enlisted in cart.");
			result.put("code", 500);
			result.put("error_message", "해당 상품과 수량이 장바구니에 담기지 못하였습니다.");
			return result;
		}
		
		
		log.info("[UserShoppingCartRestController updateProductQuantity()] success to enlist current item in cart.");
		result.put("code", 200);
		result.put("success", "해당 상품이 장바구니에 담겼습니다.");
		result.put("quantity", itemEnlisted.getQuantity());
		
		return result;
	}
	
	@Operation(summary = "deleteProduct() 해당 상품을 장바구니 목록에서 삭제", description = "해당 제품을 장바구니 목록에서 삭제")
	@Parameters({
		@Parameter(name = "<int> productId", description = "해당 상품 PK", example = "1")
		, @Parameter(name = "<Integer> cartItemId", description = "(비필수) 장바구니ID(ProductShoppingCartEntity의 PK)")
		, @Parameter(name = "<HttpSession> session", description = "session")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "403", description = "error_message: \"로그인 후 이용 가능합니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500", description = "error_message: \"해당 상품과 수량을 장바구니에서 삭제하지 못하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"해당 상품 및 수량을 장바구니에서 삭제하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
	})
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
	
	@Operation(summary = "emptyCart() 장바구니 비우기", description = "사용자의 장바구니를 통채로 비운다")
	@Parameters({
		@Parameter(name = "<HttpSession> session", description = "session")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "403", description = "error_message: \"로그인 후 이용 가능합니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500", description = "error_message: \"장바구니가 비어있는 상태입니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"장바구니를 비웠습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
	})
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
