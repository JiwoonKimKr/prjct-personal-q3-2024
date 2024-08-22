package com.givemetreat.user;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.givemetreat.pet.domain.AdminPetVO;
import com.givemetreat.user.bo.AdminUserBO;
import com.givemetreat.user.domain.AdminUserVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Admin User Controller", description = "[Admin] User Controller")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/user")
@Controller
public class AdminUserController {
	private final AdminUserBO adminUserBO;

	@GetMapping("/user-detail-view")
	@Operation(summary = "사용자 상세 조회", description = "사용자 관련 정보로 조회 가능.페이징 구현된 값을 응답")
	@Parameters({
			@Parameter(name = "userId", description = "사용자PK", example="1")
			, @Parameter(name = "loginId", description = "로그인 아이디", example="asdf")
			, @Parameter(name = "nickname", description = "사용자 닉네임", example="butler")
			, @Parameter(name = "selfDesc", description = "자기 소개", example="저는 강아지와 함께 지내는 집사입니다.")
			, @Parameter(name = "createdAtSince", description = "가입시간 조회 출발시점", example="2024-08-01T06:30:45")
			, @Parameter(name = "createdAtUntil", description = "가입시간 조회 종료시점", example="2024-08-01T06:30:45")
			, @Parameter(name = "updatedAtSince", description = "수정시간 조회 종료시점", example="2024-08-01T06:30:45")
			, @Parameter(name = "updatedAtUntil", description = "수정시간 조회 종료시점", example="2024-08-01T06:30:45")
			, @Parameter(name = "page", description = "페이징 관련 페이지 숫자", example="1")
			, @Parameter(name = "size", description = "페이징 전체 크기", example="5")
	})
	public String userDetailView( @RequestParam(required = false) Integer userId
								, @RequestParam(required = false) String loginId
								, @RequestParam(required = false) String nickname
								, @RequestParam(required = false) String selfDesc
								, @RequestParam(required = false) LocalDateTime createdAtSince
								, @RequestParam(required = false) LocalDateTime createdAtUntil
								, @RequestParam(required = false) LocalDateTime updatedAtSince
								, @RequestParam(required = false) LocalDateTime updatedAtUntil
								, @RequestParam(required = false) Integer page			
								, @RequestParam(required = false) Integer size			
								, Model model) {
		
		Page<AdminUserVO> pageInfo = adminUserBO.getListUserVOsForPaging(userId
																		, loginId
																		, nickname
																		, selfDesc
																		, createdAtSince
																		, createdAtUntil
																		, updatedAtSince
																		, updatedAtUntil
																		, page
																		, size);
		
		if(ObjectUtils.isEmpty(pageInfo) || ObjectUtils.isEmpty(pageInfo.getContent())) {
			log.info("[AdminUserController userDetailView()] current data get null result. pageInfo:{}", pageInfo);
			model.addAttribute("error_message", "해당 결과를 찾지 못 하였습니다.");	
			return "admin/user/userDetail";
		}
		
		List<AdminUserVO> listUsers = pageInfo.getContent();
		Integer totalPages = pageInfo.getTotalPages();
		Integer pageCurrent = pageInfo.getNumber();
		Integer sizeCurrent = pageInfo.getSize();
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("pageCurrent", pageCurrent);
		model.addAttribute("sizeCurrent", sizeCurrent);
		return "/admin/user/userDetail";
	}
	
	@GetMapping("/user-pet-detail-view")
	public String userPetDetailView(@RequestParam int userId
									, @RequestParam int petId
									, Model model) {
		
		AdminPetVO petVO = adminUserBO.getPetByUserIdAndPetId(userId, petId);
		
		model.addAttribute("pet", petVO);
		return "/admin/user/userPetDetail";
	}
	
}
