package com.neu.run.pojo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.neu.run.pojo.Product;
import com.neu.run.pojo.User;

@Entity
@Table(name = "seller")
@PrimaryKeyJoinColumn(name = "userId")

public class Seller extends User {

	@Column(name = "company")
	private String company;

	@OneToMany(mappedBy = "seller")
	private Set<Product> products = new HashSet<Product>();

	public Seller() {

	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}
}