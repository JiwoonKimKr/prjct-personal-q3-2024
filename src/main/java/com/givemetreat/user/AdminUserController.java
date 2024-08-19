package com.givemetreat.user;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.givemetreat.pet.domain.AdminPetVO;
import com.givemetreat.user.bo.AdminUserBO;
import com.givemetreat.user.domain.AdminUserVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/admin/user")
@Controller
public class AdminUserController {
	private final AdminUserBO adminUserBO;

	@GetMapping("/user-detail-view")
	public String userDetailView( @RequestParam(required = false) Integer userId
								, @RequestParam(required = false) String loginId
								, @RequestParam(required = false) String nickname
								, @RequestParam(required = false) String selfDesc
								, @RequestParam(required = false) LocalDateTime createdAt
								, @RequestParam(required = false) LocalDateTime updatedAt			
								, @RequestParam(required = false) Integer page			
								, @RequestParam(required = false) Integer size			
								, Model model) {
		
		Page<AdminUserVO> pageInfo = adminUserBO.getListUserVOsForPaging(userId
																		, loginId
																		, nickname
																		, selfDesc
																		, createdAt
																		, updatedAt
																		, page
																		, size);
		
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
