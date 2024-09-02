package com.givemetreat.user.bo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.givemetreat.pet.bo.PetBO;
import com.givemetreat.pet.domain.AdminPetVO;
import com.givemetreat.pet.domain.Pet;
import com.givemetreat.user.domain.AdminUserVO;
import com.givemetreat.user.domain.UserEntity;
import com.givemetreat.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminUserBO {
	private final UserRepository userRepository;
	private final PetBO petBO;
	
	private final Integer SIZE_PAGE_CURRENT = 10;
	
	@Transactional
	public Page<AdminUserVO> getListUserVOsForPaging(Integer userId
											, String loginId
											, String nickname
											, String selfDesc
											, LocalDateTime createdAtSince
											, LocalDateTime createdAtUntil
											, LocalDateTime updatedAtSince
											, LocalDateTime updatedAtUntil
											, Integer page
											, Integer size
											) {
		if(ObjectUtils.isEmpty(page) || ObjectUtils.isEmpty(size)) {
			page = 0;
			size = SIZE_PAGE_CURRENT;
		}
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "id"));
		
		// userId
		if(userId != null) {
			//userId(PK)라서 값이 하나밖에 못 넘어온다
			Page<UserEntity> userPaged = userRepository.findAllById(userId, pageable);
			//DB에서 얻은 사용자값으로 VO 생성;
			Page<AdminUserVO> pageVO = userPaged.map(entity -> new AdminUserVO(entity, petBO.getPetsByUserId(userId)));
			return pageVO;
		}
		
		// loginId
		if(loginId != null) {
			//loginId라는 키워드로 넘어와서 여러 값의 user가 리스트로 넘어올 수 있다!
			Page<UserEntity> pageEntities = userRepository.findAllByLoginIdStartingWith(loginId, pageable);
			return pageEntities.map(entity -> new AdminUserVO(entity, petBO.getPetsByUserId(entity.getId())));
		}
		
		// nickname		
		if(nickname != null) {
			Page<UserEntity> pageEntities = userRepository.findAllByNicknameStartingWith(nickname, pageable);
			return pageEntities.map(entity -> new AdminUserVO(entity, petBO.getPetsByUserId(entity.getId())));
		}
		
		// selfDesc		
		if(selfDesc != null) {
			Page<UserEntity> pageEntities = userRepository.findAllBySelfDescContaining(selfDesc, pageable);
			return pageEntities.map(entity -> new AdminUserVO(entity, petBO.getPetsByUserId(entity.getId())));
		}
		
		if(createdAtSince != null && createdAtUntil != null) {
			Page<UserEntity> pageEntities = userRepository.findAllByCreatedAtBetween(createdAtSince, createdAtUntil, pageable);
			return pageEntities.map(entity -> new AdminUserVO(entity, petBO.getPetsByUserId(entity.getId())));
		}
		
		if(updatedAtSince != null && updatedAtUntil != null) {
			Page<UserEntity> pageEntities = userRepository.findAllByUpdatedAtBetween(updatedAtSince, updatedAtUntil, pageable);
			return pageEntities.map(entity -> new AdminUserVO(entity, petBO.getPetsByUserId(entity.getId())));
		}		
		
		//아무런 값도 넘어오지 않은 경우(쌩으로 다른 RequestParam 없이 진입하는 경우)
		Page<UserEntity> pageEntities = userRepository.findAllTop10By(pageable);
		return pageEntities.map(entity -> new AdminUserVO(entity, petBO.getPetsByUserId(entity.getId())));
	}	
	
	/**
	 * Pageable 활용해서 되서 결국 쓰는 곳이 없어짐ㅠ_19 08 2024
	 * 변수가 아예 하나만 있거나 아예 없거나 두 경우만 고려
	 * @param userid
	 * @param loginId
	 * @param nickname
	 * @param selfDesc
	 * @param createdAt
	 * @param updatedAt
	 * @return List <{@link AdminUserVO}>
	 */
	@Deprecated
	@Transactional
	public List<AdminUserVO> getListUserVOs(Integer userId
											, String loginId
											, String nickname
											, String selfDesc
											, LocalDateTime createdAt
											, LocalDateTime updatedAt) {
		List<AdminUserVO> listVOs = new ArrayList<>();
		
		//이번에는 invoice에서 MyBatis와 JPA를 섞어 쓴 것과 달리, JPA만 활용해보기로ㄷㄷㄷㄷ
		
		// userId
		if(userId != null) {
			//userId(PK)라서 값이 하나밖에 못 넘어온다
			UserEntity user = userRepository.findById(userId).orElse(null);
			List<Pet> listPets = petBO.getPetsByUserId(userId);
			
			//DB에서 얻은 사용자값으로 VO 생성;
			AdminUserVO vo = new AdminUserVO(user, listPets);
			listVOs.add(vo);
			return listVOs;
		}

		// loginId
		if(loginId != null) {
			//loginId라는 키워드로 넘어와서 여러 값의 user가 리스트로 넘어올 수 있다!
			List<UserEntity> listUsers = userRepository.findByLoginIdStartingWithOrderByIdDesc(loginId);
			return generateListVOsFromListEntities(listUsers);
		}
		
		// nickname		
		if(nickname != null) {
			List<UserEntity> listUsers = userRepository.findByNicknameStartingWithOrderByIdDesc(nickname);
			return generateListVOsFromListEntities(listUsers);
		}
		
		// selfDesc		
		if(selfDesc != null) {
			List<UserEntity> listUsers = userRepository.findBySelfDescContainingOrderByIdDesc(nickname);
			return generateListVOsFromListEntities(listUsers);
		}
		
		if(createdAt != null) {
			List<UserEntity> listUsers = userRepository.findByCreatedAtOrderByIdDesc(createdAt);
			return generateListVOsFromListEntities(listUsers);
		}
		
		if(updatedAt != null) {
			List<UserEntity> listUsers = userRepository.findByUpdatedAtOrderByIdDesc(updatedAt);
			return generateListVOsFromListEntities(listUsers);
		}		
		
		//아무런 값도 넘어오지 않은 경우(쌩으로 다른 RequestParam 없이 진입하는 경우)
		List<UserEntity> listUsers = userRepository.findAllTop10ByOrderByIdDesc();
		
		return generateListVOsFromListEntities(listUsers);
	}
	
	@Transactional
	public AdminPetVO getPetByUserIdAndPetId(int userId, int petId) {

		Pet petCurrent = petBO.getPetByUserIdAndPetId(userId, petId);
		
		UserEntity user = userRepository.findById(userId).orElse(null);
		
		return new AdminPetVO(petCurrent, user.getLoginId(), user.getNickname());
	}

	@Transactional
	public int deletePetByUserIdAndPetId(int userId, int petId) {
		Pet pet = petBO.getPetByUserIdAndPetId(userId, petId);
		return petBO.deletePetByUserIdAndPetId(pet);
	}

	/**
	 * 중복되는 코드 줄이기 위한 용도
	 * @param listEntities
	 * @return
	 */
	private List<AdminUserVO> generateListVOsFromListEntities(List<UserEntity> listEntities){
		List<AdminUserVO> listVOs = new ArrayList<>();
		for(UserEntity user: listEntities) {
			List<Pet> listPets = petBO.getPetsByUserId(user.getId());
			AdminUserVO vo = new AdminUserVO(user, listPets);
			listVOs.add(vo);
		}
		return listVOs;
	}
}
