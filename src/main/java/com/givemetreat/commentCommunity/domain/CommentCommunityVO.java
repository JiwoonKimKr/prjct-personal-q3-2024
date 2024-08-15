package com.givemetreat.commentCommunity.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentCommunityVO {
	
	public CommentCommunityVO(CommentCommunityEntity entity, String loginId, String nickname) {
		this.commentId = entity.getId();
		this.postId = entity.getPostId();
		this.userId = entity.getUserId();
		this.content = entity.getContent();
		this.createdAt = entity.getCreatedAt();
		this.updatedAt = entity.getUpdatedAt();
		
		this.loginId = loginId;
		this.nickname = nickname;		
	}

	private int commentId;
	private int postId;
	private int userId;
	
	private String loginId;
	private String nickname;
	
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;	
}
