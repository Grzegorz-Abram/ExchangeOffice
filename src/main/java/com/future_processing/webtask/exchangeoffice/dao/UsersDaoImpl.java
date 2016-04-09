package com.future_processing.webtask.exchangeoffice.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.future_processing.webtask.exchangeoffice.model.Authorities;
import com.future_processing.webtask.exchangeoffice.model.Users;

@Repository
public class UsersDaoImpl implements UsersDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public UsersDaoImpl() {
	}

	public UsersDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional(readOnly = false)
	public void save(Users users) {
		Session session = sessionFactory.openSession();
		
		users.setPassword(passwordEncoder.encode(users.getPassword()));
		users.setEnabled(true);
		session.save(users);
		
		Authorities authority = new Authorities();
		authority.setUsername(users.getUsername());
		authority.setAuthority("USER");
		session.save(authority);
		
		session.flush();
	}

}
