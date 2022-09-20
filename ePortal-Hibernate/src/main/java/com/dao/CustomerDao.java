package com.dao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.beans.Cart;
import com.beans.Customer;
import com.beans.Product;

@Repository @EnableTransactionManagement
public class CustomerDao {

HibernateTemplate template;
public void setTemplate(HibernateTemplate template) {
    this.template = template;
}

@Transactional
public boolean checkUsername(String username)
{
	Customer c1 = new Customer();
	c1.setUsername(username);
	List<Customer> customerList = template.findByExample(c1);
	return !customerList.isEmpty();
}

@Transactional
public boolean checkEmail(String email)
{
	Customer c1 = new Customer();
	c1.setEmail(email);
	List<Customer> customerList = template.findByExample(c1);
	return !customerList.isEmpty();
}

@Transactional
public boolean login(String username, String password)
{
	Customer c1 = new Customer();
	c1.setUsername(username);
	c1.setPassword(password);
	List<Customer> customerList = template.findByExample(c1);
	
	return !customerList.isEmpty();
}

@Transactional
public void register(Customer c){
	
	template.save(c);
	
};

@Transactional
public List<Product> customerPortalViewProduct(){ 
	Session session = template.getSessionFactory().getCurrentSession();
    String sql = "SELECT * FROM product order by rand()";
    SQLQuery query = session.createSQLQuery(sql);
    query.addEntity(Product.class);
    query.setMaxResults(6);
    List<Product> results = query.list();
    return results;
    

  	}

@Transactional
public Customer getCustomerByUsername(String username){  
	Session s = template.getSessionFactory().getCurrentSession(); 
	Customer c = (Customer)s.createCriteria(Customer.class).add(Restrictions.eq("username", username)).uniqueResult();
	return c;   
}  

@Transactional
public void updateAddress(Customer c){    

    Session s = template.getSessionFactory().getCurrentSession(); 
	CriteriaBuilder builder = s.getCriteriaBuilder();
	CriteriaUpdate<Customer>criteria =builder.createCriteriaUpdate(Customer.class);
	Root<Customer>root = criteria.from(Customer.class);
	criteria.set(root.get("address"), c.getAddress());
	criteria.where(builder.equal(root.get("username"),c.getUsername()));
	s.createQuery(criteria).executeUpdate();
	
	
}
}