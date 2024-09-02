package com.givemetreat.community;

import java.util.*;

import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.givemetreat.commentCommunity.domain.CommentCommunityEntity;
import com.givemetreat.community.bo.CommunityBO;
import com.givemetreat.postCommunity.domain.PostCommunityEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
@Tag(name = "Community Rest Controller", description = "[Client] Community RestAPI Controller; 커뮤니티 페이지 관련 RestAPI컨트롤러")
@RequiredArgsConstructor
@RequestMapping("/community")
@RestController
public class CommunityRestController {
	private final CommunityBO communityBO;

	@Operation(summary = "writeNewPost() 새 글 작성하기", description = "새 글 작성하기")
	@Parameters({
		@Parameter(name = "<String> title", description = "제목", example = "오늘자 강아지를 모신 어느 집사의 하루 일과입니다:)")
		, @Parameter(name = "<String> content", description = "내용", example = "간식과 사료만큼은 좋은 걸 먹이고 싶어(●'◡'●)")
		, @Parameter(name = "<String> agePetProper", description = "(비필수 변수)섭취 적정 연령(enum type); <product>의 category Column과 동일 ", example = "under6Months")
		, @Parameter(name = "<HttpSession> session", description = "session")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "403", description = "error_message: \"로그인 후 커뮤니티 글 작성이 가능합니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500", description = "error_message: \"새 글 쓰기 시도가 실패하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"새로운 글을 작성하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
	})
	@PostMapping("/write-new-post")
	public Map<String, Object> writeNewPost(@RequestParam String title
											, @RequestParam String content
											, @RequestParam(required = false) String agePetProper
											, HttpSession session){
		
		Map<String, Object> result = new HashMap<>();
		Integer userId = (Integer) session.getAttribute("userId");
		if(ObjectUtils.isEmpty(userId)) {
			result.put("code", 403);
			result.put("result", "로그인 후 커뮤니티 글 작성이 가능합니다.");
			return result;
		}
		
		PostCommunityEntity post = communityBO.addPost(userId, title, content, agePetProper);
		
		if(ObjectUtils.isEmpty(post)) {
			result.put("code", 500);
			result.put("result", "새 글 쓰기 시도가 실패하였습니다.");
			return result;
		}
		
		result.put("code", 200);
		result.put("result", "새로운 글을 작성하였습니다.");
		
		return result;
	}
	
