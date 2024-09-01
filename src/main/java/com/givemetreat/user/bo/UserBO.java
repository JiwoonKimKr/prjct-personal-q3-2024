package com.givemetreat.user.bo;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.givemetreat.common.FileManagerService;
import com.givemetreat.user.domain.UserEntity;
import com.givemetreat.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserBO {
	private final UserRepository userRepository;
	private final FileManagerService fileManagerService;
	
	@Transactional
	public UserEntity updatePassword(UserEntity user, String password, String salt) {
		return userRepository.save(user.toBuilder()
				.password(password)
				.salt(salt)
				.build());
	}
	
	@Transactional
	public UserEntity addUser(String loginId, String password, String salt, String nickname) {
		return userRepository.save(UserEntity.builder()
				.loginId(loginId)
				.password(password)
				.salt(salt)
				.nickname(nickname)
				.build()
				);
	}

	//로그인 아이디(Email)로 유저 검색
	@Transactional
	public UserEntity getUserByLoginId(String loginId) {
		return userRepository.findUserByLoginId(loginId);
	}

	@Transactional
	public UserEntity getUserByLoginIdAndPassword(String loginId, String hashedPassword) {
		return userRepository.findUserByLoginIdAndPassword(loginId, hashedPassword);
	}

	@Transactional
	public UserEntity getUserById(int userId) {
		return userRepository.findById(userId).orElse(null);
	}

	/**
	 * 사용자 프로필 이미지 등록
	 * @param userId
	 * @param loginId
	 * @param user profile image file
	 * @return
	 */
	@Transactional
	public UserEntity updateImageProfile(int userId, String loginId, MultipartFile file) {
		List<String> imagePathProfile = fileManagerService.uploadImageWithThumbnail(file, loginId);
		UserEntity user = userRepository.findById(userId).orElse(null);
		
		String imgProfilePrev = user.getImgProfile();
		String imgThumbnailPrev = user.getImgThumbnail();
		
		user = userRepository.save(user.toBuilder()
								.imgProfile(imagePathProfile.get(0))
								.imgThumbnail(imagePathProfile.get(1))
								.build());
		
		//정상적으로 저장되었고, 기존 파일과 다르면 이제 삭제한다!
		if(ObjectUtils.isEmpty(user.getImgProfile()) == false 
				&& user.getImgProfile().equals(imgProfilePrev) == false) {
			log.info("[UserBO updateImageProfile()] trial to delete previous profile images. usesId:{}", userId);
			fileManagerService.deleteImageOriginAndThumbnail(imgProfilePrev, imgThumbnailPrev);
		}
		
		return user;
	}

	@Transactional
	public UserEntity updateSelfDescription(int userId, String selfDescription) {
		UserEntity user = userRepository.findById(userId).orElse(null);
		return userRepository.save(user.toBuilder()
				.selfDesc(selfDescription)
				.build());
	}

}
