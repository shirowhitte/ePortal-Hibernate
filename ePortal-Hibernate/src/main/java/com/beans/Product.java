package com.beans;

import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
@Entity
@Table(name="product")
public class Product 
{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="pid", nullable=false)
	private int pid;
	@Column(name="pname", nullable=false)
	@NotNull(message="Product name must be filled")
	private String pname;
	@Column(name="pdescription", nullable=false)
	@NotNull(message="Description must be filled")
	private String pdescription;
	@Column(name="pcategory", nullable=false)
	private String pcategory;
	@Column(name="price", nullable=false)
	@DecimalMin(value = "1.0", message = "Price must be >= 1.0")
	private double price;
	@Column(name="pstock", nullable=false)
	@Min(value=1, message="Stock must be >= 1")
	private int pstock;
	@Column(name="pimg", nullable=false)
	@NotNull(message="Please select an image")
	private String pimg;
	@Column(name="punitsold", nullable=false)
	private int punitSold;

	private void checkNull() {
	    if(this.pname == null || this.pname.isEmpty())
	    {
	    	this.pname = null;
	    }
	    if(this.pdescription == null || this.pdescription.isEmpty())
	    {
	    	this.pdescription = null;
	    }
	    if(this.price == 0.0)
	    {
	    	this.price = 0;
	    }
	    if(this.pstock == 0.0)
	    {
	    	this.pstock = 0;
	    }
	    if(this.pimg == null || this.pimg.isEmpty())
	    {
	    	this.pimg = null;
	    }
	}
	public Product()
	{
		
	}
	
	public Product(int pid, double price, int pstock) {
		super();
		this.pid = pid;
		this.price = price;
		this.pstock = pstock;
	}

	public Product(int pid,String pname, String pdescription, String pcategory, double price, int pstock,String pimg) {
		super();
		this.pid = pid;
		this.pname = pname;
		this.pdescription = pdescription;
		this.pcategory = pcategory;
		this.price = price;
		this.pstock = pstock;
		this.pimg = pimg;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
		checkNull();
	}

	public String getPdescription() {
		return pdescription;
	}

	public void setPdescription(String pdescription) {
		this.pdescription = pdescription;
		checkNull();
	}

	public String getPcategory() {
		return pcategory;
	}

	public void setPcategory(String pcategory) {
		this.pcategory = pcategory;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
		checkNull();
	}

	public int getPstock() {
		return pstock;
	}

	public void setPstock(int pstock) {
		this.pstock = pstock;
		checkNull();
	}

	public String getPimg() {
		return pimg;
	}

	public void setPimg(String pimg) {
		this.pimg = pimg;
		checkNull();
	}

	public int getPunitSold() {
		return punitSold;
	}

	public void setPunitSold(int punitSold) {
		this.punitSold = punitSold;
	}
	
	
	
}
