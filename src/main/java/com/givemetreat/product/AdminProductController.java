package com.givemetreat.product;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/admin/product")
@Controller
public class AdminProductController {
	
	@GetMapping("/product-register-view")
	public String productRegisterView() {
		return "admin/product/productRegister";
	}
	//상품 상세 조회
	@GetMapping("/product-detail-view")
	public String productDetailView(){
		return "admin/product/productDetail";
	}	
}
