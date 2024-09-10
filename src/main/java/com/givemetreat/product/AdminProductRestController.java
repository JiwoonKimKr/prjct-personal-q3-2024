package com.givemetreat.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.givemetreat.common.generic.Page;
import com.givemetreat.common.validation.AdminProductParamsValidation;
import com.givemetreat.product.bo.AdminProductBO;
import com.givemetreat.product.domain.AdminProductVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Admin Product RestController", description = "[Admin] Product RestAPI Controller; 관리자 페이지 상품 관련 RestAPI 컨트롤러")
@Slf4j
@RequestMapping("/admin/product")
@RequiredArgsConstructor
@RestController
public class AdminProductRestController {
	private final AdminProductBO adminProductBO;
	
	@Operation(summary = "registerProduct() 새 상품 등록", description = "관리자페이지 상품 등록")
	@Parameters({
		@Parameter(name = "<String> name", description = "상품명", example = "10")
		, @Parameter(name = "<String> category", description = "상품 카데고리", example = "1")
		, @Parameter(name = "<String> price", description = "상품 가격, 원 단위", example = "10000")
		, @Parameter(name = "<String> agePetProper", description = "반려견 적정 섭취 연령", example = "under6months")
		, @Parameter(name = "<MultipartFile> imageProduct", description = "상품 이미지", example = "treat03_givemetreat.png")
		, @Parameter(name = "<String> quantity", description = "추가될 상품 재고, 유닛 단위", example = "100")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "500__1", description = "error_message: \"해당 상품 등록이 실패하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500__2", description = "error_message: \"특정 변수가 잘못 입력되었습니다.\"" 
															+ "<br> wrong_parameter &lt;String&gt; 잘못 넘어온 변수 명칭" 
						, content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"success\"", content = @Content(mediaType = "APPLICATION_JSON"))
	})
	@PostMapping("/register-product")
	public Map<String, Object> registerProduct(
			@RequestPart String name 
			, @RequestPart String category
			, @RequestPart String price
			, @RequestPart String agePetProper
			, @RequestPart MultipartFile imageProduct
			, @RequestPart String quantity
			){
		Map<String, Object> result = new HashMap<>();

		String wrongParameterRequested = 
				AdminProductParamsValidation.getParamsValidated(name
															, category
															, price
															, agePetProper
															, quantity);
		
		if(wrongParameterRequested != null) {
			result.put("code", 500);
			result.put("error_message", "특정 변수가 잘못 입력되었습니다.");
			result.put("wrong_parameter", wrongParameterRequested);
			log.info("[ADMIN-Product: register-product] fail to register current product:{},{},{},{},{}"
					, name, category, price, agePetProper, imageProduct);
			return result;
		}
		
		Integer countInserted = adminProductBO.registerProduct(name, category, Integer.parseInt(price), agePetProper, imageProduct, Integer.parseInt(quantity));
		
		if(countInserted < 0) {
			result.put("code", 500);
			result.put("error_message", "해당 상품 등록이 실패하였습니다.");
			log.info("[ADMIN-Product: register-product] fail to register current product:{},{},{},{},{}"
					, name, category, price, agePetProper, imageProduct);
			return result;
		}
		
		result.put("code", 200);
		result.put("result", "success");
		log.info("[ADMIN-Product: register-product] success registering current product:{},{},{},{},{}"
					, name, category, price, agePetProper, imageProduct);
		return result;
	}
	
	
	//해당 검색 페이지에서 바로 뿌려주는 형식
	@Operation(summary = "productListView() 상품 목록 조회", description = "재고 존재하는 모든 상품 대상 조회, 필터 중복 가능")
	@Parameters({
		@Parameter(name = "<Integer> id", description = "상품 pk", example = "10")
		, @Parameter(name = "<String> name", description = "상품명", example = "나도간식줘")
		, @Parameter(name = "<String> category", description = "상품 카데고리", example = "1")
		, @Parameter(name = "<String> priceFrom", description = "상품 가격, 원 단위", example = "10000")
		, @Parameter(name = "<String> priceUntil", description = "상품 가격, 원 단위", example = "10000")
		, @Parameter(name = "<String> agePetProper", description = "반려견 적정 섭취 연령", example = "under6months")
		, @Parameter(name = "<String> direction", description = "(direction과 idRequested) 페이지 방향; 'prev'와 'next' 둘 중 하나")
		, @Parameter(name = "<Integer> idRequested", description = "(direction과 idRequested) 페이지 방향; 'prev'와 'next' 둘 중 하나")
		, @Parameter(name = "<Integer> pageCurrent", description = "(pageCurrent과 pageRequested) 현재 페이지 번호", example = "0")
		, @Parameter(name = "<Integer> pageRequested", description = "(pageCurrent과 pageRequested) 요청받은 페이지 번호", example = "3")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "500", description = "error_message: \"해당 결과를 찾지 못하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"success\""
																	+"<br>, List&lt;AdminProductVO&gt; listProducts"
																	+"<br>, &lt;Integer&gt; numberPageCurrent"
																	+"<br>, &lt;Integer&gt; numberPageMax"
																	+"<br>, &lt;Integer&gt; limit"
																	+"<br>, &lt;Integer&gt; idFirst"
																	+"<br>, &lt;Integer&gt; idLast"
		, content = @Content(mediaType = "APPLICATION_JSON", schema = @Schema(implementation = AdminProductVO.class)))
	})
	@GetMapping("/product-list")
	public Map<String, Object> productListView(
				@RequestParam(required = false) Integer id 
				, @RequestParam(required = false) String name 
				, @RequestParam(required = false) String category
				, @RequestParam(required = false) Integer priceFrom
				, @RequestParam(required = false) Integer priceUntil
				, @RequestParam(required = false) String agePetProper
				, @RequestParam(required = false) String direction
				, @RequestParam(required = false) Integer idRequested
				, @RequestParam(required = false) Integer pageCurrent
				, @RequestParam(required = false) Integer pageRequested
				){
		Map<String, Object> result = new HashMap<>();
		log.info("[ADMIN-Product: productListView] RequestParam for Searching products get arrived at controller.");
		
		if(name != null && name.isBlank()) {
			name = null;
		}
		if(category != null && category.isBlank()) {
			category = null;
		}
		if(agePetProper != null && agePetProper.isBlank()) {
			agePetProper = null;
		}
		
		Page<AdminProductVO> pageInfo = adminProductBO.getProductsForPaging(id
																		, name
																		, category
																		, priceFrom
																		, priceUntil
																		, agePetProper
																		, direction
																		, idRequested
																		, pageCurrent
																		, pageRequested);
		
		List<AdminProductVO> listProducts = pageInfo.generateCurrentPageList();
		
		if(ObjectUtils.isEmpty(listProducts)) {
			result.put("code", 500);
			result.put("error_message", "해당 결과를 찾지 못하였습니다.");
			return result;
		}
		result.put("code", 200);
		result.put("result", "success");
		result.put("listProducts", listProducts);
		result.put("numberPageCurrent", pageInfo.getNumberPageCurrent());
		result.put("numberPageMax", pageInfo.getNumberPageMax());
		result.put("limit", pageInfo.getLimit());
		result.put("idFirst", pageInfo.getIdFirst());
		result.put("idLast", pageInfo.getIdLast());		
		
		return result;
	}
	
	@Operation(summary = "updateProduct() 상품 정보 수정", description = "관리자페이지 기존 상품 정보 수정")
	@Parameters({
		@Parameter(name = "<String> name", description = "상품명", example = "10")
		, @Parameter(name = "<String> category", description = "상품 카데고리", example = "1")
		, @Parameter(name = "<String> price", description = "상품 가격, 원 단위", example = "10000")
		, @Parameter(name = "<String> agePetProper", description = "반려견 적정 섭취 연령", example = "under6months")
		, @Parameter(name = "<String> hasImageChanged", description = "상품 이미지 변경 여부", example = "true")
		, @Parameter(name = "<MultipartFile> imageProduct", description = "(변경 없을 경우 nullable)상품 이미지", example = "treat03_givemetreat.png")
		, @Parameter(name = "<String> quantity", description = "추가될 상품 재고, 유닛 단위", example = "100")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "500__1", description = "error_message: \"특정 변수가 잘못 입력되었습니다.\"" 
															+ "<br> wrong_parameter &lt;String&gt; 잘못 넘어온 변수 명칭" 
						, content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500__2", description = "error_message: \"해당 상품 정보가 존재하지 않습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500__3", description = "error_message: \"해당 상품 정보 수정 시도가 실패하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"success\"", content = @Content(mediaType = "APPLICATION_JSON"))
	})
	@PostMapping("/update-product")
	public Map<String, Object> updateProduct(
			@RequestPart String id 
			, @RequestPart String name 
			, @RequestPart String category
			, @RequestPart String price
			, @RequestPart String agePetProper
			, @RequestPart String hasImageChanged
			, @RequestPart(required = false) MultipartFile imageProduct
			){
		Map<String, Object> result = new HashMap<>();

		String wrongParameterRequested = 
				AdminProductParamsValidation.getParamsValidated(name
															, category
															, price
															, agePetProper
															, "0");
		
		if(wrongParameterRequested != null) {
			result.put("code", 500);
			result.put("error_message", "특정 변수가 잘못 입력되었습니다.");
			result.put("wrong_parameter", wrongParameterRequested);
			log.info("[ADMIN-Product: register-product] fail to register current product:{},{},{},{},{}"
					, name, category, price, agePetProper, imageProduct);
			return result;
		}
		
		AdminProductVO vo = adminProductBO.getProduct(Integer.parseInt(id), null, null, null, null).get(0);
		
		if(ObjectUtils.isEmpty(vo)) {
			result.put("code", 500);
			result.put("error_message", "해당 상품 정보가 존재하지 않습니다.");
			return result;
		}
		 
		Boolean imageChanged = hasImageChanged.equals("true") ? true : false;
		 
		Integer countUpdated = adminProductBO.updateProduct(Integer.parseInt(id)
															, name
															, category
															, Integer.parseInt(price)
															, agePetProper
															, imageChanged
															, imageProduct);
		
		if(countUpdated < 1) {
			result.put("code", 500);
			result.put("error_message", "해당 상품 정보 수정 시도가 실패하였습니다.");
			log.info("[ADMIN-Product: updateProduct()] fail to register current product:{},{},{},{},{}"
					, name, category, price, agePetProper, imageProduct);
			return result;
		}
		
		result.put("code", 200);
		result.put("result", "success");
		log.info("[ADMIN-Product: updateProduct()] success updating current product info. product id:{}", id);
		return result;
	}	
	
	
	@Operation(summary = "deleteProduct() 해당 상품 삭제", description = "상품 삭제")
	@Parameters({
		@Parameter(name = "<int> id", description = "상품(product) pk", example = "10")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "500", description = "error_message: \"해당 상품을 삭제하지 못하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"success\"", content = @Content(mediaType = "APPLICATION_JSON"))
	})
	@DeleteMapping("/delete-product")
	public Map<String, Object> deleteProduct(
				@RequestParam int id){
		Map<String, Object> result = new HashMap<>();
		 
		int count = adminProductBO.deleteProduct(id);
		
		if(count < 1) {
			log.info("[ADMIN-Product: deleteProduct()] failed to delete current product; id:{}", id);
			result.put("code", 500);
			result.put("error_message", "해당 상품을 삭제하지 못하였습니다.");
			return result;
		}
		
		result.put("code", 200);
		result.put("result", "success");
		return result;
	}
}
