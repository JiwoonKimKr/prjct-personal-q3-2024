package com.givemetreat.user.domain;

import java.time.LocalDateTime;
import java.util.*;

import com.givemetreat.pet.domain.Pet;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AdminUserVO {
	
	private int userId;
	private String loginId;
	private String nickname;
	private String imgProfile;
	private String imgThumbnail;
	private String selfDesc;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private List<Pet> listPets;
	
	public AdminUserVO(UserEntity user, List<Pet> listPets) {
		 this.userId = user.getId();
		 this.loginId = user.getLoginId();
		 this.nickname = user.getNickname();
		 this.imgProfile = user.getImgProfile();
		 this.imgThumbnail = user.getImgThumbnail();
		 this.selfDesc = user.getSelfDesc();
		 this.createdAt = user.getCreatedAt();
		 this.updatedAt = user.getUpdatedAt();
		 
		 this.listPets = listPets;
	}
	
}
