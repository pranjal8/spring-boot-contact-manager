package com.pranjal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.pranjal.dao.UserDao;
import com.pranjal.model.User;

public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// fetching Data from DB
		
		User user= userDao.getUserByUserName(username);
		
		if(user == null) {
			throw new UsernameNotFoundException("User Not found");
		}
		
		CustomUserDetail customUserDetail =new CustomUserDetail(user);
		
		return customUserDetail ;
	}

}
