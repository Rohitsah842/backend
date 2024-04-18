package com.Springboot.backend.dto;



import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


public class CustomerDto {
	
	@NotNull
	private String fullname;
	
	@NotNull
	@Email
	private String email;
	
	@NotNull
//	@Pattern(regexp = "^(\\+\\[1-9]{1}\\[0-9]{3,14}$)")
	private String mobileNo;
	
	
	@NotNull
	private String age;
	
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotNull
	private String password;
	
	

	private String role;
	
	@Column(name = "Create_date")
	private String createDate;
	
	


	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

}
