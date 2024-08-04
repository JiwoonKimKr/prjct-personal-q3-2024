package com.givemetreat.pet.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.givemetreat.pet.domain.Pet;

@Mapper
public interface PetMapper {
	Pet selectPetByUserIdAndName(
			@Param("userId") int userId
			, @Param("name") String name);
	
	int insertPet(
			@Param("userId") int userId
			, @Param("name") String name
			, @Param("age") int age
			, @Param("imagePathProfile") String imagePathProfile
			, @Param("imagePathThumbnail") String imagePathThumbnail);


}
