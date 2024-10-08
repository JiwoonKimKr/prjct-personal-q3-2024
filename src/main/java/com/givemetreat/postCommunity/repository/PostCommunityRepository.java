package com.givemetreat.postCommunity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.givemetreat.pet.domain.AgePet;
import com.givemetreat.postCommunity.domain.PostCommunityEntity;

public interface PostCommunityRepository extends JpaRepository<PostCommunityEntity, Integer> {

	List<PostCommunityEntity> findTop20ByOrderByIdDesc();

	List<PostCommunityEntity> findTop20ByAgePetProperOrderByIdDesc(AgePet agePetProper);

	List<PostCommunityEntity> findByTitleStartingWith(String unit);

}
