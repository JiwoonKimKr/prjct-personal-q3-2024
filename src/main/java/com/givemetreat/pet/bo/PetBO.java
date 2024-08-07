package com.givemetreat.pet.bo;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.givemetreat.common.FileManagerService;
import com.givemetreat.pet.domain.Pet;
import com.givemetreat.pet.mapper.PetMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PetBO {
	private final PetMapper petMapper;
	
	public int addPet(int userId, String loginId, String name, String age, MultipartFile file) {
		List<String> imagePathProfile = FileManagerService.uploadImageWithThumbnail(file, loginId);
		
		//TODO imageThumbnail도 생성해서 DB에 넣을 때 Thumbnail 경로도 추가해야 한다!
		return petMapper.insertPet(
								userId
								, name
								, age
								, imagePathProfile.get(0)
								, imagePathProfile.get(1));
	}

	public Pet getPetByUserIdAndName(int userId, String name) {
		return petMapper.selectPetByUserIdAndName(userId, name);
	}

	public List<Pet> getPetsByUserId(int userId) {
		return petMapper.selectPetsByUserId(userId);
	}

	public Pet getPetByUserIdAndPetId(int userId, int petId) {
		//Mapper로 보낼때는 petId를 Id라고 지칭하여 순서를 바꾸었다!
		return petMapper.selectPetByIdAndUserId(petId, userId);
	}

	
}
