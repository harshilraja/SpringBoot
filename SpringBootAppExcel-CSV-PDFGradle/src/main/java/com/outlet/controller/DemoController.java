package com.outlet.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoController {

	@RequestMapping("/welcome")
	public String welcome(Map<String, Object> model) {
		return "welcome";
	}
}