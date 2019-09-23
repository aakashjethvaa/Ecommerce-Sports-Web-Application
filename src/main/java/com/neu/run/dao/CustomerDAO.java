package com.neu.run.dao;

import org.hibernate.Query;

import com.neu.run.pojo.User;
import com.neu.run.pojo.Customer;

public class CustomerDAO extends DAO {

	public CustomerDAO() {

	}

	public Customer register(Customer c) {

		begin();

		getSession().save(c);
		commit();
		return c;

	}

	public Customer getCustomer(Long userId) {
		// TODO Auto-generated method stub
		Query q = getSession().createQuery("from Customer where userId = :userId");
		q.setLong("userId", userId);
		Customer customer = (Customer) q.uniqueResult();
		return customer;

	}
}
