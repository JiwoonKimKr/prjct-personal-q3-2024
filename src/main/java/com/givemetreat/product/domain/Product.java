package com.givemetreat.product.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Product {
	private int id;
	private String name;
	private String category;
	private int price;
	private String agePetProper;
	private String imgProfile;
	private String imgThumbnail;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;	
}
