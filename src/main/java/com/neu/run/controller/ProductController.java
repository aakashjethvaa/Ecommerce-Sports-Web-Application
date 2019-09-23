package com.neu.run.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.neu.run.dao.CartDAO;
import com.neu.run.dao.CustomerDAO;
import com.neu.run.dao.OrderDAO;
import com.neu.run.dao.ProductCartDAO;
import com.neu.run.dao.ProductDAO;
import com.neu.run.dao.SellerDAO;
import com.neu.run.pojo.Customer;
import com.neu.run.pojo.Product;
import com.neu.run.pojo.Seller;
import com.neu.run.pojo.User;
import com.neu.run.pojo.Order;
import com.neu.run.pojo.PDF;
import com.neu.run.pojo.Cart;
import com.neu.run.pojo.ProductCart;


@Controller
public class ProductController {
	
	@RequestMapping(value="/product/create", method = RequestMethod.POST)
	   protected ModelAndView addproducts(HttpServletRequest request, @ModelAttribute("selleradd") Product product, SellerDAO sellerDAO, ProductDAO productDAO,BindingResult result) {
		
		if (result.hasErrors()) {
			return new ModelAndView("createProductSeller", "selleradd",product);
		}
		HttpSession session = (HttpSession)request.getSession();
        try {
		if (product.getFilename().trim() != "" || product.getFilename() != null) {
			File directory;
			String check = File.separator; 			
			String path = null;
			if (check.equalsIgnoreCase("\\")) {
				path = "C:\\Images";
				path += "/";				
			}

			if (check.equalsIgnoreCase("/")) {
				path = "C:\\Images";
				path += "/"; 
			}
			directory = new File(path + "\\" + product.getFilename());
			boolean temp = directory.exists();
			if (!temp) {
				temp = directory.mkdir();
			}
			if (temp) {
				// We need to transfer to a file
				CommonsMultipartFile photoInMemory = product.getPhoto();

				String fileName = photoInMemory.getOriginalFilename();
				// could generate file names as well

				File localFile = new File(directory.getPath(), fileName);

				// move the file from memory to the file

				photoInMemory.transferTo(localFile);
				product.setFilename(localFile.getPath());
				System.out.println(" File is stored at" + localFile.getPath());
				User user = (User) session.getAttribute("user");
			    	
				
				Long userId = user.getUserId();
				Seller seller = sellerDAO.getSeller(userId);
				product.setSeller(seller);
				Product p = productDAO.requestProduct(product);

			} else {
				System.out.println("Failed to create directory!");
			}
		}
   } catch (IllegalStateException e) { 
	System.out.println("*** IllegalStateException: " + e.getMessage());
   } catch (IOException e) {
	System.out.println("*** IOException: " + e.getMessage());
   }
     return new ModelAndView("success", "request", product);
	}

	@RequestMapping(value = "/approvedProducts/", method = RequestMethod.POST)
	protected ModelAndView requestApproved(HttpServletRequest request, ProductDAO productDAO) {
		
		String[] id = request.getParameterValues("id");

		String[] option = request.getParameterValues("option");
		for (int i = 0; i < id.length; i++) {

			productDAO.updateProduct(id[i], option[i]);

		}
		HttpSession session = request.getSession();
		List<Product> p = productDAO.approvedProducts();
		return new ModelAndView("approveProducts", "viewRequest", p);
	}	
	
	
	@RequestMapping(value ="/addtocart/Product", method = RequestMethod.POST)
	protected ModelAndView addtoCartProduct(HttpServletRequest request,@ModelAttribute("availabeProducts")Customer customer, CustomerDAO customerDAO,CartDAO cartDAO, ProductDAO productDAO,ProductCartDAO productCartDAO) {
		
		HttpSession session = request.getSession();
		if(session.getAttribute("user") == null) {
			return new ModelAndView("pleaseLogin");
		}
		else {
		User user = (User) session.getAttribute("user");
		Long userId = user.getUserId();
		customer = customerDAO.getCustomer(userId);
	     {
	    	String[] option = request.getParameterValues("option");
			String[] productId = request.getParameterValues("prodId");
			String[] price = request.getParameterValues("price");
			String[] quantity = request.getParameterValues("quantity");
			float totalPrice = 0;
			int totalItems = 0;
			for (int i = 0; i < productId.length; i++) {

				if (option[i].equals("yes")) {

					float prc = Float.parseFloat(price[i]);
					int qty = Integer.parseInt(quantity[i]);
					totalPrice += prc * qty;
					totalItems += qty;
				}
			}
			Cart c = new Cart();
			c.setCustomer(customer);
			c.setTotalItems(totalItems);
			c.setTotalPrice(totalPrice);
			Cart cart = cartDAO.saveToCart(c);

			Set<ProductCart> prodCart = new HashSet<ProductCart>();
			ProductCart pc = new ProductCart();

			for (int i = 0; i < productId.length; i++) {
				if (option[i].equals("yes")) {

					long prodId = Long.parseLong(productId[i]);
					int qty = Integer.parseInt(quantity[i]);

					Product pro = productDAO.getProduct(prodId);
					pc = new ProductCart();
					pc.setQuantity(qty);
					pc.setCart(cart);
					pc.setProduct(pro);
					ProductCart p = productCartDAO.save(pc);
					if (p != null)
						prodCart.add(p);
					
				}
			}

			cart = cartDAO.updateProdCart(cart.getId(), prodCart);

			return new ModelAndView("checkout", "cart", cart);
	    }
	
		}
	}
		
	@RequestMapping(value = "/order/generatePdf", method = RequestMethod.POST)
	protected ModelAndView generateOrder(@ModelAttribute("cart") Cart cart, ModelMap model, BindingResult result,
			HttpServletRequest request, CustomerDAO customerDAO, CartDAO cartDAO, OrderDAO orderDAO) {

		HttpSession session = (HttpSession) request.getSession();

		User user = (User) session.getAttribute("user");
		Long userId = user.getUserId();
		Customer customer = customerDAO.getCustomer(userId);
		String cartId = request.getParameter("cartId");
		Long cartid = Long.parseLong(cartId);
		Cart c = cartDAO.getCart(cartid);
		Order o = new Order();
		o.setCustomer(customer);;
		o.setCart(c);
		Order order = orderDAO.saveOrder(o);
		cartDAO.updateOrder(cartid, order);

		List<Cart> pdfView = (List<Cart>) cartDAO.getCart(userId);
		model.addAttribute("cartList", pdfView);
		View v = new PDF();
		return new ModelAndView(v);

	}
}
	

	