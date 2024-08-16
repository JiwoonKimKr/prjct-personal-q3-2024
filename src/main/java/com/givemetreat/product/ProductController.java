package com.givemetreat.product;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.givemetreat.product.bo.ProductBO;
import com.givemetreat.product.domain.ProductVO;

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
								, @RequestParam(required = false) String direction
								, @RequestParam(required = false) Integer index
								, Model model) {
		
		//페이징 prev 버튼 눌렸을 때, next 버튼 눌렸을 때, 맨 앞 맨 끝인지 파악해야!
		//별도 Paging 관련 클래스 만들어서 접근해야 할 듯!
		
		List<ProductVO> listProducts = productBO.getProductsForPaging(id
																	, name
																	, category
																	, price
																	, agePetProper
																	, direction
																	, index);

		model.addAttribute("listProducts", listProducts);
		
		return "/product/productList";
	}
	
	@GetMapping("/product-detail-view/{productId}")
	public String productDetailView(@PathVariable int productId
									, Model model){
		
		ProductVO product = productBO.getProducts(productId, null, null, null, null).get(0);
		
		model.addAttribute("product", product);
		
		return "/product/productDetail";
	}
	
}
