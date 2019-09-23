package com.neu.run.pojo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.neu.run.pojo.Cart;
import com.neu.run.pojo.Order;
import com.neu.run.pojo.User;

@Entity
@Table(name = "customer")
@PrimaryKeyJoinColumn(name = "userId")

public class Customer extends User {

	@Column(name = "firstName")
	private String firstName;

	@Column(name = "lastName")
	private String lastName;
	
	@Column(name = "gender")
	private String gender;

	@OneToMany(mappedBy = "customer")
	private Set<Cart> carts = new HashSet<Cart>();

	@OneToMany(mappedBy = "customer")
	private Set<Order> orderHistory = new HashSet<Order>();

	public Customer() {

	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Set<Cart> getCarts() {
		return carts;
	}

	public void setCarts(Set<Cart> carts) {
		this.carts = carts;
	}

	public Set<Order> getOrderHistory() {
		return orderHistory;
	}

	public void setOrderHistory(Set<Order> orderHistory) {
		this.orderHistory = orderHistory;
	}
}
