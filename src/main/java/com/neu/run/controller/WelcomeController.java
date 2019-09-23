package com.neu.run.controller;

import java.util.List;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.neu.run.dao.ProductDAO;
import com.neu.run.dao.SellerDAO;
import com.neu.run.pojo.Seller;
import com.neu.run.pojo.User;
import com.neu.run.validator.SellerValidator;
import com.neu.run.pojo.Product;

@Controller
public class WelcomeController {


	
	@RequestMapping(value = "/Seller/register", method = RequestMethod.POST)
	protected ModelAndView registerNewSupplier(HttpServletRequest request,  @ModelAttribute("seller") Seller seller, BindingResult result,SellerDAO sellerDAO,SellerValidator sellerValidator) throws Exception {
		
       sellerValidator.validate(seller, result);
		
		if (result.hasErrors()) {
			return new ModelAndView("Seller-register", "seller", seller);
		}
		HttpSession session = (HttpSession) request.getSession();

		Seller s = new Seller();
		s.setCompany(request.getParameter("company"));

		s.setUsername(request.getParameter("username"));
		s.setPassword(request.getParameter("password"));
		s.setEmail(request.getParameter("email"));
		s.setRole("seller");
		Seller sel = sellerDAO.register(s);
		session.setAttribute("user", sel);
		
	     Email email = new SimpleEmail();
         email.setHostName("smtp.googlemail.com");
         email.setSmtpPort(465);
         email.setAuthenticator(new DefaultAuthenticator("finalproj94@gmail.com", "lionelmessi10"));
         email.setSSLOnConnect(true);
         email.setFrom("finalproj94@gmail.comm");
         email.setSubject("Registration");
         email.setMsg("You have successfully registered as Seller. Please visit the store to add all the products");
         email.addTo(s.getEmail());
         email.send();
		return new ModelAndView("welcomeseller", "seller", new Seller());
	}
	
	@RequestMapping(value = "/welcome/seller", method = RequestMethod.POST)
	protected ModelAndView welcomeSeller(HttpServletRequest request,HttpSession session, ProductDAO productDao) {
		//HttpSession session = request.getSession(true);
		String selection = request.getParameter("option");
		session.setAttribute("selection", selection);
		if (selection.equals("add")) {

			return new ModelAndView("createProductSeller", "selleradd", new Product());
		} else {
			
			//Seller seller = (Seller)session.getAttribute("seller");
	      User user = (User) session.getAttribute("user");
	        Long userId = user.getUserId();
	        List<Product> product = productDao.productListById(userId);
	 	
			return new ModelAndView("viewProductSeller", "approvedProducts", product);

		}
	}

	@RequestMapping(value = "/HomeTo/Product")
    protected ModelAndView availableProducts(HttpServletRequest request, ProductDAO productDAO) {
 
		HttpSession session = request.getSession(true);
		List<Product> product = productDAO.approvedProducts();
		return new ModelAndView("listofProducts","availableProducts",product);
	}
	
	
}
