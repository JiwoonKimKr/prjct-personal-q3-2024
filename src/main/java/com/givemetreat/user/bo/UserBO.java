package com.givemetreat.user.bo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.givemetreat.user.domain.UserEntity;
import com.givemetreat.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserBO {
	private final UserRepository userRepository;
	
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

}
