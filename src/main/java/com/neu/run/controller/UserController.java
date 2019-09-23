package com.neu.run.controller;

import java.util.List;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.neu.run.pojo.Seller;
import com.neu.run.pojo.User;
import com.neu.run.pojo.Customer;
import com.neu.run.pojo.Product;
import com.neu.run.dao.ProductDAO;
import com.neu.run.dao.SellerDAO;
import com.neu.run.dao.UserDAO;
import com.neu.run.exception.UserException;
import com.neu.run.dao.CustomerDAO;

@Controller
public class UserController {

	@RequestMapping(value="/Seller/login/home")
	protected String Style(HttpServletRequest request) throws Exception {
		return "Seller-login";
	}
	
	@RequestMapping(value="/Style/Product", method = RequestMethod.POST)
	protected String loginSeller(HttpServletRequest request) throws Exception {
		return "style";
	}
	
	@RequestMapping(value="/Seller/login", method = RequestMethod.POST)
	protected String sellerlogin(HttpServletRequest request, UserDAO userDAO, SellerDAO sellerDAO) throws Exception {
		HttpSession session = request.getSession();
		try {
			
			
			User u = userDAO.get(request.getParameter("username"), request.getParameter("password"),
					request.getParameter("email"), request.getParameter("role"));
			session.setAttribute("user", u);
			return  "welcomeseller";
			
			} catch(UserException e ) {
			System.out.println("Exception: " + e.getMessage());
			return "home";
			}
		}		
	
	@RequestMapping(value="/Admin/login")
	protected String loginAdmin(HttpServletRequest request) throws Exception {
		String value = request.getParameter("admin");
		
		return "Admin-login";
	}
	
	@RequestMapping(value="/Admin/register", method = RequestMethod.GET)
	protected ModelAndView registerAdmin(HttpServletRequest request) throws Exception {
		String value = request.getParameter("register");
		return new ModelAndView("Admin-register","admin", new User());
	}
	
	@RequestMapping(value="/Seller/register", method = RequestMethod.GET)
	protected ModelAndView registerSeller(HttpServletRequest request, @ModelAttribute("seller") Seller seller) throws Exception {
		String value = request.getParameter("registerS");
		return new ModelAndView("Seller-register","s", new Seller());
	}
	
	@RequestMapping(value = "/logout/", method = RequestMethod.POST)
    public String logout(HttpServletRequest request){
        
		HttpSession session = request.getSession();
        return "logout";
}
	
	@RequestMapping(value = "/logout/admin", method = RequestMethod.POST)
    public String logoutAdmin(HttpServletRequest request){
        
		HttpSession session = request.getSession();
        return "logout";

	}
	
	@RequestMapping(value = "Admin/welcome", method = RequestMethod.POST)
	protected ModelAndView welcomeAdmin(HttpServletRequest request, UserDAO userDAO, ProductDAO productDAO, @ModelAttribute("product")Product product ) {
		HttpSession session = request.getSession(true);
		
		try {
		User u = userDAO.get(request.getParameter("username"), request.getParameter("password"),
				request.getParameter("email"), request.getParameter("role"));
		session.setAttribute("user", u);
	
			List<Product> p = productDAO.productsForAdmin();

			return new ModelAndView("welcomeAdmin", "viewRequest", p);
		
		} catch(UserException e ) {
		System.out.println("Exception: " + e.getMessage());
		return new ModelAndView("Admin-login");
		}
	}		
	
	@RequestMapping(value = "/Seller/home", method = RequestMethod.POST) 
	  public String homepage(HttpServletRequest request) {
 
		return "home";
		
	}
	
	@RequestMapping(value = "/Customer/welcome", method = RequestMethod.POST )
	 protected String welcomeCustomer(HttpServletRequest request, UserDAO userDAO, ProductDAO productDAO) {
		HttpSession session = request.getSession(true);
		
		try {
			User u = userDAO.get(request.getParameter("username"), request.getParameter("password"),
					request.getParameter("email"), request.getParameter("role"));
			session.setAttribute("user", u);
		    return "listofProducts";
		    
		    
		} catch (UserException e) {
			System.out.println("Exception: " + e.getMessage());
			session.setAttribute("errorMessage", "error while login");
			return "error";
		}
	}
		
	 @RequestMapping(value = "/Customer/register", method = RequestMethod.GET)
	  protected ModelAndView customeRegister(HttpServletRequest request, @ModelAttribute("custregister")Customer customer, CustomerDAO customerDAO) throws EmailException {
		
		HttpSession session = request.getSession();
		Customer c = new Customer();
		c.setFirstName(request.getParameter("firstName"));
		c.setLastName(request.getParameter("lastName"));
	    c.setRole(request.getParameter("customer"));
	    c.setEmail(request.getParameter("email"));
	    c.setPassword(request.getParameter("password"));
	    Customer cus = customerDAO.register(c);	
	    session.setAttribute("user", cus);
	    
	    Email email = new SimpleEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("finalproj94@gmail.com", "lionelmessi10"));
        email.setSSLOnConnect(true);
        email.setFrom("finalproj94@gmail.comm");
        email.setSubject("Registration");
        email.setMsg("You have successfully registered as Customer. Please visit the store for the purchase");
        email.addTo(c.getEmail());
        email.send();
	    
	    return new ModelAndView("Customer-register", "custregister", c);
		
	}		
	
      @RequestMapping(value = "/Customer/products", method = RequestMethod.POST)
        protected ModelAndView productsForCust(HttpServletRequest request, ProductDAO productDAO, UserDAO userDAO) {
       
       HttpSession session = request.getSession(true);
  	   User user = (User) session.getAttribute("user");
       Long userId = user.getUserId();
       List<Product> product = productDAO.productListById(userId);
		return new ModelAndView("listofProducts","availableProducts",product);
      }
}
	
		




