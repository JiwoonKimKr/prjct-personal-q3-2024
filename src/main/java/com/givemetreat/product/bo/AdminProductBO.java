package com.givemetreat.product.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.givemetreat.common.FileManagerService;
import com.givemetreat.product.domain.AdminProductVO;
import com.givemetreat.product.domain.Product;
import com.givemetreat.product.mapper.ProductMapper;
import com.givemetreat.productBuffer.bo.ProductBufferBO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminProductBO {
	private final ProductMapper productMapper;
	private final ProductBufferBO ProductBufferBO;

	public List<AdminProductVO> getProduct(Integer id
								, String name
								, String category
								, Integer price
								, String agePetProper) {
		List<Product> listProducts = productMapper.selectProduct(id, name, category, price, agePetProper);
		
		//Product를 VO로 변환 후 List 형태로 반환
		List<AdminProductVO> listVOs = new ArrayList<>();
 		
		for(Product product : listProducts) {
			AdminProductVO vo = new AdminProductVO(product);
			Integer productId = product.getId();
			
			//총 재고조회_배송예정 수량 포함
			vo.setCountTotal(ProductBufferBO.getTotalCountByProductId(productId));
			
			//가용 재고_배송예정 수량 제외
			vo.setCountAvailable(ProductBufferBO.getCountAvailableByProductIdAndReserved(productId, false));
			
			listVOs.add(vo);
		}
		
		return listVOs;
	}
	
	@Transactional
	public int registerProduct(String name
							, String category
							, int price
							, String agePetProper,
							MultipartFile imageProduct
							, int quantity) {

		List<String> ListpathsImageProfile = FileManagerService
												.uploadImageWithThumbnail(
														imageProduct
														, "productProfile");
		
		Product productToInsert = new Product(name
										, category
										, price
										, agePetProper
										, ListpathsImageProfile.get(0) //imgProfile
										, ListpathsImageProfile.get(1)); //imgThumbnail
		
		
		
		// MyBatis에서 생성된 record의 ID값을 얻으려는데, 계속 에러 발생 ㅠ
		//아예 Product를 생성해서 parameter로 넘기는 방식으로 선회_13:30_08 08 2024
		int countRecorded = productMapper.insertProduct(productToInsert);
		
		//Mapper를 모두 거쳐서 해당 Entity에서 ID값을 빼올 수 있다!
		int productId = productToInsert.getId();
		log.info("[AdminProductBO registerProduct()] id of Product inserted:{}", productId);
		
		//바로 count를 controller에 return하기 전에
		//productBufferBO에서 해당 quantity만큼 record들을 생성
		
		ProductBufferBO.addProductBuffersInQuantity(productToInsert.getId(), quantity);	
		
		log.info("[AdminProductBO] requested ProductBufferBO.addProductBuffersInQuantity(productId:{}, quantity:{})", productId, quantity);
		
		return countRecorded;
	}

	public int deleteProduct(int id) {
		//해당 product의 product_buffer도 모두 삭제?!ㄷㄷㄷㄷ
		
		return productMapper.deleteProduct(id);
	}

}
