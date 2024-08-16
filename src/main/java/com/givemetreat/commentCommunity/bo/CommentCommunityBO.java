package com.givemetreat.commentCommunity.bo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.givemetreat.commentCommunity.domain.CommentCommunityEntity;
import com.givemetreat.commentCommunity.domain.CommentCommunityVO;
import com.givemetreat.commentCommunity.repository.CommentCommunityRepository;
import com.givemetreat.user.bo.UserBO;
import com.givemetreat.user.domain.UserEntity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentCommunityBO {
	private final CommentCommunityRepository commentCommunityRepository;
	private final UserBO userBO;
	
	@Transactional
	public List<CommentCommunityVO> getCommentsByPostId(int postId) {
		List<CommentCommunityEntity> listEntities = commentCommunityRepository.findByPostIdOrderByIdDesc(postId);
		List<CommentCommunityVO> listVOs = listEntities.stream()
							.map(entity -> voFromEntity(entity))
							.collect(Collectors.toList());
		return listVOs;
	}
	
	
	private CommentCommunityVO voFromEntity(CommentCommunityEntity entity) {
		UserEntity user = userBO.getUserById(entity.getUserId()); 
		String loginId = user.getLoginId();
		String nickname = user.getNickname();
		return new CommentCommunityVO(entity, loginId, nickname);
	}


	public void deleteCommentsByPostId(int postId) {
		List<CommentCommunityEntity> listEntities = commentCommunityRepository.findByPostIdOrderByIdDesc(postId);
		commentCommunityRepository.deleteAllInBatch(listEntities);
		log.info("[CommentCommunityBO deleteCommentsByPostId()] comments are deleted. Post ID:{}", postId);
	}
}
