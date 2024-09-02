package com.givemetreat.community.bo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.givemetreat.commentCommunity.bo.CommentCommunityBO;
import com.givemetreat.commentCommunity.domain.CommentCommunityEntity;
import com.givemetreat.commentCommunity.domain.CommentCommunityVO;
import com.givemetreat.pet.domain.AgePet;
import com.givemetreat.postCommunity.bo.PostCommunityBO;
import com.givemetreat.postCommunity.domain.PostCommunityEntity;
import com.givemetreat.postCommunity.domain.PostCommunityVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommunityBO {
	private final PostCommunityBO postCommunityBO;
	private final CommentCommunityBO commentCommunityBO;
	
	@Transactional
	public List<PostCommunityVO> getPostsLatestTop20() {
		return postCommunityBO.getPostsLatestTop20();
	}
	@Transactional
	public List<PostCommunityVO> getPostsByAgePetProperLimit20(String agePetProper) {
		return postCommunityBO.getPostsByAgePetProPerLimit20(agePetProper);
	}
	@Transactional
	public PostCommunityVO getPostByPostId(int postId) {
		return postCommunityBO.getPostByPostId(postId);
	}
	@Transactional
	public List<CommentCommunityVO> getCommentsByPostId(int postId) {
		return commentCommunityBO.getCommentsByPostId(postId);
	}
	
	@Transactional
	public PostCommunityEntity addPost(Integer userId, String title, String content, String agePetProper) {
		AgePet agePetcurrent = AgePet.findAgeCurrent(agePetProper, null, null);
		return postCommunityBO.addPost(userId, title, content, agePetcurrent);
	}

	@Transactional
	public PostCommunityEntity updatePost(int postId
										, Integer userId
										, String title
										, String content,
										String agePetProper) {
		return postCommunityBO.updatePost(postId, userId, title, content, agePetProper);
	}

	public PostCommunityEntity deletePostAndCommentsByPostIdAndUserId(int postId, Integer userId) {
		PostCommunityEntity entity = postCommunityBO.deleteByPostIdAndUserId(postId, userId);
		commentCommunityBO.deleteCommentsByPostId(postId);
		log.info("[CommunityBO deletePostAndCommentsByPostIdAndUserId()] post and comments get deleted."
				+ " Post Id:{}, User Id:{}", postId, userId);
		return entity;
	}
	
/* 댓글 관련 */
	@Transactional
	public CommentCommunityEntity getCommentByPostIdAndCommentId(int postId, int commentId) {
		return commentCommunityBO.getCommentByPostIdAndCommentId(postId, commentId);
	}
	
	@Transactional
	public CommentCommunityEntity addComment(int postId, Integer userId, String content) {
		return commentCommunityBO.addComment(postId, userId, content);
	}
	
	@Transactional
	public CommentCommunityEntity deleteComment(int postId, int commentId, Integer userId) {
		log.info("[CommunityBO deleteComment()] comment get deleted."
				+ " Post Id:{}, Comment Id:{}, User Id:{}"
				, postId, commentId, userId);
		return commentCommunityBO.deleteComment(postId, commentId, userId);
	}


}
