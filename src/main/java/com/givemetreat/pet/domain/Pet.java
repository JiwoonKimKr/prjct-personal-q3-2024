package com.givemetreat.pet.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Pet {
	private int id;
	private int userId;
	private String name;
	
	/**
	 *  {@link AgePet} Enum 타입 활용
	 */
	private AgePet age;
	private String imgProfile;
	private String imgThumbnail;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
