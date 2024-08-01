package com.givemetreat.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.givemetreat.user.domain.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	UserEntity findUserByLoginId(String loginId);

	UserEntity findUserByLoginIdAndPassword(String loginId, String hashedPassword);

}
