package com.givemetreat.test;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
//	@Autowired
//	private testMapper testMapper;
	
	@ResponseBody
	@GetMapping("/test1")
	public String text1() {
		return "Hello World";
	}
	
	@ResponseBody
	@GetMapping("/test2")
	public Map<String, Object> text2() {
		Map<String, Object> map = new HashMap<>();
		map.put("a", 0);
		map.put("b", 1);
		map.put("c", 2);
		return map;
	}	
	
	@GetMapping("/test3")
	public String test3() {
		// templates/test/test3.html
		return "test/test3";
	}
	
//	@ResponseBody
//	@GetMapping("/test4")
//	public Product test4() {
//		return testMapper.getTestFirstDummy();
//	}
}
