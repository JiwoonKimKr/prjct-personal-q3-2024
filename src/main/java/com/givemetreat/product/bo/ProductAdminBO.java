package com.givemetreat.product.bo;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import com.givemetreat.common.FileManagerService;
import com.givemetreat.product.domain.Product;
import com.givemetreat.product.mapper.ProductMapper;

@RequiredArgsConstructor
@Service
public class ProductAdminBO {
	private final ProductMapper productMapper;

	public List<Product> getProduct(Integer id
								, String name
								, String category
								, Integer price
								, String agePetProper) {
		return productMapper.selectProduct(id, name, category, price, agePetProper);
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
