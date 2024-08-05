package com.givemetreat.pet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.givemetreat.pet.domain.Pet;

@Mapper
public interface PetMapper {
	Pet selectPetByUserIdAndName(
			@Param("userId") int userId
			, @Param("name") String name);
	
	List<Pet> selectPetsByUserId(int userId);
	
	int insertPet(
			@Param("userId") int userId
			, @Param("name") String name
			, @Param("age") String age
			, @Param("imagePathProfile") String imagePathProfile
			, @Param("imagePathThumbnail") String imagePathThumbnail);

	


}
