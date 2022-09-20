package com.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.apache.jasper.tagplugins.jstl.core.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;  
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.beans.Cart;
import com.beans.Customer;
import com.beans.Item;
import com.beans.Orders;
import com.beans.Product;
import com.dao.OrderDao;
import com.dao.ProductDao;
@Controller  
public class ProductController {  
     
	@Autowired
	ProductDao productDao;//will inject dao from XML file
	@Autowired
	OrderDao orderDao;//will inject dao from XML file
	@Autowired
	private JavaMailSender mailSenderObj;

	static String emailToRecipient, emailSubject, emailMessage;
	static final String emailFromRecipient = "shirowhitte@hotmail.com";
	
   @RequestMapping("/viewProductByCategory/{category}")  
   public String showProductCategory(@PathVariable String category,Model theModel) { 
	   List <Product> plist = productDao.getProductByCategory(category);
	   List <Product> byLatest = productDao.sortProductByLatest(category);
	   List <Product> byTop = productDao.sortProductByTopSales(category);
	   List <Product> byHighestPrice = productDao.sortProductByHighestPrice(category);
	   List <Product> byLowestPrice = productDao.sortProductByLowestPrice(category);
	   theModel.addAttribute("list", plist);  
	   theModel.addAttribute("category",category);
	   theModel.addAttribute("byLatest", byLatest);
	   theModel.addAttribute("byTop", byTop);
	   theModel.addAttribute("byHighestPrice", byHighestPrice);
	   theModel.addAttribute("byLowestPrice", byLowestPrice);
       return "viewProductByCategory";  
   } 
   
   @RequestMapping("/viewProduct/{id}")  
   public String showProduct(@PathVariable int id,Model theModel) {  
	   Product product = productDao.getProductById(id);
	   theModel.addAttribute("product", product);          
       return "viewProduct";  
   }
   
   @RequestMapping("/manageProduct")  
   public String showProductMenu(Model theModel) { 
	   theModel.addAttribute("product", new Product());  
       return "manageProduct";  
   }
   

   @RequestMapping("/addProduct")  
   public String addProduct(Model theModel) { 
	   theModel.addAttribute("product", new Product());  
       return "addProduct";  
   }
   
   @RequestMapping("/updateProduct")  
   public String updateProductForm(Model theModel) { 
	   List<Product> plist = productDao.vendorViewProduct();
	   theModel.addAttribute("plist", plist);
       return "updateProduct";  
   }
   
   @RequestMapping("/updateThisProduct/{id}")  
   public String updateThisProduct(@PathVariable int id,Model theModel) { 
	   Product p = productDao.getProductById(id);
	   theModel.addAttribute("product", p);
       return "updateThisProduct";  
   } 
   
   @RequestMapping("/viewProductVendor")  
   public String viewProductFormVendor(Model theModel) { 
	   List<Product> plist = productDao.vendorViewProduct();
	   theModel.addAttribute("plist", plist);  
       return "viewProductVendor";  
   }
   
   @RequestMapping("/deleteProduct")  
   public String deleteProductForm(Model theModel) { 
	   List<Product> plist = productDao.vendorViewProduct();
	   theModel.addAttribute("plist", plist); 
       return "deleteProduct";  
   }
   
   @RequestMapping(value="/deleteProduct/{id}")    
   public String deleteProduct(@PathVariable int id,Model theModel){    
       productDao.deleteProduct(id); 
       theModel.addAttribute("msg","Product has been deleted successfully.");
	   List<Product> plist = productDao.vendorViewProduct();
	   theModel.addAttribute("plist", plist); 
       return "deleteProduct";    
   }  
   
   @RequestMapping(value = "/searchProduct", method = RequestMethod.POST)
	public String searchProductForm(@RequestParam("search") String search, Model theModel) {
		
	   	List <Product> searchProduct = productDao.searchProductList(search);
	   	theModel.addAttribute("searchProduct", searchProduct);  
	   	theModel.addAttribute("product", new Product());  
		return "displaySearchProduct";
	}
  
