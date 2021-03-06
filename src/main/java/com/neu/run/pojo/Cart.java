package com.neu.run.pojo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.neu.run.pojo.Order;
import com.neu.run.pojo.ProductCart;

@Entity
@Table(name = "cart")
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cartId", unique = true, nullable = false)
	private long id;

	@Column(name = "totalItems")
	private int totalItems;

	@Column(name = "totalPrice")
	private float totalPrice;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	private Customer customer;

	@OneToOne(mappedBy = "cart", cascade = CascadeType.ALL)
	private Order order;

	@OneToMany(mappedBy = "cart")
	private Set<ProductCart> productCart = new HashSet<ProductCart>();

	public Cart() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}

	public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Set<ProductCart> getProductCart() {
		return productCart;
	}

	public void setProductCart(Set<ProductCart> productCart) {
		this.productCart = productCart;
	}
}
