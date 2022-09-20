package com.controllers;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;  
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.beans.Orders;
import com.beans.Item;
import com.dao.OrderDao;  
 
@Controller  
public class OrderController {  
     
	@Autowired
	OrderDao orderDao;//will inject dao from XML file

	@RequestMapping("/customerHistory")  
	public String customerHistory(Model theModel,HttpServletRequest request) { 
		String username = (String)request.getSession().getAttribute("custSession");
		List <Orders> orderList= orderDao.customerViewOrderHistory(username);
		theModel.addAttribute("orderList", orderList);       
	    return "customerHistory";  
	   }

	
   @RequestMapping("/viewOrderHistoryDetail/{id}")  
   public String viewOrderHistoryDetail(@PathVariable int id,Model model,HttpServletRequest request) { 
	   String username = (String)request.getSession().getAttribute("custSession");
	   Orders o1 = orderDao.getOrderById(id, username);
	   List <Item> orderDetails= orderDao.customerViewOrderHistoryDetails(id);
	   model.addAttribute("o1", o1); 
	   model.addAttribute("orderDetails", orderDetails);  
	   
       return "viewOrderHistoryDetail";  
   }
   
   @RequestMapping("/manageOrder")  
   public String showOrderManagement(Model model) {  
	   model.addAttribute("order", new Orders());
       return "manageOrder";  
   }
   
   @RequestMapping("/viewOrder")  
   public String viewOrder(Model model) {  
	   List<Orders> orderList = orderDao.vendorViewNewOrder();
	   model.addAttribute("orderList", orderList);
       return "viewOrder";  
   }
   
   @RequestMapping("/vendorViewOrderDetail/{id}")  //this id refers to order id
   public String vendorViewOrderDetail(@PathVariable int id,Model model,HttpServletRequest request) 
   { 
	   Orders o1 = orderDao.getOrderById(id);
	   List <Item> orderDetailList= orderDao.vendorViewOrderHistoryDetails(id);
	   model.addAttribute("o1", o1); 
	   model.addAttribute("orderDetailList", orderDetailList);  
       return "vendorViewOrderDetail";  
   }
	
   @RequestMapping("/updateOrderStatus")  
   public String updateOrderStatus(Model model) { 
	   List<Orders> orderList = orderDao.vendorViewNewOrder();
	   model.addAttribute("orderList", orderList);
	   model.addAttribute("order", new Orders());
       return "updateOrderStatus";  
   }
   
   @RequestMapping(value = "/updateOrderStatusForm") //this id is order id
	public String updateOrderStatusForm(@RequestParam("orderID") int orderID, @RequestParam("status") String status, Model theModel,HttpServletRequest request) {
		Orders order = orderDao.getOrderById(orderID);
		order.setOid(orderID);
		order.setOstatus(status);
	   	orderDao.updateOrderStatus(order);
	   	List<Orders> orderList = orderDao.vendorViewNewOrder();
		theModel.addAttribute("orderList", orderList);
	   	theModel.addAttribute("msg", "Order has been updated successfully."); 
		return "updateOrderStatus";
	}
   
   @RequestMapping("/orderHistory")  
   public String orderHistory(Model model) {  
	   List<Orders> orderList = orderDao.vendorViewOldOrder();
	   model.addAttribute("orderList", orderList);
       return "orderHistory";  
   }
   
   


   

    
}