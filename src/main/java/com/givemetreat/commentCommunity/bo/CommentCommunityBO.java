package com.givemetreat.commentCommunity.bo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
	
	@Transactional
	private CommentCommunityVO voFromEntity(CommentCommunityEntity entity) {
		UserEntity user = userBO.getUserById(entity.getUserId()); 
		String loginId = user.getLoginId();
		String nickname = user.getNickname();
		return new CommentCommunityVO(entity, loginId, nickname);
	}

	@Transactional
	public CommentCommunityEntity getCommentByPostIdAndCommentId(int postId, int commentId) {
		return commentCommunityRepository.findByIdAndPostId(commentId, postId);
	}
	
	@Transactional
	public void deleteCommentsByPostId(int postId) {
		List<CommentCommunityEntity> listEntities = commentCommunityRepository.findByPostIdOrderByIdDesc(postId);
		commentCommunityRepository.deleteAllInBatch(listEntities);
		log.info("[CommentCommunityBO deleteCommentsByPostId()] comments are deleted. Post ID:{}", postId);
	}

	@Transactional
	public CommentCommunityEntity addComment(int postId, Integer userId, String content) {
		return commentCommunityRepository.save(CommentCommunityEntity.builder()
										.postId(postId)
										.userId(userId)
										.content(content)
										.build());
	}

	@Transactional
	public CommentCommunityEntity deleteComment(int postId, int commentId, Integer userId) {
		CommentCommunityEntity entity = commentCommunityRepository.findByIdAndPostIdAndUserId(postId, commentId, userId);
		if(ObjectUtils.isEmpty(entity)) {
			log.warn("[CommentCommunityBO deleteComment()] failed to delete comment."
					+ "Post ID:{}, Comment ID:{}, User ID:{}", postId, commentId, userId);
			return null;
		}
		commentCommunityRepository.delete(entity);
		log.info("[CommentCommunityBO deleteComment()] comment get deleted."
				+ "Post ID:{}, Comment ID:{}, User ID:{}"
				, postId, commentId, userId);
		return entity;
	}


}
