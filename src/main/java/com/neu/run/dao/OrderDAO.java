package com.neu.run.dao;

import com.neu.run.pojo.Order;

public class OrderDAO extends DAO {

	public Order saveOrder(Order o) {
		// TODO Auto-generated method stub
		begin();
		getSession().save(o);
		commit();
		return o;
	}

}
