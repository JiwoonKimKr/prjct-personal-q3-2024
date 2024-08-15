package com.givemetreat.community;

import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.givemetreat.commentCommunity.domain.CommentCommunityVO;
import com.givemetreat.community.bo.CommunityBO;
import com.givemetreat.postCommunity.domain.PostCommunityVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/community")
@Controller
public class CommunityController {
	private final CommunityBO communityBO;

	@GetMapping("/community-main-view")
	public String communityMainView(HttpSession session
									, @RequestParam(required = false) String agePetProper
									, Model model) {
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null) {
			return "redirect:user/sign-in-view";
		}
		
		if(agePetProper != null) {
			List<PostCommunityVO> listPosts = communityBO.getPostsByAgePetProperLimit20(agePetProper);
			model.addAttribute("listPosts", listPosts);
		
			return "community/communityMain";
		}
		
		List<PostCommunityVO> listPosts = communityBO.getPostsLatestTop20();
		model.addAttribute("listPosts", listPosts);
		
		return "community/communityMain";
	}
	
	@GetMapping("/post/{postId}")
	public String postDetailView(@PathVariable int postId
								, HttpSession session
			                    , Model model) {
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null) {
			return "redirect:user/sign-in-view";
		}
		PostCommunityVO post = communityBO.getPostByPostId(postId);
		List<CommentCommunityVO> listComments = communityBO.getCommentsByPostId(postId);
		
		model.addAttribute("post", post);
		model.addAttribute("listComments", listComments);
		
		return "community/postDetail";
	}
	
}
