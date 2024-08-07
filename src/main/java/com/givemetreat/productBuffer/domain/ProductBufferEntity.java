package com.givemetreat.productBuffer.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
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
@Table(name ="product_buffer")
@Entity
public class ProductBufferEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "productId")
	private int productId;
	
	private boolean reserved;
	
	@CreationTimestamp
	@Column(name = "createdAt")
	private int createdAt;
	
	@UpdateTimestamp
	@Column(name = "updatedAt")
	private int updatedAt;
}
