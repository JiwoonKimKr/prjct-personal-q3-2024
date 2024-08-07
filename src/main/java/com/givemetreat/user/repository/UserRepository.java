package com.givemetreat.user.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.givemetreat.user.domain.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	UserEntity findUserByLoginId(String loginId);

	UserEntity findUserByLoginIdAndPassword(String loginId, String hashedPassword);

	List<UserEntity> findByNicknameStartingWithOrderByIdDesc(String nickname);

	List<UserEntity> findBySelfDescContainingOrderByIdDesc(String nickname);

	List<UserEntity> findByCreatedAtOrderByIdDesc(LocalDateTime createdAt);

	List<UserEntity> findByUpdatedAtOrderByIdDesc(LocalDateTime updatedAt);

	List<UserEntity> findAllTop10ByOrderByIdDesc(); //Top 숫자 (Limit)기능 뒤에 By 꼭 붙여야 ㅠㅠ

}
