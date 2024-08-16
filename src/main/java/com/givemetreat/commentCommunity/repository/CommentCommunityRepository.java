package com.givemetreat.commentCommunity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.givemetreat.commentCommunity.domain.CommentCommunityEntity;

public interface CommentCommunityRepository extends JpaRepository<CommentCommunityEntity, Integer> {

	List<CommentCommunityEntity> findByPostIdOrderByIdDesc(int postId);

	CommentCommunityEntity findByIdAndPostIdAndUserId(int postId, int commentId, Integer userId);

}