	@Operation(summary = "updateCurrentPost() 기존 글 수정하기", description = "글 수정하기")
	@Parameters({
		@Parameter(name = "<int> postId", description = "커뮤니티 글(post) PK", example = "10")
		, @Parameter(name = "<String> title", description = "제목", example = "오늘자 강아지를 모신 어느 집사의 하루 일과입니다:)")
		, @Parameter(name = "<String> content", description = "내용", example = "간식과 사료만큼은 좋은 걸 먹이고 싶어(●'◡'●)")
		, @Parameter(name = "<String> agePetProper", description = "(비필수) 섭취 적정 연령(enum type); <product>의 category Column과 동일 ", example = "under6Months")
		, @Parameter(name = "<HttpSession> session", description = "session")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "403", description = "error_message: \"로그인 후 커뮤니티 글 작성이 가능합니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500__1", description = "error_message: \"작성자 본인만 글을 수정할 수 있습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500__2", description = "error_message: \"글을 수정하지 못하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"글을 수정하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
	})
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
			result.put("result", "로그인 후 커뮤니티에 접근 가능합니다.");
			return result;
		}
		
		int userIdCurrentPost = communityBO.getPostByPostId(postId).getUserId();
		
		if(userId != userIdCurrentPost) {
			result.put("code", 500);
			result.put("result", "작성자 본인만 글을 수정할 수 있습니다.");
			return result;
		}
		
		PostCommunityEntity post = communityBO.updatePost(postId, userId, title, content, agePetProper);
		
		if(ObjectUtils.isEmpty(post)) {
			result.put("code", 500);
			result.put("result", "글을 수정하지 못 하였습니다.");
			return result;
		}
		
		
		result.put("code", 200);
		result.put("result", "글을 수정하였습니다.");
		
		return result;	
	}
	
	@Operation(summary = "deleteCurrentPost() 해당 글 삭제", description = "해당 글 삭제")
	@Parameters({
		@Parameter(name = "<int> postId", description = "커뮤니티 글(post) PK", example = "10")
		, @Parameter(name = "<HttpSession> session", description = "session")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "403", description = "error_message: \"로그인 후 커뮤니티 글 작성이 가능합니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500__1", description = "error_message: \"작성자 본인만 글을 삭제할 수 있습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500__2", description = "error_message: \"글을 삭제하지 못하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"글을 삭제하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
	})
	@DeleteMapping("/delete-current-post")
	public Map<String, Object> deleteCurrentPost(@RequestParam int postId
												, HttpSession session){
		Map<String, Object> result = new HashMap<>();
		Integer userId = (Integer) session.getAttribute("userId");
		if(ObjectUtils.isEmpty(userId)) {
			result.put("code", 403);
			result.put("result", "로그인 후 커뮤니티에 접근 가능합니다.");
			return result;
		}
		
		int userIdCurrentPost = communityBO.getPostByPostId(postId).getUserId();
		
		if(userId != userIdCurrentPost) {
			result.put("code", 500);
			result.put("code", "작성자 본인만 글을 삭제할 수 있습니다.");
			return result;
		}
		
		PostCommunityEntity post = communityBO.deletePostAndCommentsByPostIdAndUserId(postId, userId);
		
		if(ObjectUtils.isEmpty(post)) {
			result.put("code", 500);
			result.put("result", "글을 삭제하지 못하였습니다.");
			return result;
		}
		
		result.put("code", 200);
		result.put("result", "글을 삭제하였습니다.");
		
		return result;
	}
	
	@Operation(summary = "writeComment() 해당 글에 댓글 작성하기", description = "해당 글에 댓글 작성하기")
	@Parameters({
		@Parameter(name = "<int> postId", description = "커뮤니티 글(post) PK", example = "10")
		, @Parameter(name = "<String> content", description = "댓글 내용", example = "역시 강아지 보호자들 심정이 비슷한가 봐야.")
		, @Parameter(name = "<HttpSession> session", description = "session")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "403", description = "error_message: \"로그인 후 커뮤니티 글 작성이 가능합니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500", description = "error_message: \"댓글을 추가하지 못하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"댓글을 작성하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
	})
	@PostMapping("/write-comment")
	public Map<String, Object> writeComment(@RequestParam int postId
								,@RequestParam String content
								, HttpSession session) {
		Map<String, Object> result = new HashMap<>();
		Integer userId = (Integer) session.getAttribute("userId");
		if(ObjectUtils.isEmpty(userId)) {
			result.put("code", 403);
			result.put("result", "로그인 후 커뮤니티에 접근 가능합니다.");
			return result;
		}
		
		CommentCommunityEntity comment = communityBO.addComment(postId, userId, content);
		
		if(ObjectUtils.isEmpty(comment)) {
			result.put("code", 500);
			result.put("result", "댓글을 추가하지 못하였습니다.");
			return result;
		}
		
		result.put("code", 200);
		result.put("result", "댓글을 작성하였습니다.");
		
		return result;
	}
	
	@Operation(summary = "deleteCurrentComment() 해당 댓글 삭제", description = "해당 댓글 삭제")
	@Parameters({
		@Parameter(name = "<int> postId", description = "커뮤니티 글(post) PK", example = "10")
		, @Parameter(name = "<int> commentId", description = "댓글(comment) PK", example = "5")
		, @Parameter(name = "<HttpSession> session", description = "session")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "403", description = "error_message: \"로그인 후 커뮤니티 글 작성이 가능합니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500__1", description = "error_message: \"작성자 본인만 해당 댓글을 삭제할 수 있습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500__2", description = "error_message: \"댓글을 삭제하지 못하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"댓글을 삭제하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
	})
	@DeleteMapping("/delete-current-comment")
	public Map<String, Object> deleteCurrentComment(@RequestParam int postId
												, @RequestParam int commentId
												, HttpSession session){
		Map<String, Object> result = new HashMap<>();
		Integer userId = (Integer) session.getAttribute("userId");
		if(ObjectUtils.isEmpty(userId)) {
		result.put("code", 403);
		result.put("result", "로그인 후 커뮤니티에 접근 가능합니다.");
		return result;
		}
		
		int userIdCurrentComment = communityBO.getCommentByPostIdAndCommentId(postId, commentId).getUserId();
		
		if(userId != userIdCurrentComment) {
			result.put("code", 500);
			result.put("result", "작성자 본인만 해당 댓글을 삭제할 수 있습니다.");
			return result;
		}	
		
		CommentCommunityEntity comment = communityBO.deleteComment(postId, commentId, userId);
		
		if(ObjectUtils.isEmpty(comment)) {
		result.put("code", 500);
		result.put("result", "댓글을 삭제하지 못하였습니다.");
		return result;
		}
		result.put("code", 200);
		result.put("result", "댓글을 삭제하였습니다.");
		
		return result;
	}

}
