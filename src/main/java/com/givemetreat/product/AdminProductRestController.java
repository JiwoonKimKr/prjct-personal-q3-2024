package com.givemetreat.product;

import java.util.*;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.givemetreat.product.bo.ProductBO;
import com.givemetreat.product.domain.Product;

import lombok.RequiredArgsConstructor;

@RequestMapping("/admin/product")
@RequiredArgsConstructor
@RestController
public class AdminProductRestController {
	private final ProductBO productBO;
	
	//상품 상세 조회
	@PostMapping("/product-detail")
	public Map<String, Object> productDetail(
				@RequestParam(required = false) Integer id 
				, @RequestParam(required = false) String name 
				, @RequestParam(required = false) String category
				, @RequestParam(required = false) Integer price
				, @RequestParam(required = false) String agePetProper
				, Model model){
		Map<String, Object> result = new HashMap<>();
		
		List<Product> listProducts = productBO.getProduct(id, name, category, price, agePetProper);
		
		model.addAttribute(listProducts);
		
		result.put("code", 200);
		result.put("result", "success");
		return result;
	}
}
