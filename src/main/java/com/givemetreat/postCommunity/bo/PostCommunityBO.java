package com.givemetreat.postCommunity.bo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.givemetreat.aop.TrackUsersFavor;
import com.givemetreat.pet.domain.AgePet;
import com.givemetreat.postCommunity.domain.PostCommunityEntity;
import com.givemetreat.postCommunity.domain.PostCommunityVO;
import com.givemetreat.postCommunity.repository.PostCommunityRepository;
import com.givemetreat.user.bo.UserBO;
import com.givemetreat.user.domain.UserEntity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostCommunityBO {
	private final PostCommunityRepository postCommunityRepository;
	private final UserBO userBO;

	@Transactional
	public List<PostCommunityVO> getPostsLatestTop20() {
		List<PostCommunityEntity> listEntities = postCommunityRepository.findTop20ByOrderByIdDesc();
		List<PostCommunityVO> listVOs = listEntities.stream()
									.map(entity -> voFromEntity(entity))
									.collect(Collectors.toList());
		return listVOs;
	}

	@Transactional
	public List<PostCommunityVO> getPostsByAgePetProPerLimit20(String agePetProper) {
		//Enum 타입 도입_27 08 2024
		AgePet agePetCurrent = AgePet.findAgeCurrent(agePetProper, null, null);
		List<PostCommunityEntity> listEntities = postCommunityRepository.findTop20ByAgePetProperOrderByIdDesc(agePetCurrent);
		List<PostCommunityVO> listVOs = listEntities.stream()
									.map(entity -> voFromEntity(entity))
									.collect(Collectors.toList());
		
		return listVOs;
	}

	@Transactional
	public PostCommunityVO getPostByPostId(int postId) {
		PostCommunityEntity entity = postCommunityRepository.findById(postId).orElse(null);
		return voFromEntity(entity);
	}
	@Transactional
	private PostCommunityVO voFromEntity(PostCommunityEntity entity) {
		UserEntity user = userBO.getUserById(entity.getUserId()); 
		String loginId = user.getLoginId();
		String nickname = user.getNickname();
		return new PostCommunityVO(entity, loginId, nickname);
	}
	@TrackUsersFavor
	@Transactional
	public PostCommunityEntity addPost(Integer userId, String title, String content, AgePet agePetProper) {
		//Enum 타입 도입_27 08 2024
		return postCommunityRepository.save(PostCommunityEntity.builder()
															.userId(userId)
															.title(title)
															.content(content)
															.agePetProper(agePetProper)
															.build());
	}
	@Transactional
	public PostCommunityEntity updatePost(int postId
										, Integer userId
										, String title
										, String content,
										String agePetProper) {
		PostCommunityEntity entity = postCommunityRepository.findById(postId).orElse(null);
		
		if(ObjectUtils.isEmpty(entity)) {
			log.warn("[PostCommunityBO updatePost()] post fail to get found. post ID:{}", postId );
			return null;
		}
		
		//Enum 타입 도입_27 08 2024
		AgePet agePetCurrent = AgePet.findAgeCurrent(agePetProper, null, null);
		return postCommunityRepository.save(entity.toBuilder()
											.title(title)
											.content(content)
											.agePetProper(agePetCurrent)
											.build());
	}

	public PostCommunityEntity deleteByPostIdAndUserId(int postId, Integer userId) {
		PostCommunityEntity entity = postCommunityRepository.findById(postId).orElse(null);
		postCommunityRepository.delete(entity);
		log.info("[PostCommunityBO deleteByPostIdAndUserId()] post get deleted."
				+ " Post Id:{}, User Id:{}", postId, userId);
		
		return entity;
	}
}
