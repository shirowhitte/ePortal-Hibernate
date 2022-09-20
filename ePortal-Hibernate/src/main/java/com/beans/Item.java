package com.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.Pattern;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="item")
public class Item {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", nullable=false)
	private int id;
	@Column(name="oid", nullable=false)
	private int oid;
	@Column(name="pname", nullable=false)
	private String pname;
	@Column(name="pcategory", nullable=false)
	private String pcategory;
	@Column(name="pdescription", nullable=false)
	private String pdescription;
	@Column(name="price", nullable=false)
	private double price;
	@Column(name="pstock", nullable=false)
	private int pstock;
	@Column(name="pimg", nullable=false)
	private String pimg;
	@Column(name="pid", nullable=false)
	private int pid;
	@Column(name="punitsold")
	private int punitsold;
	@Column(name="quantity", nullable=false)
	private int quantity;
	@Transient
	private String username;

	
	public Item() {
	}
	

	public Item(int oid, String pname, String pcategory, String pdescription, double price, int pstock, String pimg,
			int pid, int punitsold, int quantity, String username) {
		super();
		this.oid = oid;
		this.pname = pname;
		this.pcategory = pcategory;
		this.pdescription = pdescription;
		this.price = price;
		this.pstock = pstock;
		this.pimg = pimg;
		this.pid = pid;
		this.punitsold = punitsold;
		this.quantity = quantity;
		this.username = username;
	}



	public int getOid() {
		return oid;
	}


	public void setOid(int oid) {
		this.oid = oid;
	}


	public String getPname() {
		return pname;
	}


	public void setPname(String pname) {
		this.pname = pname;
	}


	public String getPcategory() {
		return pcategory;
	}


	public void setPcategory(String pcategory) {
		this.pcategory = pcategory;
	}


	public String getPdescription() {
		return pdescription;
	}


	public void setPdescription(String pdescription) {
		this.pdescription = pdescription;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public int getPstock() {
		return pstock;
	}


	public void setPstock(int pstock) {
		this.pstock = pstock;
	}


	public String getPimg() {
		return pimg;
	}


	public void setPimg(String pimg) {
		this.pimg = pimg;
	}


	public int getPid() {
		return pid;
	}


	public void setPid(int pid) {
		this.pid = pid;
	}


	public int getPunitsold() {
		return punitsold;
	}


	public void setPunitsold(int punitsold) {
		this.punitsold = punitsold;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}

	
	

	
	

}