 //when vendor wants to add a new product 
   @RequestMapping(value="/addProductForm", method=RequestMethod.POST)  
   public String processAddProductForm(@Valid @ModelAttribute("product") Product product,BindingResult br ,@RequestParam("ppimg") MultipartFile img, Model theModel) 
   {
	   		//set the message attribute to empty
		   theModel.addAttribute("msg", "");
	       if (br.hasErrors()) //if validation not pass
	       {
	           return "addProduct";  //return add product page with error message
	       } 
	       else
	       {
	    	   //else if validation pass
	    	  if(!img.isEmpty()) //check whether the img is empty if its not
	    	  {
	    		  try {
	    			  byte[] bytes = img.getBytes(); //change the img to bytes
	    			  String rootPath = "C:\\Users\\SH Chau\\"; //define desktop path
		  			  File dir = new File(rootPath +"eclipse-workspace\\ePortal-Hibernate\\src\\main\\webapp\\resources\\img\\category");//save the uploaded img in this folder
		  			  //this is the eclipse folder 
		  			  if (!dir.exists()) //if this folder is not exits
		  			  {
		  				  dir.mkdirs();//create this folder
		  			  }
		  			  //save this image with ori file name(eg: watch.png)to the folders
					  File serverFile = new File(dir.getAbsolutePath()+ File.separator, img.getOriginalFilename());
					  BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					  stream.write(bytes);
					  stream.close();
					  theModel.addAttribute("msg","Product has been added successfully."); //display success message
					  productDao.addProduct(new Product(product.getPid(),product.getPname(), product.getPdescription(),product.getPcategory(),product.getPrice(),product.getPstock(),img.getOriginalFilename()));
	    			  //insert product info into db
	    		  }
	    		  catch(Exception e)
	    		  {
	    			  e.printStackTrace();
	    		  }
	    	  }
	    	  theModel.addAttribute("product", new Product()); 
	    	  return "addProduct";//return add product page
	       }
     }  
   
   

   @RequestMapping(value="/updateProductForm", method=RequestMethod.POST)  
   public String processUpdateProductForm(@ModelAttribute("product") Product product, Model theModel) 
   {
			Product p1 = new Product(product.getPid(),product.getPname(),product.getPdescription(),product.getPcategory(),product.getPrice(),product.getPstock(),product.getPimg());
			productDao.updateProduct(p1);
			List<Product> plist = productDao.vendorViewProduct();
			theModel.addAttribute("plist", plist);
			theModel.addAttribute("product", new Product()); 
			theModel.addAttribute("msg","Product has been updated successfully.");
			return "updateProduct"; 
     }  
   
   
	private int exists(int id, List<Cart> temp_cart) {
		for (int i = 0; i < temp_cart.size(); i++) 
		{
			if(temp_cart.get(i).getPid() == id)
			{
				return i;
			}
		}
		return -1;
	}
	@RequestMapping("/viewCart")  
	   public String showCart(Model theModel,HttpServletRequest request) {
		   String username = (String)request.getSession().getAttribute("custSession");
		   List<Cart> temp_cart = productDao.customerViewTempCart(username);
		   int totalQty = 0;
		   for(int i=0;i<temp_cart.size();i++)
		   {
			   totalQty = totalQty + temp_cart.get(i).getQuantity();
		   }
		   request.getSession().setAttribute("totalQty", totalQty);
		   theModel.addAttribute("temp_cart", temp_cart);  
	       return "viewCart";  
	   }
	  
