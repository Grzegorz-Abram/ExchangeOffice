package com.futureprocessing.webtask.exchangeoffice.service;

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
    public void registerNewUserAccount(Users user) {
        restTemplate.postForObject(url + "/add", user, Boolean.class);
    }

    @Override
    public String getUsername() {
        Users user = restTemplate.getForObject(url + "/current/get", Users.class);

        if (user == null) {
            return null;
        } else {
            return user.getUsername();
        }
    }

    @Override
    public boolean isLoggedIn() {
        return restTemplate.getForObject(url + "/current/get", Users.class) == null ? false : true;
    }

}
