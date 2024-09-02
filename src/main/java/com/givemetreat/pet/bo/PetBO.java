package com.givemetreat.pet.bo;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.givemetreat.common.FileManagerService;
import com.givemetreat.pet.domain.AgePet;
import com.givemetreat.pet.domain.Pet;
import com.givemetreat.pet.mapper.PetMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PetBO {
	private final PetMapper petMapper;
	private final FileManagerService fileManagerService;
	
	public int addPet(int userId, String loginId, String name, String age, MultipartFile file) {
		AgePet agePetCurrent = AgePet.findAgeCurrent(age, null, null);
		if(ObjectUtils.isEmpty(file)) {
			return petMapper.insertPet(
					userId
					, name
					, agePetCurrent
					, null
					, null);
		}
		List<String> imagePathProfile = fileManagerService.uploadImageWithThumbnail(file, loginId);
		
		return petMapper.insertPet(
								userId
								, name
								, agePetCurrent
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

	public int deletePetByUserIdAndPetId(Pet pet) {
		int userId = pet.getUserId();
		int petId = pet.getId();
		String imgProfileCur = pet.getImgProfile();
		String imgThumbnailCur = pet.getImgThumbnail();
		//Mapper로 보낼때는 petId를 Id라고 지칭하여 순서를 바꾸었다!
		int count = petMapper.deletePetByIdAndUserId(petId, userId);
		
		//DB에서 해당 반려견 정보가 지워졌으면, 그 때 이미지 지우기
		if(count > 0
			&& ObjectUtils.isEmpty(imgProfileCur) == false
			&& ObjectUtils.isEmpty(imgThumbnailCur) == false) {
			fileManagerService.deleteImageOriginAndThumbnail(imgProfileCur, imgThumbnailCur);
		}
		return count;
	}

	public Integer countPetsByUserId(int userId) {
		return petMapper.countPetsByUserId(userId);
	}

	@Transactional
	public Pet updatePet(int userId
						, String loginId
						, int petId
						, String name
						, String age
						, Boolean hasImageChanged
						, MultipartFile file) {
		Pet pet = petMapper.selectPetByIdAndUserId(petId, userId);
		
		if(ObjectUtils.isEmpty(pet)) {
			log.warn("[PetBO updatePet()] current pet failed to be found. userId:{}, petId:{}", userId, petId);
			return null;
		}
		
		String nameCur = pet.getName();
		AgePet ageCur = pet.getAge();
		String imgProfileCur = pet.getImgProfile();
		String imgThumbnailCur = pet.getImgThumbnail();
		
		if(ObjectUtils.isEmpty(name) == false) {
			nameCur = name;
		}
		if(ObjectUtils.isEmpty(age) == false) {
			ageCur = AgePet.findAgeCurrent(age, null, null);
		}
		if(ObjectUtils.isEmpty(file) && hasImageChanged) {
			//이미지 파일이 넘어오지 않은 경우
			petMapper.updatePet(petId
					, nameCur
					, ageCur
					, null
					, null);
			
		} else if(ObjectUtils.isEmpty(file) == false){
			//이미지 파일을 받은 경우
			List<String> imagePathProfile = fileManagerService.uploadImageWithThumbnail(file, loginId);
			petMapper.updatePet(petId
					, nameCur
					, ageCur
					, imagePathProfile.get(0)
					, imagePathProfile.get(1));
		} else { // 파일이 바뀐 것이 없는 경우; hasImageChanged= false인 경우
			petMapper.updatePet(petId
					, nameCur
					, ageCur
					, imgProfileCur
					, imgThumbnailCur);
		}
		
		//update된 Record로 가져옴;
		pet = petMapper.selectPetByIdAndUserId(petId, userId);
		
		//record의 이미지가 비워졌거나, 예전 파일들과 다른 경우 삭제한다!
		if((ObjectUtils.isEmpty(pet.getImgProfile()) && hasImageChanged)
				|| pet.getImgProfile().equals(imgProfileCur) == false) {
			
			if(ObjectUtils.isEmpty(imgProfileCur) == false 
					&& ObjectUtils.isEmpty(imgThumbnailCur) == false) {
				fileManagerService.deleteImageOriginAndThumbnail(imgProfileCur, imgThumbnailCur);
				log.info("[PetBO updatePet()] previous profile images got deleted. petId:{}", petId);
			}
		}
		
		return pet;
	}
	
}
