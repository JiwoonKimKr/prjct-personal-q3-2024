package com.givemetreat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "MainController", description = "메인 컨트롤러")
public class MainController {
	
	@GetMapping("/")
	@Operation(summary = "초기 진입 화면", description = "로그인 불필요, product/product-list-view로 리다이렉트")
	public String initialIndexView() {
		return "redirect:product/product-list-view";
	}
}
