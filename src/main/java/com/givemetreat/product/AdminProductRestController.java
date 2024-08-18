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
import com.givemetreat.product.bo.AdminProductBO;
import com.givemetreat.product.domain.AdminProductVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/admin/product")
@RequiredArgsConstructor
@RestController
public class AdminProductRestController {
	private final AdminProductBO adminProductBO;
	
	//localhost/admin/product/register-product
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

		//TODO Server쪽에서도 관련 Parameter들 Validatoin 거쳐야!!!
		
		Integer pkInserted = adminProductBO.registerProduct(name, category, Integer.parseInt(price), agePetProper, imageProduct, Integer.parseInt(quantity));
		
		if(pkInserted < 0) {
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
	@GetMapping("/product-list")
	public Map<String, Object> productListView(
				@RequestParam(required = false) Integer id 
				, @RequestParam(required = false) String name 
				, @RequestParam(required = false) String category
				, @RequestParam(required = false) Integer price
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
																		, price
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
	
	@DeleteMapping("/delete-product")
	public Map<String, Object> deleteProduct(
				@RequestParam(required = false)int id){
		Map<String, Object> result = new HashMap<>();
		 
		int count = adminProductBO.deleteProduct(id);
		
		if(count < 0) {
			log.info("[ADMIN-Product: deleteProduct()] failed to delete current product; id:{}", id);
		}
		
		result.put("code", 200);
		result.put("result", "success");
		return result;
	}
}
