package com.givemetreat.product;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.givemetreat.product.bo.AdminProductBO;
import com.givemetreat.product.domain.Product;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/admin/product")
@Controller
public class AdminProductController {
	private final AdminProductBO adminProductBO;
	
	@GetMapping("/product-register-view")
	public String productRegisterView() {
		return "admin/product/productRegister";
	}
	
	//상품 리스트 조회
	@GetMapping("/product-list-view")
	public String productListView(){
		return "admin/product/productList";
	}
	
	//상품 상세 조회
	@GetMapping("/{idProduct}")
	public String productDetailView(@PathVariable int idProduct
									, Model model) {
		Product productCurrent = adminProductBO.getProduct(idProduct, null, null, null, null).get(0);
		
		model.addAttribute("productCurrent", productCurrent);
		return "admin/product/productDetail";
	}
	
}
