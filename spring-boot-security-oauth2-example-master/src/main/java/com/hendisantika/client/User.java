package com.hendisantika.client;

public class User {
	private long id;
	 private String username;
	 private long salary;
	 private long price;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public long getSalary() {
		return salary;
	}
	public void setSalary(long salary) {
		this.salary = salary;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public User(long id, String username, long salary, long price) {
		super();
		this.id = id;
		this.username = username;
		this.salary = salary;
		this.price = price;
	}
	
}
