package com.futureprocessing.webtask.exchangeoffice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.futureprocessing.webtask.exchangeoffice.model.Users;

@Service("userService")
@PropertySource(value = "classpath:application.default.properties")
public class UserServiceImpl implements UserService {

    @Autowired
    RestTemplate restTemplate;

    private String url;

    @Autowired
    public UserServiceImpl(Environment environment) {
        this.url = environment.getRequiredProperty("default.userService.url");
    }

    @Override
    public List<Users> findAllUsers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Users findByUsername(String username) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isUserExist(Users user) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void saveUser(Users user) {
        restTemplate.postForObject(url + "/add", user, Boolean.class);
    }

    @Override
    public void updateUser(Users currentUser) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAllUsers() {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteUserByUsername(String username) {
        // TODO Auto-generated method stub

    }

}
