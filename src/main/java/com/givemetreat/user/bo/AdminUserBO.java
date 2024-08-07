package com.givemetreat.user.bo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.givemetreat.pet.bo.PetBO;
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

	/**
	 * 변수가 아예 하나만 있거나 아예 없거나 두 경우만 고려
	 * @param userid
	 * @param loginId
	 * @param nickname
	 * @param selfDesc
	 * @param createdAt
	 * @param updatedAt
	 * @return List <{@link AdminUserVO}>
	 */
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
			for(UserEntity user: listUsers) {
				List<Pet> listPets = petBO.getPetsByUserId(user.getId());
				AdminUserVO vo = new AdminUserVO(user, listPets);
				listVOs.add(vo);
			}
			return listVOs;
		}
		
		// nickname		
		if(nickname != null) {
			List<UserEntity> listUsers = userRepository.findByNicknameStartingWithOrderByIdDesc(nickname);
			
			for(UserEntity user: listUsers) {
				List<Pet> listPets = petBO.getPetsByUserId(user.getId());
				AdminUserVO vo = new AdminUserVO(user, listPets);
				listVOs.add(vo);
			}
			return listVOs;
		}
		
		// selfDesc		
		if(selfDesc != null) {
			List<UserEntity> listUsers = userRepository.findBySelfDescContainingOrderByIdDesc(nickname);
			
			for(UserEntity user: listUsers) {
				List<Pet> listPets = petBO.getPetsByUserId(user.getId());
				AdminUserVO vo = new AdminUserVO(user, listPets);
				listVOs.add(vo);
			}
			return listVOs;
		}
		
		//createdAt ★★★★★ 날짜 범위 추후 지정하도록 수정해야!
		if(createdAt != null) {
			List<UserEntity> listUsers = userRepository.findByCreatedAtOrderByIdDesc(createdAt);
			
			for(UserEntity user: listUsers) {
				List<Pet> listPets = petBO.getPetsByUserId(user.getId());
				AdminUserVO vo = new AdminUserVO(user, listPets);
				listVOs.add(vo);
			}
			return listVOs;
		}
		
		//updatedAt ★★★★★ 날짜 범위 추후 지정하도록 수정해야!		
		if(updatedAt != null) {
			List<UserEntity> listUsers = userRepository.findByUpdatedAtOrderByIdDesc(updatedAt);
			
			for(UserEntity user: listUsers) {
				List<Pet> listPets = petBO.getPetsByUserId(user.getId());
				AdminUserVO vo = new AdminUserVO(user, listPets);
				listVOs.add(vo);
			}
			return listVOs;
		}		
		
		//아무런 값도 넘어오지 않은 경우(쌩으로 다른 RequestParam 없이 진입하는 경우)
		List<UserEntity> listUsers = userRepository.findAllTop10ByOrderByIdDesc();
		
		for(UserEntity user: listUsers) {
			List<Pet> listPets = petBO.getPetsByUserId(user.getId());
			AdminUserVO vo = new AdminUserVO(user, listPets);
			listVOs.add(vo);
		}
		
		return listVOs;
	}

	@Transactional
	public Pet getPetByUserIdAndPetId(int userId, int petId) {

		return petBO.getPetByUserIdAndPetId(userId, petId);
	}

}
