package com.pranjal.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.pranjal.dao.UserDao;
import com.pranjal.helper.Message;
import com.pranjal.model.User;

@Controller
public class RegisterController {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	//handler for user registration
	
	@PostMapping("/do-register")
	public String registerUser( @Valid @ModelAttribute("user") User user,  BindingResult bindingResult,  
			  Model model,  HttpSession session,
			 @RequestParam(value = "agreement", defaultValue = "false") boolean agreement) {
		
		try {
			if( !agreement) {
				System.out.println("You have not agreed terms & conditions");
				throw new Exception("You have not agreed terms & conditions");
			}
			
			if(bindingResult.hasErrors()) {
				System.out.println("Error " +bindingResult.toString());
				model.addAttribute("user", user);
				return "register"; //register.html
			}
			
//			System.out.println("agreement  " +agreement);
//			System.out.println("user  " +user);
//			System.out.println("model  " +model);
			
			// name, email, password,about coming from registration form.  id is auto generated
			// set remaining user fields
			user.setEnabled(true);
			user.setRole("USER");
			user.setImgUrl("user.png");
			
			//encrypt password
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			//Add user in DB
			this.userDao.save(user);
			
			model.addAttribute("user", new User()); //new User()  means after saving user blank registration form displayed
			session.setAttribute("message", new Message("Successfully register  " ,"success"));
			return "register"; //register.html
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong " +e.getMessage(), "alert-danger"));
			return "register"; //register.html
		}			
	}

}
