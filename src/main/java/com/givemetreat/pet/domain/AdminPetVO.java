package com.givemetreat.pet.domain;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AdminPetVO {
	
	public AdminPetVO(Pet pet, String loginId, String nickname) {
		this.id = pet.getId();
		
		this.userId = pet.getUserId();
		this.loginId = loginId;
		this.userNickname = nickname;
		
		this.name = pet.getName();
		this.age = pet.getAge().getAgePetE();
		this.imgProfile = pet.getImgProfile();
		this.imgThumbnail = pet.getImgThumbnail();
		this.createdAt = pet.getCreatedAt();
		this.updatedAt = pet.getUpdatedAt();
	}
	private int id;
	
	private int userId;
	private String loginId;
	private String userNickname;
	
	private String name;
	private String age;
	private String imgProfile;
	private String imgThumbnail;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
