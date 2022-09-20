package com.dao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.beans.Customer;
import com.beans.Vendor;

@Repository
public class VendorDao {

HibernateTemplate template;

public void setTemplate(HibernateTemplate template) {
    this.template = template;
}

	public boolean login(String vusername, String vpassword)
	{
		Vendor v1 = new Vendor();
		v1.setVpassword(vpassword);
		v1.setVusername(vusername);
		List<Vendor> vendorList = template.findByExample(v1);
		
		return !vendorList.isEmpty();
	}

}