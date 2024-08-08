package com.givemetreat.product;

import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.givemetreat.product.bo.ProductBO;
import com.givemetreat.product.domain.Product;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/product")
@Controller
public class ProductController {
	private final ProductBO productBO;

	@GetMapping("/product-list-view")
	public String productListView(
								@RequestParam(required = false) Integer id 
								, @RequestParam(required = false) String name 
								, @RequestParam(required = false) String category
								, @RequestParam(required = false) Integer price
								, @RequestParam(required = false) String agePetProper			
								, Model model) {
		List<Product> listProducts = productBO.getProduct(id, name, category, price, agePetProper);

		model.addAttribute("listProducts", listProducts);
		
		return "/product/productList.html";
	}
}
