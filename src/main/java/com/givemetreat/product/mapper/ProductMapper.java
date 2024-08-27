package com.givemetreat.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.givemetreat.aop.TrackUsersFavor;
import com.givemetreat.pet.domain.AgePet;
import com.givemetreat.product.domain.CategoryProduct;
import com.givemetreat.product.domain.Product;

@Mapper
public interface ProductMapper {

	@TrackUsersFavor
	public List<Product> selectProduct(
			@Param("id") Integer id
			, @Param("name") String name
			, @Param("category") CategoryProduct category
			, @Param("price") Integer price
			, @Param("agePetProper") AgePet agePetProper);
	
	@TrackUsersFavor
	public List<Product> selectProductForPaging(
			@Param("id") Integer id
			, @Param("name") String name
			, @Param("category") CategoryProduct category
			, @Param("price") Integer price
			, @Param("agePetProper") AgePet agePetProper
			, @Param("direction") String direction
			, @Param("index") Integer index
			, @Param("limit") Integer limit);

	public List<Product> selectTop4ProductsRecommended(
			@Param("category") CategoryProduct category
			, @Param("agePetProper") AgePet agePetProper
			, @Param("limit") int limit);
	
	public Integer insertProduct(Product product);
	
//	public int insertProduct(
//			 @Param("name") String name
//			, @Param("category") String category
//			, @Param("price") int price
//			, @Param("agePetProper") String agePetProper
//			, @Param("imgProfile") String pathImageProfile
//			, @Param("imgThumbnail") String pathImageThumbnail);

	public int deleteProduct(int id);

	
	
}
