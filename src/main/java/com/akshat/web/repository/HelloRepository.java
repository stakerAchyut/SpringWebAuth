package com.akshat.web.repository;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.akshat.web.model.User;

@Repository
public class HelloRepository {
	
	SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void registerUser(User user) {
		sessionFactory.getCurrentSession().persist(user);
	}
	
	public List<User> getUsers() {
		List<User> list = null;
		try {
			Query<User> query = sessionFactory.getCurrentSession().createQuery("FROM User", User.class);
			list = query.list();
			
			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
}
