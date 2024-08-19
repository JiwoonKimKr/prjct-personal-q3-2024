package com.givemetreat.user.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.givemetreat.user.domain.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	UserEntity findUserByLoginId(String loginId);

	UserEntity findUserByLoginIdAndPassword(String loginId, String hashedPassword);

	List<UserEntity> findByLoginIdStartingWithOrderByIdDesc(String loginId);
	
	List<UserEntity> findByNicknameStartingWithOrderByIdDesc(String nickname);

	List<UserEntity> findBySelfDescContainingOrderByIdDesc(String nickname);

	List<UserEntity> findByCreatedAtOrderByIdDesc(LocalDateTime createdAt);

	List<UserEntity> findByUpdatedAtOrderByIdDesc(LocalDateTime updatedAt);

	List<UserEntity> findAllTop10ByOrderByIdDesc(); //Top 숫자 (Limit)기능 뒤에 By 꼭 붙여야 ㅠㅠ
	
	//Paging) findAll Top 10
	Page<UserEntity> findAllTop10By(Pageable pageable);

	//Paging) userId
	Page<UserEntity> findAllById(Integer userId, Pageable pageable);

	//Paging) loginId
	Page<UserEntity> findAllByLoginIdStartingWith(String loginId, Pageable pageable);
	
	//Paging) nickname
	Page<UserEntity> findAllByNicknameStartingWith(String nickname, Pageable pageable);
	
	//Paging) selfDesc
	Page<UserEntity> findAllBySelfDescContaining(String selfDesc, Pageable pageable);
	
	//Paging) createdAt
	Page<UserEntity> findAllByCreatedAt(LocalDateTime createdAt, Pageable pageable);
	
	//Paging) updatedAt
	Page<UserEntity> findAllByUpdatedAt(LocalDateTime updatedAt, Pageable pageable);


	



}
