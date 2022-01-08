package com.pranjal.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.pranjal.dao.ContactDao;
import com.pranjal.dao.UserDao;
import com.pranjal.helper.Message;
import com.pranjal.model.Contact;
import com.pranjal.model.User;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ContactDao contactDao;
	
	@Autowired
	public BCryptPasswordEncoder bCryptPasswordEncoder;
		
	//common method for all handlers @ModelAttribute
	@ModelAttribute
	private void addCommonData(Model model, Principal principal) {
		 String userName = principal.getName();  //gives username of logged in user
		 System.out.println("userName :"+userName);
		  
		  User user = this.userDao.getUserByUserName(userName);
		  //get the user using username (username is email here) 
		  
		  System.out.println("USER :"+user);
		  model.addAttribute(user);
	}
		
	// user dashboard 
	@RequestMapping("/index")  //user/index
	public String dashboard(Model model, Principal principal) {
		model.addAttribute("title", "user dashboard " ); //page title     		
		return "user/user-dashboard";	 // user/user-dashboard.html  	
	}

	//handler for open add-contact-form
	@GetMapping("/add-contact")  //user/add-contact
	public String openAddContactForm( Model model) {		
		model.addAttribute("title", "Add contact" ); //page title
		model.addAttribute("contact", new Contact());		
		return "user/add-contact-form";  
	}
	
	//handler for save contact in DB
	@PostMapping("/process-contact")   //user/process-contact
	public String addContact(@ModelAttribute Contact contact , @RequestParam("profileImage") MultipartFile file,
			Principal principal , HttpSession session) {
	     	
	    	/* Contact contact  ===>  for contact details excluding image
	     	 * @RequestParam("profileImage") MultipartFile file  ===> for file (image)
	     	 * profileImage must be same as form's image field name attribute 
	     	 * Principal principal ===> to get user name 
	     	 * HttpSession session ===> display success and error message to user using HttpSession
	     	*/
		
			  try {
					
					  String userName = principal.getName(); //gives username of logged in user
					  System.out.println("userName :"+userName); 
					 
					  User user = this.userDao.getUserByUserName(userName);
					  System.out.println("User without contact :" +user);
					 				  
				  //processing and uploading file
				  if(file.isEmpty()) {
					  //give message to user
					  System.out.println("File must not be empty");
					
					 contact.setImg("contact.jpg");    //default image for contact
					  
					  //throw new Exception();
					  
				  }else {
					  //// add the file(image) to the folder and update the name to contact 
					 				  
				    	//steps to serve resources dyamically 
						//path of folder where we can upload images
					    //step 1
						 final String UPLOAD_DIR =new ClassPathResource("static/img").getFile().getAbsolutePath() ; 
						
						 //step 2
						 //update image
						//new API to upload file in folder
							Files.copy(file.getInputStream() ,
									       Paths.get( UPLOAD_DIR +File.separator + file.getOriginalFilename()) , 
									       StandardCopyOption.REPLACE_EXISTING);
							
							//step 3		
				            //update the image name of contact in DB after uploading new image
							// contact.setImg(file.getOriginalFilename());
																		
						System.out.println("Image is uploaded");
				  }
				
					/*  save contact in DB
					 * //bidirectional mapping of tables // set user to contact table //
					 * contact.setUser(user);
					 * 
					 * //add contact in user's list of contacts (List<Contact> contact) boolean add
					 * = user.getContact().add(contact);
					 * 
					 * System.out.println("Is Contact added in User ? " +add);
					 * System.out.println("Contact Data" +contact);
					 * System.out.println("Contact added successfully");
					 * 
					 * User updatedUserWithContact = this.userDao.save(user); // save user in DB
					 * System.out.println("updated user with contact:  " +updatedUserWithContact );
					 */
				
				 System.out.println(contact);
				
				 // add the image name of contact in DB after uploading image
				contact.setImg(file.getOriginalFilename());
				  				  
				  //save contact in DB
			    this.contactDao.save(contact);
				
				//success message
				session.setAttribute("message", new Message("Contact Added Successfully", "success"));
													
			} catch (Exception e) {
				
			    System.out.println("Error" +e.getMessage());
				e.printStackTrace();
				
				//error message
				session.setAttribute("message", new Message("Somethig went wrong ! Try again...", "danger"));						
			}	
	
			  return "user/add-contact-form";
	}

	
	//handler for view contact without pagination	
	@GetMapping("/show-contacts")
	public String showContact(Model model) {
				
					/* //handler for view contact with  pagination
					 * pagination shows no of contacts per page = [n] 
					 * and current page = [currentPage] //
					 * 
					 *
					 * @GetMapping("/show-contacts/{currentPage}")
					 * public String showContact(@PathVariable("currentPage") Integer page ,Model model, Principal principal) {
					 * 
					 *  //username of logged in user, (email here)
					 * String userName = principal.getName(); 
					 * User user = this.userDao.getUserByUserName(userName);
					 * 
					 *  // pagination
					 *  // creating  pageable object
					 *  //we can store child in parent's variable
					 *  //showing 5 contacts per page
					 *  
					 *   Pageable pageable = PageRequest.of(page, 5);
					 * 
					 *  //fetching list of contacts by userID(email here) from DB
					 * Page<Contact> contacts =this.contactDao.findContactsByUser(user.getId(), pageable );
					 * 
					 * //sending list of contacts to the view
					 * model.addAttribute("contacts" , contacts);
					 * 
					 * model.addAttribute("currentPage", currentPage);
					 * model.addAttribute("totalPages", contacts.getTotalPages());
					 * 
					 * return "user/show-contact";
					 * }
					 */
		
		//page title
		model.addAttribute("title", "View Contacts");
		 	
		 //finding all contacts
		 List<Contact> contacts = this.contactDao.findAll();
				 
		//sending list of contacts to the view
		model.addAttribute("contacts" , contacts);
		System.out.println(contacts);
						
		return "user/show-contact";
	}

	//handle for showing particular contact details
	@GetMapping("/{cid}/contact")
	public String showContactDetails(@PathVariable("cid") Integer cid , Model model) {
		System.out.println("cid " +cid);
	    		
		Optional<Contact> contactOptional = this.contactDao.findById(cid);
		Contact contact = contactOptional.get();
		System.out.println(contact);
		
		/*//handle for showing particular contact details
	      @GetMapping("/{cid}/contact")
	      public String showContactDetails(@PathVariable("cid") Integer cid , Model model , Principal principal) {
     	  System.out.println("cid " +cid);
		 * 
		 * //check for authorization 
		 * String userName = principal.getName();
		 * User user = this.userDao.getUserByUserName(userName);
		 * if(user.getId() ==  contact.getUser().getId()) {
		 *    model.addAttribute("contact",contact);
		 *  }
		 */
		
		//page title
		model.addAttribute("title", contact.getName() +" contact details");
		model.addAttribute("contact",contact);
		
		return "user/contact-details";
	}
	
	//handler to delete contact by id
	@GetMapping("/delete/{cid}")
	public String delete(@PathVariable("cid") Integer cid , Model model, HttpSession session) {
		//to receive dynamic value of url in method we use @PathVariable
		
		/*
		 * Optional<Contact> optionalContact = this.contactDao.findById(cid); 
		 * Contact contact =optionalContact.get();
		 * 
		 * or we can write as
		 * Contact contact = this.contactDao.findById(cid).get();Contact contact = this.contactDao.findById(cid).get();
		 */
		
		Contact contact = this.contactDao.findById(cid).get();
		
		//to unlink user with contact
		//contact.setUser(null);
		
		//remove contact image
		//path= image path + contact.getImg();
		//remove path
		
		//delete contact
		this.contactDao.delete(contact);
		
		//to pass message we can use HttpSession session
		session.setAttribute("message", new Message("contact delete successfully", "success"));
		
		return "redirect:/user/show-contacts";		
	}
	
	//handler for open update-form.html page
	@PostMapping("/update-contact/{cid}")
	public String updateForm(Model model, @PathVariable("cid") Integer cid) {
		
		model.addAttribute("title" , "Update Contact");
	 
		//first, fetch contact data from DB
		Contact  contact = this.contactDao.findById(cid).get();
		
		//Pass contact data to view
		model.addAttribute("contact",contact);
				
		return "user/update-form";
	}
	
	//handler for processing update-form.html  data and updating contact into DB
	//@RequestMapping(value = "/process-update/{cid}", method = RequestMethod.POST)
	@PostMapping("/process-update/{cid}")
	public String updateHandler(@PathVariable("cid") Integer cid, Principal principal,
			@ModelAttribute Contact contact , @RequestParam("profileImage") MultipartFile file,  HttpSession session) {
		
		Contact oldContactDetails = this.contactDao.findById(contact.getCid()).get();
		System.out.println("oldContactDetails " +oldContactDetails);
				
		try {
			if(!file.isEmpty()) {
				//if file is not empty then do file work
				//rewrite file
				
				//delete old image
				File deletefile = new ClassPathResource("static/img").getFile();
				File file1=new File(deletefile, oldContactDetails.getImg());
				file1.delete();
				
				
				//steps to  update new image
				//path of folder where we can upload images
				//step 1
				 final String UPLOAD_DIR =new ClassPathResource("static/img").getFile().getAbsolutePath() ; 
				
				 //step 2
				 //update image
				//new API to upload file in folder
					Files.copy(file.getInputStream() ,
							       Paths.get( UPLOAD_DIR +File.separator + file.getOriginalFilename()) , 
							       StandardCopyOption.REPLACE_EXISTING);
					//step 3		
		            //update the image name of contact in DB after uploading new image
					 contact.setImg(file.getOriginalFilename());
					 
			System.out.println("Image is updated");
				
			} else {
				//take old image as it is
				contact.setImg(oldContactDetails.getImg());
			}
			
			/*  getting  java.lang.StackOverflowError at this line
			 * User user = this.userDao.getUserByUserName(principal.getName());
			 * contact.setUser(user);
			 */
						 
			//update contact in DB
			this.contactDao.save(contact);
			System.out.println("Updated CONTACT " +contact);
			
			//success message
			session.setAttribute("message", new Message("Contact Update Successfully", "success"));
			
		} catch (Exception e) {
			//error message
			session.setAttribute("message", new Message("Somethig went wrong ! Try again...", "danger"));		
			
			e.printStackTrace();
		}
				
		return "redirect:/user/"+contact.getCid()+"/contact";	
	}
	
	   //profile
		@GetMapping("/profile")
		public String profile(Model model)
		{
			model.addAttribute("title", "Profile Page");			
			return "user/profile";
		}
		
		//go to setting.html page 
		@GetMapping("/setting")
		public String settings(Model model)
		{
			model.addAttribute("title", "Setting");
			return "user/setting";
		}
	
		//change password
		@PostMapping("/change-password")
		public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword")String newPassword,Principal principal,HttpSession session)
		{
			System.out.println(oldPassword+" "+newPassword);
			
			String name = principal.getName(); //user name mil gaya
			User currentuser = userDao.getUserByUserName(name); //user repo ko fetch kar sakte hai 
			System.out.println(currentuser.getPassword());
			
			if(bCryptPasswordEncoder.matches(oldPassword, currentuser.getPassword()))
			{
				//change the password
				currentuser.setPassword(bCryptPasswordEncoder.encode(newPassword));
				userDao.save(currentuser);
				session.setAttribute("message", new Message("Password Successfully changed", "success"));
				return "redirect:/user/setting";
			}
			else {
				session.setAttribute("message", new Message("Wrong passworld - Old !!", "danger"));
				return "redirect:/user/setting";
			}
			
		}
			
}
