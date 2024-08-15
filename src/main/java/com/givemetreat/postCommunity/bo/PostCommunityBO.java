package com.givemetreat.postCommunity.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.givemetreat.postCommunity.domain.PostCommunityEntity;
import com.givemetreat.postCommunity.domain.PostCommunityVO;
import com.givemetreat.postCommunity.repository.PostCommunityRepository;
import com.givemetreat.user.bo.UserBO;
import com.givemetreat.user.domain.UserEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostCommunityBO {
	private final PostCommunityRepository postCommunityRepository;
	private final UserBO userBO;

	public List<PostCommunityVO> getPostsLatestTop20() {
		List<PostCommunityEntity> listEntities = postCommunityRepository.findTop20ByOrderByIdDesc();
		List<PostCommunityVO> listVOs = new ArrayList<>();
		
		for(PostCommunityEntity entity: listEntities) {
			UserEntity user = userBO.getUserById(entity.getUserId()); 
			String loginId = user.getLoginId();
			String nickname = user.getNickname();
			PostCommunityVO vo = new PostCommunityVO(entity, loginId, nickname);
			listVOs.add(vo);
		}
		
		return listVOs;
	}

	public List<PostCommunityVO> getPostsByAgePetProPerLimit20(String agePetProper) {
		List<PostCommunityEntity> listEntities = postCommunityRepository.findTop20ByAgePetProperOrderByIdDesc(agePetProper);
		List<PostCommunityVO> listVOs = new ArrayList<>();
		
		for(PostCommunityEntity entity: listEntities) {
			UserEntity user = userBO.getUserById(entity.getUserId()); 
			String loginId = user.getLoginId();
			String nickname = user.getNickname();
			PostCommunityVO vo = new PostCommunityVO(entity, loginId, nickname);
			listVOs.add(vo);
		}
		
		return listVOs;
	}
}
