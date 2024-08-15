package com.givemetreat.commentCommunity.domain;

import java.time.LocalDateTime;

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
@Table(name="comment_community")
@Entity
public class CommentCommunityEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "postId")
	private int postId;
	
	@Column(name = "userId")
	private int userId;
	private String content;
	
	@CreationTimestamp
	@Column(name ="createdAt")
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(name = "updatedAt")
	private LocalDateTime updatedAt;
}
