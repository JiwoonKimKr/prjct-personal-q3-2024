package com.givemetreat.community.bo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.givemetreat.postCommunity.bo.PostCommunityBO;
import com.givemetreat.postCommunity.domain.PostCommunityVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommunityBO {
	private final PostCommunityBO postCommunityBO;
	
	public List<PostCommunityVO> getPostsLatestTop20() {
		return postCommunityBO.getPostsLatestTop20();
	}

	public List<PostCommunityVO> getPostsByAgePetProperLimit20(String agePetProper) {
		return postCommunityBO.getPostsByAgePetProPerLimit20(agePetProper);
	}

}
