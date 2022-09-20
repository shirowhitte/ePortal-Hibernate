package com.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Controller;  
import org.springframework.ui.Model;  
import org.springframework.validation.BindingResult;  
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.beans.Cart;
import com.beans.Customer;
import com.beans.Item;
import com.beans.Orders;
import com.beans.Product;
import com.dao.CustomerDao;
import com.dao.ProductDao;

import org.springframework.beans.factory.BeanFactory;  
import org.springframework.beans.factory.xml.XmlBeanFactory;  
import org.springframework.core.io.ClassPathResource;  
import org.springframework.core.io.Resource;  
 
@Controller  
public class CustomerController {  
     
	@Autowired
	CustomerDao customerDao;
	@Autowired
	ProductDao productDao;
   @RequestMapping("/customerRegister")  
   public String showForm(Model theModel) {  
       theModel.addAttribute("customer", new Customer());     
       return "customerRegister";  
   } 
   
   @RequestMapping("/customerLogin")  
   public String loginForm(Model theModel) {  
       theModel.addAttribute("customer", new Customer());    
       return "customerLogin";  
   }
  
   @RequestMapping("/aboutUs")  
   public String aboutUsPage() {           
       return "aboutUs";  
   }
   
   @RequestMapping("/contactUs")  
   public String contactUsPage() {           
       return "contactUs";  
   }
   
   @RequestMapping("/customerLogout")  
   public String logoutPage(HttpSession session, Model theModel) {           
	   session.invalidate();
	   theModel.addAttribute("customer", new Customer());  
       return "customerLogin";  
   }
   
   
   @RequestMapping("/customerPortal")  
   public String customerPortal(Model theModel) {
	   List<Product> plist = customerDao.customerPortalViewProduct();
	   theModel.addAttribute("plist", plist); 
	   theModel.addAttribute("customer", new Customer());
       return "customerPortal";  
   }
   
   @RequestMapping("/registerForm")  
   public String processForm( @Valid @ModelAttribute("customer") Customer customer, BindingResult br, Model theModel) {  
             
	   theModel.addAttribute("msg", "");
       if (br.hasErrors()) 
       {
           return "customerRegister";  
       } 
       else 
       {  
    	   if(customerDao.checkEmail(customer.getEmail())|| customerDao.checkUsername(customer.getUsername()))
    	   {
    		   theModel.addAttribute("msg", "Username or Email has already exist.");
    		   return "customerRegister"; 
    	   }
    	   else
    	   {
	    	   customerDao.register(customer);
	    	   theModel.addAttribute("msg", "User has been registered successfully. Please log in.");
	    	   theModel.addAttribute("customer", customer);
	    	   return "customerLogin"; 
    	   }
    	   
       }  
   }  
   
   @RequestMapping(value="/loginForm", method=RequestMethod.POST)  
   public String processLoginForm(@ModelAttribute("customer") Customer customer, Model theModel,HttpServletRequest request) 
   {  
	   if(customerDao.login(customer.getUsername(), customer.getPassword())==true)
	   {
		   request.getSession().setAttribute("custSession", customer.getUsername());
		   List<Product> plist = customerDao.customerPortalViewProduct();
		   String username = (String)request.getSession().getAttribute("custSession"); //get the session attribute
		   List<Cart> temp_cart = productDao.customerViewTempCart(username);  //get the temporary cart for this specific user
		   int totalQty = 0;
		   for(int i=0;i<temp_cart.size();i++)
		   {
			   totalQty = totalQty + temp_cart.get(i).getQuantity(); //count the total quantity in cart
		   }
		   request.getSession().setAttribute("totalQty", totalQty);//set the total quantity as session attribute
		   theModel.addAttribute("temp_cart", temp_cart);  //supply tempory cart to the view
		   theModel.addAttribute("plist", plist);  //supply product list to the view
		   theModel.addAttribute("customer", customer); //supply this customer to the view
		   return "customerPortal";
	   }
	   else
	   {
		   theModel.addAttribute("msg", "Invalid Credential. Please try again.");
		   return "customerLogin";
	   } 
   }  
   
   @RequestMapping(value="/updateAddress/{username}")    
   public String updateAddress(@PathVariable String username,Model theModel){ 
	   Customer c = customerDao.getCustomerByUsername(username);
	   String address = c.getAddress();
	   String email = c.getEmail();
	   theModel.addAttribute("email",email);
	   theModel.addAttribute("address",address);
       //customerDao.updateAddress(c);
       theModel.addAttribute("customer",  new Customer());
       return "updateAddress";    
   }  
   
   @RequestMapping(value = "/updateAddressForm",method=RequestMethod.POST)
	public String updateOrderStatusForm(@RequestParam("username") String username, @RequestParam("address") String address, Model theModel,HttpServletRequest request) {
		Customer c = new Customer();
		c.setUsername(username);
		c.setAddress(address);
		customerDao.updateAddress(c);
		Customer cu = customerDao.getCustomerByUsername(username);
		String add = cu.getAddress();
		String email = cu.getEmail();
		theModel.addAttribute("email",email);
		theModel.addAttribute("address",add);
	    theModel.addAttribute("customer",  new Customer());
	    theModel.addAttribute("msg",  "Your address has been updated successfully.");
		return "updateAddress";
	}

}  