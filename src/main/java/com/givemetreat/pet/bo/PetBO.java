package com.givemetreat.pet.bo;

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
	
	public int addPet(int userId, String loginId, String name, int age, MultipartFile file) {
		String imagePathProfile = FileManagerService.uploadFile(file, loginId);
		
		//TODO imageThumbnail도 생성해서 DB에 넣을 때 Thumbnail 경로도 추가해야 한다!
		return petMapper.insertPet(userId, name, age, imagePathProfile, null);
	}

	public Pet getPetByUserIdAndName(int userId, String name) {
		return petMapper.selectPetByUserIdAndName(userId, name);
	}

	
}