		@RequestMapping(value = "buy/{id}", method = RequestMethod.POST)
		public String buy(@PathVariable("id") int id, @RequestParam("quantity") int qty, Model theModel,HttpServletRequest request) {
			Product product = productDao.getProductById(id);
			String uid = (String)request.getSession().getAttribute("custSession");
			List<Cart> temp1 = productDao.customerViewTempCart(uid);
			Customer c = new Customer();
			c.setUsername(uid);
			int old_punitsold = product.getPunitSold();
			int new_punitsold = old_punitsold + qty;
			product.setPunitSold(new_punitsold);
			String pname = product.getPname();
			String pcategory = product.getPcategory();
			String pdescription = product.getPdescription();
			double price = product.getPrice();
			int pstock = product.getPstock();
			String pimg = product.getPimg();
			int pid = product.getPid();
			int punitsold = product.getPunitSold();
			
			Cart c1 = new Cart(pname,pcategory,pdescription,price,pstock,pimg,pid,punitsold,qty,uid);
			int exist = exists(id,temp1);
			if(exist==-1)
			{
				productDao.addProductIntoTempCart(c1);
			}
			else
			{
				for(int j=0; j<temp1.size();j++)
				{
					int oriQty = temp1.get(j).getQuantity();
					int newQty = oriQty + qty;
					int oldUnitSold = temp1.get(j).getPunitsold();
					int newUnitSold = oldUnitSold - oriQty + newQty; //new unit sold
					//temp1.get(j).setPunitsold(newUnitSold);
					Cart itemExist = new Cart(pname,pcategory,pdescription,price,pstock,pimg,pid,newUnitSold,newQty,uid);
					productDao.updateProductFromTempCart(itemExist);
				}
			}
			List<Cart> temp = productDao.customerViewTempCart(uid);
			theModel.addAttribute("temp_cart",temp);
			int totalQty = (Integer)request.getSession().getAttribute("totalQty");
			request.getSession().setAttribute("totalQty", totalQty);  
			return "viewCart";
		}
		
	   @RequestMapping(value = "remove/{id}", method = RequestMethod.GET)
		public String remove(@PathVariable("id") int id,HttpServletRequest request,Model theModel) {
		   String uid = (String)request.getSession().getAttribute("custSession");
			productDao.removeProductFromTempCart(id,uid);
			List<Cart> temp_cart = productDao.customerViewTempCart(uid);
			theModel.addAttribute("temp_cart",temp_cart);
			int totalQty = (Integer)request.getSession().getAttribute("totalQty");
			request.getSession().setAttribute("totalQty", totalQty);
			return "viewCart";
		}
	   
		@RequestMapping(value = "update/{id}", method = RequestMethod.POST)
		public String update(@PathVariable("id") int id, @RequestParam("quantity") int qty,  Model theModel,HttpServletRequest request) {
			Product product = productDao.getProductById(id);
			String uid = (String)request.getSession().getAttribute("custSession");
			Customer c = new Customer();
			c.setUsername(uid);
			int old_punitsold = product.getPunitSold();
			int new_punitsold = old_punitsold + qty;
			product.setPunitSold(new_punitsold);
			String pname = product.getPname();
			String pcategory = product.getPcategory();
			String pdescription = product.getPdescription();
			double price = product.getPrice();
			int pstock = product.getPstock();
			String pimg = product.getPimg();
			int pid = product.getPid();
			Cart cart = new Cart(pname,pcategory,pdescription,price,pstock,pimg,pid,new_punitsold,qty,uid);
			productDao.updateProductFromTempCart(cart);
			 List<Cart> temp_cart = productDao.customerViewTempCart(uid);
			theModel.addAttribute("temp_cart",temp_cart);
			int totalQty = (Integer)request.getSession().getAttribute("totalQty");
			request.getSession().setAttribute("totalQty", totalQty);
			return "viewCart";
		}
		
		   @RequestMapping("/customerConfirmOrder")  
		   public String customerConfirmOrder(Model theModel, HttpServletRequest request) { 
			   String username = (String)request.getSession().getAttribute("custSession");
			   List<Cart> temp_cart = productDao.customerViewTempCart(username);
			   Customer c = productDao.getAddressByUsername(username);
			   theModel.addAttribute("address", c.getAddress());  
			   theModel.addAttribute("temp_cart", temp_cart);  
		       return "customerConfirmOrder";  
		   }
		   
