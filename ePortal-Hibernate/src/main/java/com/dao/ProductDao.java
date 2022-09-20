package com.dao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.beans.Cart;
import com.beans.Customer;
import com.beans.Product;
import com.beans.Item;
import com.beans.Orders;

@Repository @EnableTransactionManagement
public class ProductDao {

HibernateTemplate template;

public void setTemplate(HibernateTemplate template) {
    this.template = template;
}

@Transactional
public void addProduct(Product p){
    template.save(p);
}

@Transactional
public void updateProduct(Product p){ 
    template.update(p);
}    

@Transactional
public void updateProductStockAndPunitsold(Product p){    
    //String sql="update Product set punitsold="+p.getPunitSold()+", pstock="+p.getPstock()+" where pid="+p.getPid()+"";    
    Session s = template.getSessionFactory().getCurrentSession(); 
	CriteriaBuilder builder = s.getCriteriaBuilder();
	CriteriaUpdate<Product>criteria =builder.createCriteriaUpdate(Product.class);
	Root<Product>root = criteria.from(Product.class);
	criteria.set(root.get("punitSold"), p.getPunitSold());
	criteria.set(root.get("pstock"), p.getPstock());
	criteria.where(builder.equal(root.get("pid"), p.getPid()));
	s.createQuery(criteria).executeUpdate();
}    


@Transactional
public void deleteProduct(int id){    
	Session s = template.getSessionFactory().getCurrentSession(); 
	Product p = (Product)s.load(Product.class, id);
	s.delete(p); 
} 

@Transactional
public List<Product> vendorViewProduct(){    
    	Criteria cr = template.getSessionFactory().getCurrentSession().createCriteria(Product.class);
    	cr.addOrder(Order.asc("pid"));
    	List<Product> productList = cr.list();
    	return productList;
  	}
/*
public List<Product> vendorGetProductByCategory(int pid){    
    String sql="select pname,pcategory,pdescription,price,pstock,pimg ,pid from product where pid=?";    
    return template.query(sql, new Object[] {pid}, new RowMapper<Product>()   
    {
    	public Product mapRow(ResultSet rs, int row) throws SQLException {    
        	Product p=new Product();    
        	p.setPname(rs.getString(1));
        	p.setPcategory(rs.getString(2));
        	p.setPdescription(rs.getString(3));
        	p.setPrice(rs.getDouble(4));
        	p.setPstock(rs.getInt(5));
        	p.setPimg(rs.getString(6));
        	p.setPid(rs.getInt(7));
            return p;
    		}
    });
} 

*/
@Transactional
public Product getProductById(int id){    
	Product p = (Product)template.get(Product.class, id);
	return p;   
}  

@Transactional
public Customer getAddressByUsername(String username){    
	Session s = template.getSessionFactory().getCurrentSession(); 
	Customer c = (Customer)s.createCriteria(Customer.class).add(Restrictions.eq("username", username)).uniqueResult();
	return c;    
}  


@Transactional
public List<Product> getProductByCategory(String category){     
	Criteria cr = template.getSessionFactory().getCurrentSession().createCriteria(Product.class);
	cr.add(Restrictions.eq("pcategory", category));
	cr.addOrder(Order.asc("pid"));
	List<Product> productList = cr.list();
	return productList;
} 

@Transactional
public List<Product> sortProductByLatest(String category){    
		Criteria cr = template.getSessionFactory().getCurrentSession().createCriteria(Product.class);
		cr.add(Restrictions.eq("pcategory", category));
		cr.addOrder(Order.desc("pid"));
		List<Product> productList = cr.list();
		return productList;
} 

@Transactional
public List<Product> sortProductByTopSales(String category){    
		Criteria cr = template.getSessionFactory().getCurrentSession().createCriteria(Product.class);
		cr.add(Restrictions.eq("pcategory", category));
		cr.addOrder(Order.desc("punitSold"));
		List<Product> productList = cr.list();
		return productList;
} 

@Transactional
public List<Product> sortProductByHighestPrice(String category){    
		Criteria cr = template.getSessionFactory().getCurrentSession().createCriteria(Product.class);
		cr.add(Restrictions.eq("pcategory", category));
		cr.addOrder(Order.desc("price"));
		List<Product> productList = cr.list();
		return productList;
} 

@Transactional
public List<Product> sortProductByLowestPrice(String category){    
		Criteria cr = template.getSessionFactory().getCurrentSession().createCriteria(Product.class);
		cr.add(Restrictions.eq("pcategory", category));
		cr.addOrder(Order.asc("price"));
		List<Product> productList = cr.list();
		return productList;
}

@Transactional
public List<Product> searchProductList(String search){      
    String thisSearch = "%" + search + "%";
	Criteria cr = template.getSessionFactory().getCurrentSession().createCriteria(Product.class);
	Criterion c1 = Restrictions.like("pname", thisSearch);
	Criterion c2 = Restrictions.like("pcategory", thisSearch);
	LogicalExpression orExp = Restrictions.or(c1,c2);
	cr.add(orExp);
	List<Product> productList = cr.list();
	return productList;
} 

/*
public Item getItemById(int id,String username){    
    String sql="select quantity from temp_cart where pid=? and username=?";    
    return template.queryForObject(sql, new Object[]{id,username},new BeanPropertyRowMapper<Item>(Item.class));    
}  
*/
@Transactional
public void addProductIntoTempCart(Cart c){
    template.save(c);
}

@Transactional
public void updateProductFromTempCart(Cart c){    
   // String sql="update temp_cart set quantity="+i.getQuantity()+", punitsold="+i.getProduct().getPunitSold()+" where pid="+i.getProduct().getPid()+" and username='"+i.getUsername().getUsername()+"'";    
	Session s = template.getSessionFactory().getCurrentSession(); 
	CriteriaBuilder builder = s.getCriteriaBuilder();
	CriteriaUpdate<Cart>criteria =builder.createCriteriaUpdate(Cart.class);
	Root<Cart>root = criteria.from(Cart.class);
	criteria.set(root.get("quantity"), c.getQuantity());
	criteria.set(root.get("punitsold"), c.getPunitsold());
	criteria.where(builder.and(builder.equal(root.get("pid"), c.getPid()), builder.equal(root.get("username"), c.getUsername())));
	s.createQuery(criteria).executeUpdate();
}   

@Transactional
public void removeProductFromTempCart(int id, String username){       
    Session s = template.getSessionFactory().getCurrentSession(); 
	Cart cart = (Cart)s.createCriteria(Cart.class).add(Restrictions.eq("username", username)).add(Restrictions.eq("pid", id)).uniqueResult();
	s.delete(cart);
} 

@Transactional
public List<Cart> customerViewTempCart(String username){    
  Criteria cr = template.getSessionFactory().getCurrentSession().createCriteria(Cart.class);
	cr.add(Restrictions.eq("username", username));
	List<Cart> cartList = cr.list();
	return cartList;
  	}

@Transactional
public void addOrder(Orders o){
    template.save(o);
}
@Transactional
public void removeProductFromCart(String username){ 
	
	Session s = template.getSessionFactory().getCurrentSession(); 
	CriteriaBuilder builder = s.getCriteriaBuilder();
	CriteriaDelete<Cart>delete = builder.createCriteriaDelete(Cart.class);
	Root<Cart>root = delete.from(Cart.class);
	delete.where(builder.equal(root.get("username"), username));
	s.createQuery(delete).executeUpdate();

} 

@Transactional
public Orders getOrderId(String cusername, String odate)
{
	Criteria cr = template.getSessionFactory().getCurrentSession().createCriteria(Orders.class);
	cr.add(Restrictions.eq("cusername", cusername));
	cr.add(Restrictions.eq("odate", odate)); 
	Orders o = (Orders)cr.uniqueResult();
	return o;
}
@Transactional
public void addProductIntoItemTable(Item i){
    //String sql="insert into Item(oid,pname,pcategory,pdescription,price,pstock,pimg,pid,punitsold,quantity)"
    //		+ " values((SELECT oid FROM orders WHERE cusername=? and odate=?),'"+i.getProduct().getPname()+"','"+i.getProduct().getPcategory()+"','"+i.getProduct().getPdescription()+"',"+i.getProduct().getPrice()+","+i.getProduct().getPstock()+",'"+i.getProduct().getPimg()+"' ,"+i.getProduct().getPid()+" ,"+i.getProduct().getPunitSold()+","+i.getQuantity()+")";
   // return template.update(sql,username, odate);
    template.save(i);
}

}