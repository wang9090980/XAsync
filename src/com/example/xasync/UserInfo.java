package com.example.xasync;

import com.alibaba.fastjson.annotation.JSONField;


public class UserInfo {
	@JSONField(name="username")
	private String name;
	private String password;
	private String email;
	private String phone;
	@JSONField(name="regTime",format="yyyy-MM-dd HH:mm:ss")
	private String reg;

	public String getReg() {
		return reg;
	}
	public void setReg(String reg) {
		this.reg = reg;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
