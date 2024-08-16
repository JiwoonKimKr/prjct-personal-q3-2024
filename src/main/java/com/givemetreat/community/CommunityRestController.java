package com.givemetreat.community;

import java.util.*;

import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.givemetreat.community.bo.CommunityBO;
import com.givemetreat.postCommunity.domain.PostCommunityEntity;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/community")
@RestController
public class CommunityRestController {
	private final CommunityBO communityBO;

	
	@PostMapping("/write-new-post")
	public Map<String, Object> writeNewPost(@RequestParam String title
											, @RequestParam String content
											, @RequestParam(required = false) String agePetProper
											, HttpSession session){
		
		Map<String, Object> result = new HashMap<>();
		Integer userId = (Integer) session.getAttribute("userId");
		if(ObjectUtils.isEmpty(userId)) {
			result.put("code", 403);
			result.put("code", "로그인 후 커뮤니티 글 작성이 가능합니다.");
			return result;
		}
		
		PostCommunityEntity post = communityBO.addPost(userId, title, content, agePetProper);
		
		if(ObjectUtils.isEmpty(post)) {
			result.put("code", 500);
			result.put("code", "새 글 쓰기 시도가 실패하였습니다.");
			return result;
		}
		
		result.put("code", 200);
		result.put("result", "새로운 글을 작성하였습니다.");
		
		return result;
	}
	
	@PostMapping("/update-current-post")
	public Map<String, Object> updateCurrentPost(@RequestParam int postId
												, @RequestParam String title
												, @RequestParam String content
												, @RequestParam(required = false) String agePetProper
												, HttpSession session
												){
		Map<String, Object> result = new HashMap<>();
		Integer userId = (Integer) session.getAttribute("userId");
		if(ObjectUtils.isEmpty(userId)) {
			result.put("code", 403);
			result.put("code", "로그인 후 커뮤니티에 접근 가능합니다.");
			return result;
		}
		
		PostCommunityEntity post = communityBO.updatePost(postId, userId, title, content, agePetProper);
		
		if(ObjectUtils.isEmpty(post)) {
			result.put("code", 500);
			result.put("code", "글을 수정하지 못 하였습니다.");
			return result;
		}		
		
		result.put("code", 200);
		result.put("result", "글을 수정하였습니다.");
		
		return result;	
	}

}
