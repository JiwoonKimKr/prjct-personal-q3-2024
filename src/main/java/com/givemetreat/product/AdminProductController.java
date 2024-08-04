package com.givemetreat.product;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin/product")
@Controller
public class AdminProductController {

	@GetMapping("/product-register-view")
	public String productRegisterView() {
		return "admin/product/productRegister";
	}
}
