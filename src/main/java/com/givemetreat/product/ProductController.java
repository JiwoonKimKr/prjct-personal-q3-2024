package com.givemetreat.product;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.givemetreat.common.generic.Page;
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
								, @RequestParam(required = false) Integer idRequested
								, @RequestParam(required = false) Integer pageCurrent
								, @RequestParam(required = false) Integer pageRequested
								, Model model) {
		
		Page<ProductVO> pageInfo = productBO.getProductsForPaging(id
																, name
																, category
																, price
																, agePetProper
																, direction
																, idRequested
																, pageCurrent
																, pageRequested);
		List<ProductVO> listProducts = pageInfo.generateCurrentPageList();
		
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("numberPageCurrent", pageInfo.getNumberPageCurrent());
		model.addAttribute("numberPageMax", pageInfo.getNumberPageMax());
		model.addAttribute("limit", pageInfo.getLimit());
		model.addAttribute("idFirst", pageInfo.getIdFirst());
		model.addAttribute("idLast", pageInfo.getIdLast());
		
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
