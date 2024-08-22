package com.givemetreat.product;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.givemetreat.product.bo.AdminProductBO;
import com.givemetreat.product.domain.AdminProductVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Admin Product Controller", description = "[Admin] Product Controller; 관리자페이지 상품 관련 컨트롤러")
@RequiredArgsConstructor
@RequestMapping("/admin/product")
@Controller
public class AdminProductController {
	private final AdminProductBO adminProductBO;
	
	@Operation(summary = "productRegisterView() 상품 등록 페이지", description = "새로운 상품 등록 페이지로 이동")
	@ApiResponse(responseCode = "200", description = "/admin/product/productRegister.html", content = @Content(mediaType = "TEXT_HTML"))
	@GetMapping("/product-register-view")
	public String productRegisterView() {
		return "admin/product/productRegister";
	}
	
	//상품 리스트 조회
	@Operation(summary = "productListView() 상품 조회 페이지", description = "상품 조회 페이지로 이동")
	@ApiResponse(responseCode = "200", description = "/admin/product/productList.html", content = @Content(mediaType = "TEXT_HTML"))
	@GetMapping("/product-list-view")
	public String productListView(){
		return "admin/product/productList";
	}
	
	//상품 상세 조회
	@Operation(summary = "productDetailView() 상품 상세조회 페이지", description = "기존 등록한 상품 목록 조회")
	@Parameters({
		@Parameter(name = "[PathVariable] <int> idProduct", description = "해당 상품 PK", example = "5")
		, @Parameter(name = "<Model> model", description = "MVC Model")
	})
	@ApiResponse(responseCode = "200", description = "/admin/product/productDetail.html "
													+ "<br> Model Attributes"
													+" <br> &lt;AdminProductVO&gt; \"productCurrent\""
		, content = @Content(mediaType = "TEXT_HTML", schema = @Schema(implementation = AdminProductVO.class)))
	@GetMapping("/{idProduct}")
	public String productDetailView(@PathVariable int idProduct
									, Model model) {
		AdminProductVO productCurrent = adminProductBO.getProduct(idProduct, null, null, null, null).get(0);
		
		model.addAttribute("productCurrent", productCurrent);
		return "admin/product/productDetail";
	}
	
}
