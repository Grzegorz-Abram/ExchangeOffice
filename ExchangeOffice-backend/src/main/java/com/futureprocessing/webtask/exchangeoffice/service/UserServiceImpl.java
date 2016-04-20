package com.futureprocessing.webtask.exchangeoffice.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.futureprocessing.webtask.exchangeoffice.model.Authorities;
import com.futureprocessing.webtask.exchangeoffice.model.Users;
import com.futureprocessing.webtask.exchangeoffice.repository.AuthoritiesRepository;
import com.futureprocessing.webtask.exchangeoffice.repository.UsersRepository;

@Service("userService")
@Transactional
@PropertySource(value = "classpath:application.default.properties")
public class UserServiceImpl implements UserService {

    @Autowired
    private Environment environment;

    @Autowired
    protected UsersRepository usersRepository;

    @Autowired
    protected AuthoritiesRepository authoritiesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void registerNewUserAccount(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        usersRepository.save(user);

        Authorities authority = new Authorities();
        authority.setUsername(user.getUsername());
        authority.setAuthority(environment.getRequiredProperty("default.user.role"));
        authoritiesRepository.save(authority);
    }

}
