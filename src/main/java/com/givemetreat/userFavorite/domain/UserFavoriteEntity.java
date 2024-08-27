package com.givemetreat.userFavorite.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.givemetreat.common.converter.AgePetConverter;
import com.givemetreat.common.converter.CategoryProductConverter;
import com.givemetreat.pet.domain.AgePet;
import com.givemetreat.product.domain.CategoryProduct;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Table(name = "user_favorite")
@Entity
public class UserFavoriteEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "userId")
	private int userId;
	
	@Column(nullable = true)
	private String keyword;
	
	/**
	 * {@link CategoryProduct} Enum 타입 활용
	 */
	@Convert(converter = CategoryProductConverter.class)
	@Column(nullable = true)
	private CategoryProduct category;
	
	/**
	 * {@link AgePet} Enum 타입 활용
	 */
	@Convert(converter = AgePetConverter.class)
	@Column(name = "agePetProper", nullable = true)
	private AgePet agePetProper;
	
	@CreationTimestamp
	@Column(name = "createdAt")
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(name = "updatedAt")
	private LocalDateTime updatedAt;
}
