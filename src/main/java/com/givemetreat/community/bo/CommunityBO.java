package com.givemetreat.community.bo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.givemetreat.commentCommunity.bo.CommentCommunityBO;
import com.givemetreat.commentCommunity.domain.CommentCommunityVO;
import com.givemetreat.postCommunity.bo.PostCommunityBO;
import com.givemetreat.postCommunity.domain.PostCommunityEntity;
import com.givemetreat.postCommunity.domain.PostCommunityVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommunityBO {
	private final PostCommunityBO postCommunityBO;
	private final CommentCommunityBO commentCommunityBO;
	
	public List<PostCommunityVO> getPostsLatestTop20() {
		return postCommunityBO.getPostsLatestTop20();
	}

	public List<PostCommunityVO> getPostsByAgePetProperLimit20(String agePetProper) {
		return postCommunityBO.getPostsByAgePetProPerLimit20(agePetProper);
	}

	public PostCommunityVO getPostByPostId(int postId) {
		return postCommunityBO.getPostByPostId(postId);
	}

	public List<CommentCommunityVO> getCommentsByPostId(int postId) {
		return commentCommunityBO.getCommentsByPostId(postId);
	}

	public PostCommunityEntity addPost(Integer userId, String title, String content, String agePetProper) {
		return postCommunityBO.addPost(userId, title, content, agePetProper);
	}

}
