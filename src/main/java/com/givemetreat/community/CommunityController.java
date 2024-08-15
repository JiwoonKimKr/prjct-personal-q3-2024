package com.givemetreat.community;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/community")
@Controller
public class CommunityController {

	@GetMapping("/community-main-view")
	public String communityMainView() {
		return "community/communityMain";
	}
}