			@RequestMapping(value = "/confirmOrder")
			public String Confirm(Model theModel,HttpServletRequest request,@RequestParam("totalPrice") double totalPrice) {
				String uid = (String)request.getSession().getAttribute("custSession");
				List<Cart> temp_cart = productDao.customerViewTempCart(uid);
				String odate = new java.util.Date().toLocaleString();
				Customer c = productDao.getAddressByUsername(uid);
				String address = c.getAddress();
				Orders o1 = new Orders();
				Orders orders = new Orders(o1.getOid(),odate,uid,address,temp_cart,"Received",totalPrice);
				productDao.addOrder(orders);
				productDao.removeProductFromCart(uid);
				for(int i=0;i<temp_cart.size();i++)
				{
					Customer customer = new Customer();
					customer.setUsername(uid);
					int pid = temp_cart.get(i).getPid();
					//int old_punitsold = temp_cart.get(i).getPunitsold(); //this is the initial sales
					int totalQty = temp_cart.get(i).getQuantity(); //this is the quantity purchased by customer
					Product product = productDao.getProductById(pid);
					int old_stock = product.getPstock();//this is the initial stock
					int old_punitsold = product.getPunitSold();//this is the initial sales
					int new_stock = old_stock - totalQty; //this is the new stock, must update table product
					int new_punitsold = old_punitsold+totalQty; //this is the new unit sold, must update table product and item
					product.setPstock(new_stock);
					product.setPunitSold(new_punitsold);
					product.setPid(pid);
					String pname = product.getPname();
					String pcategory = product.getPcategory();
					String pdescription = product.getPdescription();
					double price = product.getPrice();
					//int pstock = product.getPstock();
					String pimg = product.getPimg();
					//int pid = product.getPid();
					productDao.updateProductStockAndPunitsold(product); //update table product
					//get order id
					Orders orderId = productDao.getOrderId(uid, odate);
					Item items = new Item(orderId.getOid(),pname,pcategory,pdescription,price,new_stock,pimg,pid,new_punitsold,totalQty,uid);
					productDao.addProductIntoItemTable(items);
				}
				
				String msg = "Your order has been confirmed!\n" 
				+ "Order Date: " + odate + "\n" + "Recipent Name: " 
				+ uid + "\nDelivery Address: " + address + "\nPrice: S$" + totalPrice
				+ "\nPlease prepare exact amount. Thank you for shopping with us.";
	
				emailSubject = "Order Confirmation From E-Commerce Portal";
				emailMessage = msg;
				emailToRecipient = c.getEmail();
				
				System.out.println("\nReceipient?= " + emailToRecipient + ", Subject?= " + emailSubject + ", Message?= " + emailMessage + "\n");

				mailSenderObj.send(new MimeMessagePreparator() {
					public void prepare(MimeMessage mimeMessage) throws Exception {

						MimeMessageHelper mimeMsgHelperObj = new MimeMessageHelper(mimeMessage, true, "UTF-8");				
						mimeMsgHelperObj.setTo(emailToRecipient);
						mimeMsgHelperObj.setFrom(emailFromRecipient);				
						mimeMsgHelperObj.setText(emailMessage);
						mimeMsgHelperObj.setSubject(emailSubject);
						
						FileSystemResource file = new FileSystemResource(new File("c:/thankyou.png"));
						mimeMsgHelperObj.addAttachment("thankyou.png", file);
					}
				});
				
				request.getSession().setAttribute("totalQty", 0); 
				theModel.addAttribute("msg", "Your order has been added successfully. Please check your email.");
				List <Orders> orderList= orderDao.customerViewOrderHistory(uid);
				theModel.addAttribute("orderList", orderList); 
				return "customerHistory";
			}

}