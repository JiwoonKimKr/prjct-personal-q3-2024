package com.givemetreat.pet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.givemetreat.pet.domain.AgePet;
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
			, @Param("age") AgePet age
			, @Param("imagePathProfile") String imagePathProfile
			, @Param("imagePathThumbnail") String imagePathThumbnail);

	Pet selectPetByIdAndUserId(
			@Param("id") int id
			, @Param("userId") int userId);

	int deletePetByIdAndUserId(
			@Param("id") int id
			, @Param("userId") int userId);

	Integer countPetsByUserId(
			@Param("userId") int userId);

	void updatePet(@Param("id") int id
				, @Param("name") String name
				, @Param("age") AgePet age
				, @Param("imagePathProfile") String imagePathProfile
				, @Param("imagePathThumbnail") String imagePathThumbnail);
}
