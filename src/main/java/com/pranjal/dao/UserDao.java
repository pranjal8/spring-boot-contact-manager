package com.pranjal.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pranjal.model.User;

public interface UserDao extends JpaRepository<User, Integer> {
	
	@Query("select u from User u where u.email = :email ")
	public User getUserByUserName(@Param("email") String email );
    // @Param("email") for dynamic value of email
		
}
