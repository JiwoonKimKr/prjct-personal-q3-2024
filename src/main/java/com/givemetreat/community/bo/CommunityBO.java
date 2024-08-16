package com.givemetreat.community.bo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.givemetreat.commentCommunity.bo.CommentCommunityBO;
import com.givemetreat.commentCommunity.domain.CommentCommunityVO;
import com.givemetreat.postCommunity.bo.PostCommunityBO;
import com.givemetreat.postCommunity.domain.PostCommunityEntity;
import com.givemetreat.postCommunity.domain.PostCommunityVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

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
		return postCommunityBO.addPost(userId, title, content, agePetProper);
	}

	@Transactional
	public PostCommunityEntity updatePost(int postId
										, Integer userId
										, String title
										, String content,
										String agePetProper) {
		return postCommunityBO.updatePost(postId, userId, title, content, agePetProper);
	}

}
