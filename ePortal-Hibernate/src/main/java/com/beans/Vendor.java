package com.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="vendor")
public class Vendor {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="vid", nullable=false)
	private int vid;
	@Column(name="vusername", nullable=false)
	private String vusername;
	@Column(name="vpassword", nullable=false)
	private String vpassword;
	
	public Vendor()
	{
		
	}

	public int getVid() {
		return vid;
	}

	public void setVid(int vid) {
		this.vid = vid;
	}

	public String getVusername() {
		return vusername;
	}

	public void setVusername(String vusername) {
		this.vusername = vusername;
	}

	public String getVpassword() {
		return vpassword;
	}

	public void setVpassword(String vpassword) {
		this.vpassword = vpassword;
	}
	
}
