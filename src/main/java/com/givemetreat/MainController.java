package com.givemetreat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	@GetMapping("/")
	public String initialIndexView() {
		return "redirect:product/product-list-view";
	}
}
