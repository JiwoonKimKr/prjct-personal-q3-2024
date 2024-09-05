package com.givemetreat.product;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.givemetreat.common.generic.Page;
import com.givemetreat.product.bo.ProductBO;
import com.givemetreat.product.domain.ProductVO;
import com.givemetreat.productUserInterested.bo.ProductUserInterestedBO;
import com.givemetreat.productUserInterested.domain.ProductUserInterestedEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Product Controller", description = "[Client] Product Controller 상품 페이지 관련 컨트롤러")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/product")
@Controller
public class ProductController {
	private final ProductBO productBO;
	private final ProductUserInterestedBO productUserInterestedBO;

	@Operation(summary = "productListView() 상품 조회 페이지;현 메인 페이지", description = "상품 조회 페이지; 현 메인 페이지 <br> 다양한 변수들 활용 가능 <br> 중복 필터 활용 가능")
	@Parameters({
		@Parameter(name = "<String> keyword", description = "header Fragment의 검색 버튼 이벤트로 넘어온 키워드", example = "성견 간식")
		, @Parameter(name = "<Integer> id", description = "상품 pk", example = "10")
		, @Parameter(name = "<String> name", description = "상품명", example = "나도간식줘")
		, @Parameter(name = "<String> category", description = "상품 카데고리", example = "1")
		, @Parameter(name = "<String> price", description = "상품 가격, 원 단위", example = "10000")
		, @Parameter(name = "<String> agePetProper", description = "반려견 적정 섭취 연령", example = "under6months")
		, @Parameter(name = "<String> direction", description = "(direction과 idRequested) 페이지 방향; 'prev'와 'next' 둘 중 하나")
		, @Parameter(name = "<Integer> idRequested", description = "(direction과 idRequested) 요청받은 id(pk)값")
		, @Parameter(name = "<Integer> pageCurrent", description = "(pageCurrent과 pageRequested) 현재 페이지 번호", example = "0")
		, @Parameter(name = "<Integer> pageRequested", description = "(pageCurrent과 pageRequested) 요청받은 페이지 번호", example = "3")
		, @Parameter(name = "<HttpSession> session", description = "session")
		, @Parameter(name = "<RedirectAttributes> redirectAttributtes", description = "리다이렉트 되는 경우 받아오는 변수값")
		, @Parameter(name = "<Model> model", description = "MVC Model")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "302", description = "redirect:/product/product-list-view <br> 해당 검색어가 존재하지 않아 메인페이지로 리다이렉트시킴"
													+ "<br> Model Attributtes"
													+ "<br>, \"error_message\" \"해당 검색 결과가 존재하지 않습니다.\"")
		, @ApiResponse(responseCode = "200", description = "/product/productList.html"
																	+"<br> Model Attributtes"
																	+"<br>, List&lt;ProductVO&gt; listProducts"
																	+"<br>, &lt;Integer&gt; numberPageCurrent"
																	+"<br>, &lt;Integer&gt; numberPageMax"
																	+"<br>, &lt;Integer&gt; limit"
																	+"<br>, &lt;Integer&gt; idFirst"
																	+"<br>, &lt;Integer&gt; idLast"
		, content = @Content(mediaType = "TEXT_HTML", schema = @Schema(implementation = ProductVO.class)))
	})
	@GetMapping("/product-list-view")
	public String productListView(
								@RequestParam(required = false) String keyword 
								, @RequestParam(required = false) Integer id 
								, @RequestParam(required = false) String name 
								, @RequestParam(required = false) String category
								, @RequestParam(required = false) Integer priceFrom
								, @RequestParam(required = false) Integer priceUntil
								, @RequestParam(required = false) String agePetProper
								, @RequestParam(required = false) String direction
								, @RequestParam(required = false) Integer idRequested
								, @RequestParam(required = false) Integer pageCurrent
								, @RequestParam(required = false) Integer pageRequested
								, HttpSession session
								, RedirectAttributes redirectAttributtes
								, Model model) {
		
		Page<ProductVO> pageInfo = productBO.getProductsForPaging(keyword
																, id
																, name
																, category
																, priceFrom
																, priceUntil
																, agePetProper
																, direction
																, idRequested
																, pageCurrent
																, pageRequested);
		
		if(ObjectUtils.isEmpty(pageInfo)) {
			redirectAttributtes.addFlashAttribute("error_message", "해당 검색 결과가 존재하지 않습니다.");
			return "redirect:/product/product-list-view";
		}
		
		List<ProductVO> listProducts = pageInfo.generateCurrentPageList();
		
		if(listProducts.size() < 1) {
			redirectAttributtes.addFlashAttribute("error_message", "해당 검색 결과가 존재하지 않습니다.");
			return "redirect:/product/product-list-view";
		}
		
		
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("numberPageCurrent", pageInfo.getNumberPageCurrent());
		model.addAttribute("numberPageMax", pageInfo.getNumberPageMax());
		model.addAttribute("limit", pageInfo.getLimit());
		model.addAttribute("idFirst", pageInfo.getIdFirst());
		model.addAttribute("idLast", pageInfo.getIdLast());
		
		//로그인한 경우 사용자 개인화 추천시스템으로 상품 4개 추천_25 08 2024
		Integer userId = (Integer) session.getAttribute("userId");
		
		if(ObjectUtils.isEmpty(userId) == false) {
			List<ProductVO> listProductsRecommended = productBO.getListProductsRecommended(userId, category, agePetProper);
			model.addAttribute("listProductsRecommended", listProductsRecommended);
		}
		
		return "product/productList";
	}
	
	@Operation(summary = "productDetailView() 상품 상세 조회 페이지", description = "상품 상세 조회 페이지")
	@Parameters({
		@Parameter(name = "<Integer> productId", description = "[pathVariable] 상품 pk", example = "10")
		, @Parameter(name = "<HttpSession> session", description = "session; 현재 페이지는 비로그인 상태에서도 접근 가능")
		, @Parameter(name = "<Model> model", description = "MVC Model")
	})
	@ApiResponse(responseCode = "200"
				, description = "/product/productDetail.html"
								+ "<br> Model Attributes"
								+ "<br> &lt;ProductVO&gt; \"product\""
				, content = @Content(mediaType = "TEXT_HTML", schema = @Schema(implementation = ProductVO.class)))
	@GetMapping("/product-detail-view/{productId}")
	public String productDetailView(@PathVariable int productId
									, HttpSession session
									, Model model){
		
		
		ProductVO product = productBO.getProducts(productId, null, null, null, null).get(0);
		
		Integer userId = (Integer) session.getAttribute("userId");
		
		//사용자 경험 관련된 DB 테이블에 productId 관련된 정보 추가_24 08 2024
		if(ObjectUtils.isEmpty(userId) == false) {
			ProductUserInterestedEntity recordProductOrdered = 
					productUserInterestedBO.addRecordForProductUserInterested(userId, productId, true, false, null);
			
			if(ObjectUtils.isEmpty(recordProductOrdered)) {
				log.warn("[ProductController productDetailView()]" 
						+ " ProductUserInterestedEntity failed to get saved correctly."
						+ " productId:{}", productId);
			}
		}
		
		model.addAttribute("product", product);
		
		return "product/productDetail";
	}
	
}
