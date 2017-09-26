package com.sky;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * user控制器
 * 
 * @作者 乐此不彼
 * @时间 2017年8月31日
 * @公司 sky工作室
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@RequestMapping("/info")
	public Principal user(Principal user) {
		System.err.println("访问了/api/user/info接口");
		return user;
	}
	
	@GetMapping("/say")
	public String say(String msg) {
		System.err.println("访问了/api/user/say接口,传入参数msg="+msg);
		return String.format("Hello %s !!! ", msg);
	}
}
