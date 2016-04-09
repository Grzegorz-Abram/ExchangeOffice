package com.futureprocessing.webtask.exchangeoffice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futureprocessing.webtask.exchangeoffice.dao.UsersDao;
import com.futureprocessing.webtask.exchangeoffice.model.Users;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UsersDao usersDao;

    @Override
    public void registerNewUserAccount(Users user) {
        usersDao.save(user);
    }

}
