package com.givemetreat.user;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
								, Model model) {
		
		List<AdminUserVO> listUsers = adminUserBO.getListUserVOs(userId, loginId, nickname, selfDesc, createdAt, updatedAt);
		
		model.addAttribute("listUsers", listUsers);
		return "/admin/user/userDetail";
	}
}
