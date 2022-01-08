package com.pranjal.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pranjal.model.Contact;

public interface ContactDao extends JpaRepository<Contact, Integer> {

	/*
	 * //pagination 
	 * //fetch contact by user Id
	 * 
	 * @Query("from Contact as c where c.user.id =:user" ) 
	 * public Page<Contact>findContactsByUser( @Param("user") int userId , Pageable pageable);
	 * 
	 * pageable having two information about page
	 * current page & 
	 * no of contacts per page
	 */

	// fetch contact by user Id
	@Query("from Contact as c where c.user.id =:user")
	public List<Contact> findContactsByUser(@Param("user") int userId);

}
