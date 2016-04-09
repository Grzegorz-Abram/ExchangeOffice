package com.future_processing.webtask.exchangeoffice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.future_processing.webtask.exchangeoffice.dao.UsersDao;
import com.future_processing.webtask.exchangeoffice.model.Users;

@Service("userService")
public class UserServiceImpl implements UserService {

	UsersDao usersDao;

	@Autowired
	public void setUsersDao(UsersDao usersDao) {
		this.usersDao = usersDao;
	}

	@Override
	public void registerNewUserAccount(Users user) {
		usersDao.save(user);
	}

}
