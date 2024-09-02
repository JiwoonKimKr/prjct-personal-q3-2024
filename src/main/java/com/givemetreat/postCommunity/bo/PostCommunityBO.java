package com.givemetreat.postCommunity.bo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
		//AOP 도입에 해당 AgePet Enum 클래스가 직접 Argument로 포함되어 있어야 하는 탓에, 이 메소드만 보완하였다_28 08 2024
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

	public List<PostCommunityVO> getPostsByKeyword(String keyword) {
		List<String> listString = new ArrayList<>(Arrays.asList(keyword.trim().split("_"))) ;
		List<PostCommunityEntity> listPosts = new ArrayList<>();
		
		for(String unit : listString) {
			Character lastLetter = unit.charAt(unit.length()-1);
			List<Character> listChar = Arrays.asList('이', '가', '을', '를', '는', '도');
			if(listChar.contains(lastLetter)) {
				unit = unit.substring(0, unit.length()-1);
				log.info("[PostCommunityBO getPostsByKeyword()] last letter of String get erased."
						+ " String Unit:{}", unit);
			}
			
			List<PostCommunityEntity> listEntities = postCommunityRepository.findByTitleStartingWith(unit);
			
			for(PostCommunityEntity entity : listEntities) {
				if(listPosts.contains(entity) == false) {
					listPosts.add(entity);
				}
			}
		}
		listPosts.sort(Comparator.comparing(PostCommunityEntity::getCreatedAt).reversed());
		
		log.info("[PostCommunityBO getPostsByKeyword()] List PostEntities get selected."
				+ " listString:{}", listString.toString());
		return listPosts.stream().map(entity -> voFromEntity(entity)).collect(Collectors.toList());
	}
}
