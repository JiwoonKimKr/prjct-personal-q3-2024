package com.givemetreat.userFavorite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.givemetreat.userFavorite.domain.UserFavoriteEntity;

public interface UserFavoriteRepository extends JpaRepository<UserFavoriteEntity, Integer>{

	UserFavoriteEntity findByUserId(Integer userId);

}
