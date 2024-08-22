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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Product Controller", description = "[Client] Product Controller")
@RequiredArgsConstructor
@RequestMapping("/product")
@Controller
public class ProductController {
	private final ProductBO productBO;

	@Operation(summary = "productListView", description = "상품 조회 페이지; 현 메인 페이지")
	@Parameters({
		@Parameter(name = "<Integer> id", description = "상품 pk", example = "10")
		, @Parameter(name = "<String> name", description = "상품명", example = "나도간식줘")
		, @Parameter(name = "<String> category", description = "상품 카데고리", example = "1")
		, @Parameter(name = "<String> price", description = "상품 가격, 원 단위", example = "10000")
		, @Parameter(name = "<String> agePetProper", description = "반려견 적정 섭취 연령", example = "under6months")
		, @Parameter(name = "<String> direction", description = "(direction과 idRequested) 페이지 방향; 'prev'와 'next' 둘 중 하나")
		, @Parameter(name = "<Integer> idRequested", description = "(direction과 idRequested) 페이지 방향; 'prev'와 'next' 둘 중 하나")
		, @Parameter(name = "<Integer> pageCurrent", description = "(pageCurrent과 pageRequested) 현재 페이지 번호", example = "0")
		, @Parameter(name = "<Integer> pageRequested", description = "(pageCurrent과 pageRequested) 요청받은 페이지 번호", example = "3")
		, @Parameter(name = "<Model> model", description = "MVC Model")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "/product/productList.html"
																	+"<br> Model Attributtes"
																	+"<br>, List&lt;AdminProductVO&gt; listProducts"
																	+"<br>, &lt;Integer&gt; numberPageCurrent"
																	+"<br>, &lt;Integer&gt; numberPageMax"
																	+"<br>, &lt;Integer&gt; limit"
																	+"<br>, &lt;Integer&gt; idFirst"
																	+"<br>, &lt;Integer&gt; idLast"
		, content = @Content(mediaType = "TEXT_HTML", schema = @Schema(implementation = ProductVO.class)))
	})
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
	
	@Operation(summary = "productDetailView", description = "상품 상세 조회 페이지")
	@ApiResponse(responseCode = "200"
				, description = "/product/productDetail.html"
								+ "<br> Model Attributes"
								+ "<br> &lt;ProductVO&gt; \"product\""
				, content = @Content(mediaType = "TEXT_HTML", schema = @Schema(implementation = ProductVO.class)))
	@GetMapping("/product-detail-view/{productId}")
	public String productDetailView(@PathVariable int productId
									, Model model){
		
		ProductVO product = productBO.getProducts(productId, null, null, null, null).get(0);
		
		model.addAttribute("product", product);
		
		return "/product/productDetail";
	}
	
}
