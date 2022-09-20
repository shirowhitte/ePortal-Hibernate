package com.beans;

import javax.validation.constraints.Pattern;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="customer")
public class Customer 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", nullable=false)
	private int id;
	@Column(name="username", nullable=false)
	@NotNull(message="Username must be filled.") @Pattern(regexp="^([a-zA-Z0-9]{5,50})?$",message="Username must be more than 5.")
	private String username;
	@Column(name="email", nullable=false)
	@NotNull(message="Email must be filled.")
	private String email;
	@Column(name="password", nullable=false)
	@Pattern(regexp="^([a-zA-Z0-9]{8,30})?$",message="Password must be more than 8." )
	private String password;
	@Transient
	@NotNull(message="Password not match.")
	private String confirmPassword;
	@Column(name="address")
	@NotNull(message="Address must be filled.")
	private String address;
	
	public Customer()
	{
		
	}
	
	public Customer(String username, String email)
	{
		this.username = username;
		this.email = email;
	}

	public Customer(String username, String email, String password, String address) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.address = address;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
		checkNull();
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
		checkNull();
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
		checkPassword();
	}

	public void setConfirmPassword(String confirmPassword) {
	    this.confirmPassword = confirmPassword;
	    checkPassword();
	}

	public String getConfirmPassword()
	{
		return confirmPassword;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
		checkNull();
	}
	
	private void checkPassword() {
	    if(this.password == null || this.confirmPassword == null){
	        return;
	    }else if(!this.password.equals(confirmPassword)){
	        this.confirmPassword = null;
	    }
	}
	
	private void checkNull() {
	    if(this.username == null || this.username.isEmpty())
	    {
	    	this.username = null;
	    }
	    if(this.email == null || this.email.isEmpty())
	    {
	    	this.email = null;
	    }
	    if(this.address == null || this.address.isEmpty())
	    {
	    	this.address = null;
	    }
	}
	
	
}
