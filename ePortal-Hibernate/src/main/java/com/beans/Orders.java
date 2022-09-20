package com.beans;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="orders")
public class Orders {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="oid", nullable=false)
	private int oid;
	@Column(name="odate", nullable=false)
	private String odate;
	@Column(name="cusername", nullable=false)
	private String cusername;
	@Column(name="caddress", nullable=false)
	private String caddress;
	@Transient
	private List <Cart> item;
	@Column(name="ostatus", nullable=false)
	private String ostatus;
	@Column(name="totalPrice", nullable=false)
	private double totalPrice;
	@Transient
	private Item singleItem;
	
	public Orders()
	{
		
	}

	
	public Orders(int oid,String odate, String cusername, String caddress, List<Cart> item, String ostatus, double totalPrice) {
		super();
		this.oid = oid;
		this.odate = odate;
		this.cusername = cusername;
		this.caddress = caddress;
		this.item = item;
		this.ostatus = ostatus;
		this.totalPrice = totalPrice;
	}
	
	public Item getSingleItem() {
		return singleItem;
	}


	public void setSingleItem(Item singleItem) {
		this.singleItem = singleItem;
	}


	public String getOstatus() {
		return ostatus;
	}


	public void setOstatus(String ostatus) {
		this.ostatus = ostatus;
	}


	public int getOid() {
		return oid;
	}

	public void setOid(int oid) {
		this.oid = oid;
	}

	public String getOdate() {
		return odate;
	}

	public void setOdate(String odate) {
		this.odate = odate;
	}
	
	public String getCusername() {
		return cusername;
	}

	public void setCusername(String cusername) {
		this.cusername = cusername;
	}

	public String getCaddress() {
		return caddress;
	}

	public void setCaddress(String caddress) {
		this.caddress = caddress;
	}
	
	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public List<Cart> getItem() {
		return item;
	}

	public void setItem(List<Cart> item) {
		this.item = item;
	}
	
	

	
}
