package com.dao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import javax.persistence.TypedQuery;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;

import com.beans.Cart;
import com.beans.Customer;
import com.beans.Item;
import com.beans.Orders;
import com.beans.Product;

@Repository @EnableTransactionManagement
public class OrderDao {

HibernateTemplate template;

public void setTemplate(HibernateTemplate template) {
    this.template = template;
}
@Transactional
public List<Orders> customerViewOrderHistory(String username){    
	Criteria cr = template.getSessionFactory().getCurrentSession().createCriteria(Orders.class);
	cr.add(Restrictions.eq("cusername", username));
	cr.addOrder(Order.desc("oid"));
	cr.setMaxResults(10);
	List<Orders> orderList = cr.list();
	return orderList;
  	}

@Transactional
public List<Item> customerViewOrderHistoryDetails(int oid){       
	
	Criteria cr = template.getSessionFactory().getCurrentSession().createCriteria(Item.class);
	cr.add(Restrictions.eq("oid", oid));
	List<Item> orderList = cr.list();
	return orderList;
}

@Transactional
public Orders getOrderById(int oid,String username){  
	Criteria cr = template.getSessionFactory().getCurrentSession().createCriteria(Orders.class);
	cr.add(Restrictions.eq("oid", oid));
	cr.add(Restrictions.eq("cusername", username)); 
	Orders o = (Orders)cr.uniqueResult();
	return o;     
}  

@Transactional
public List<Orders> vendorViewNewOrder(){    
    	Criteria cr = template.getSessionFactory().getCurrentSession().createCriteria(Orders.class);
    	Criterion c1 = Restrictions.eq("ostatus", "Received");
    	Criterion c2 = Restrictions.eq("ostatus", "Preparing");
   		LogicalExpression orExp = Restrictions.or(c1,c2);
   		cr.add(orExp);
   		cr.addOrder(Order.asc("oid"));
   		List<Orders> orderList = cr.list();
   		return orderList;
  	}  
@Transactional
public List<Orders> vendorViewOldOrder(){    
		Criteria cr = template.getSessionFactory().getCurrentSession().createCriteria(Orders.class);
		Criterion c1 = Restrictions.eq("ostatus", "Delivered");
		cr.add(c1);
		cr.addOrder(Order.asc("oid"));
		List<Orders> orderList = cr.list();
		return orderList;
  	}  
@Transactional
public List<Item> vendorViewOrderHistoryDetails(int oid){    
	Criteria cr = template.getSessionFactory().getCurrentSession().createCriteria(Item.class);
	cr.add(Restrictions.eq("oid", oid));
	List<Item> itemList = cr.list();
	return itemList;
}


@Transactional
public Orders getOrderById(int oid){  
	Criteria cr = template.getSessionFactory().getCurrentSession().createCriteria(Orders.class);
	cr.add(Restrictions.eq("oid", oid));
	Orders o = (Orders)cr.uniqueResult();
	return o;     
}  
 
@Transactional
public void updateOrderStatus(Orders o){ 
    Session s = template.getSessionFactory().getCurrentSession(); 
	CriteriaBuilder builder = s.getCriteriaBuilder();
	CriteriaUpdate<Orders>criteria =builder.createCriteriaUpdate(Orders.class);
	Root<Orders>root = criteria.from(Orders.class);
	criteria.set(root.get("ostatus"),o.getOstatus());
	criteria.where(builder.equal(root.get("oid"),o.getOid()));
	s.createQuery(criteria).executeUpdate();
}    


}
