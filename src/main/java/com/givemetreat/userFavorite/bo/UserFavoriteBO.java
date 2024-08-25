package com.givemetreat.userFavorite.bo;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.givemetreat.userFavorite.domain.UserFavoriteEntity;
import com.givemetreat.userFavorite.repository.UserFavoriteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserFavoriteBO {
	private final UserFavoriteRepository userFavoriteRepository;

	/**
	 * Category 또는 agePetProper Field값 갱신(update)
	 * @param userId
	 * @param category
	 * @return
	 */
	@Transactional
	public UserFavoriteEntity updateUserFavors(Integer userId, String category, String agePetProper) {
		
		//userId에 대한 기존 Entity가 존재하면 record를 update하도록
		UserFavoriteEntity entity = userFavoriteRepository.findByUserId(userId);
		
		if(ObjectUtils.isEmpty(entity) == false) {
			return userFavoriteRepository.save(entity.toBuilder()
													.category(category)
													.agePetProper(agePetProper)
													.build());
		}
		
		//생성된 적이 없다면 새 Entity 생성
		return userFavoriteRepository.save(UserFavoriteEntity.builder()
															.userId(userId)
															.category(category)
															.build());
	}

	@Transactional
	public UserFavoriteEntity getEntityByUserId(Integer userId) {
		return userFavoriteRepository.findByUserId(userId);
	}
}
