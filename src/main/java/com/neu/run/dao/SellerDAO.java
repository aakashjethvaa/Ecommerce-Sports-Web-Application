package com.neu.run.dao;

import org.hibernate.Query;

import com.neu.run.pojo.Seller;
import com.neu.run.dao.DAO;

public class SellerDAO extends DAO {

	public SellerDAO() {

	}

	public Seller register(Seller s) {

		begin();

		getSession().save(s);
		commit();
		return s;
	}

	public Seller getSeller(Long userId) {
		// TODO Auto-generated method stub
		Query q = getSession().createQuery("from Seller where userId = :userId");
		q.setLong("userId", userId);
		Seller seller = (Seller) q.uniqueResult();
		return seller;
	}

}
