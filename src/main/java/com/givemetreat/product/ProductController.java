package com.givemetreat.product;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/product")
@Controller
public class ProductController {

	@GetMapping("/product-list-view")
	public String productListView() {
		return "/product/productList.html";
	}
}
