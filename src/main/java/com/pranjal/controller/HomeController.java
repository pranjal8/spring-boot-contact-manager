package com.pranjal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.pranjal.model.User;

@Controller
public class HomeController {

	@GetMapping("/")
	public String home() {
		return "home"; //home.html
	}
	@GetMapping("/about")
	public String about(Model m) {
		m.addAttribute("title" , "About- smart contact manager");
		return "about"; //about.html
	}
	@GetMapping("/signup")
	public String signup(Model m) {
		m.addAttribute("title" , "Signup- smart contact manager");
		m.addAttribute("user", new User());
	
		return "register"; //register.html
		
	}
	
	//handler for custom login
	@GetMapping("/signin")
	public String customLogin(Model m) {
       
		m.addAttribute("title" , "Login- smart contact manager");
		
		return "login";  //login.html		
	}
	
}
