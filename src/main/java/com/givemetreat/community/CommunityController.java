package com.givemetreat.community;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.givemetreat.commentCommunity.domain.CommentCommunityVO;
import com.givemetreat.community.bo.CommunityBO;
import com.givemetreat.postCommunity.domain.PostCommunityVO;
import com.givemetreat.productShoppingCart.domain.ProductShoppingCartVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Tag(name = "Community Controller", description = "[Client] Community Controller 커뮤니티 페이지 관련 컨트롤러")
@RequiredArgsConstructor
@RequestMapping("/community")
@Controller
public class CommunityController {
	private final CommunityBO communityBO;

	@Operation(summary = "communityMainView() 커뮤니티 메인페이지", description = "커뮤니티 메인페이지")
	@Parameters({
		@Parameter(name = "<String> keyword", description = "(비필수 변수) 검색 키워드 ", example = "포리_간식_강아지")
		, @Parameter(name = "<String> agePetProper", description = "(비필수 변수)섭취 적정 연령(enum type); <product>의 category Column과 동일 ", example = "under6Months")
		, @Parameter(name = "<HttpSession> session", description = "session")
		, @Parameter(name = "<Model> model", description = "MVC Model")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "403", description = "redirect:user/sign-in-view")
		, @ApiResponse(responseCode = "200", description = "/community/communityMain.html"
																	+"<br> Model Attributtes"
																	+"<br>, List&lt;PostCommunityVO&gt; \"listPosts\""
		, content = @Content(mediaType = "TEXT_HTML", schema = @Schema(implementation = ProductShoppingCartVO.class)))
	})
	@GetMapping("/community-main-view")
	public String communityMainView(@RequestParam(required = false) String keyword
									, @RequestParam(required = false) String agePetProper
									, HttpSession session
									, Model model) {
		Integer userId = (Integer) session.getAttribute("userId");
		if(userId == null) {
			return "redirect:/user/sign-in-view";
		}
		
		if(keyword != null) {
			List<PostCommunityVO> listPosts = communityBO.getPostsByKeyword(keyword);
			model.addAttribute("listPosts", listPosts);
		
			return "community/communityMain";
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
	
	@Operation(summary = "postDetailView() 커뮤니티 게시글 상세 조회 페이지", description = "커뮤니티 게시글 상세 조회 페이지")
	@Parameters({
		@Parameter(name = "<int> postId", description = "[PathVariable] 커뮤니티 글(post) PK", example = "10")
		, @Parameter(name = "<HttpSession> session", description = "session")
		, @Parameter(name = "<Model> model", description = "MVC Model")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "403", description = "redirect:user/sign-in-view")
		, @ApiResponse(responseCode = "200", description = "/community/postDetail.html"
																	+"<br> Model Attributtes"
																	+"<br>, &lt;PostCommunityVO&gt; \"post\""
																	+"<br>, List&lt;CommentCommunityVO&gt; \"listComments\""
		, content = @Content(mediaType = "TEXT_HTML", schema = @Schema(allOf = {PostCommunityVO.class, CommentCommunityVO.class})))
	})
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
