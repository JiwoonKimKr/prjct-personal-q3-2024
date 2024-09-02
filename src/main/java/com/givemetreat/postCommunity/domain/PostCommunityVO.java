package com.givemetreat.postCommunity.domain;

import java.time.LocalDateTime;

import org.apache.commons.lang3.ObjectUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "커뮤니티 게시글 VO")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostCommunityVO {
	
	public PostCommunityVO(PostCommunityEntity entity, String loginId, String nickname) {
		this.postId = entity.getId();
		this.userId = entity.getUserId();
		this.title = entity.getTitle();
		this.content = entity.getContent();
		this.agePetProper = ObjectUtils.isEmpty(entity.getAgePetProper()) ? null : entity.getAgePetProper().getAgePetE();
		this.agePetProperTranslatedK = ObjectUtils.isEmpty(entity.getAgePetProper()) ? null : entity.getAgePetProper().getAgePetK();
		this.createdAt = entity.getCreatedAt();
		this.updatedAt = entity.getUpdatedAt();
		
		this.loginId = loginId;
		this.nickname = nickname;
	}
	private int postId;
	private int userId;
	
	private String loginId;
	private String nickname;
	
	private String title;
	private String content;
	private String agePetProper;
	private String agePetProperTranslatedK;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
