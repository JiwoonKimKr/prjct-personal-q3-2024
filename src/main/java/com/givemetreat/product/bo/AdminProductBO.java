package com.givemetreat.product.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import com.givemetreat.common.FileManagerService;
import com.givemetreat.product.domain.AdminProductVO;
import com.givemetreat.product.domain.Product;
import com.givemetreat.product.mapper.ProductMapper;
import com.givemetreat.productBuffer.bo.productBufferBO;

@RequiredArgsConstructor
@Service
public class AdminProductBO {
	private final ProductMapper productMapper;
	private final productBufferBO productBufferBO;

	public List<AdminProductVO> getProduct(Integer id
								, String name
								, String category
								, Integer price
								, String agePetProper) {
		List<Product> listProducts = productMapper.selectProduct(id, name, category, price, agePetProper);
		List<AdminProductVO> listVOs = new ArrayList<>();
 		
		for(Product product : listProducts) {
			AdminProductVO vo = new AdminProductVO(product);
			vo.setBuffer(productBufferBO.getCountByProductId(product.getId()));
			
			listVOs.add(vo);
		}
		
		return listVOs;
	}

	public int registerProduct(String name
							, String category
							, int price
							, String agePetProper,
							MultipartFile imageProduct) {

		List<String> ListpathsImageProfile = FileManagerService
												.uploadImageWithThumbnail(
														imageProduct
														, "productProfile");
		
		return productMapper.insertProduct(name
										, category
										, price
										, agePetProper
										, ListpathsImageProfile.get(0)
										, ListpathsImageProfile.get(1));
	}

	public int deleteProduct(int id) {
		return productMapper.deleteProduct(id);
	}

}
