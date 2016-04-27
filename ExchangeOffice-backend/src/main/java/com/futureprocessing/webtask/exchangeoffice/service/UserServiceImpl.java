package com.futureprocessing.webtask.exchangeoffice.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.futureprocessing.webtask.exchangeoffice.model.Authorities;
import com.futureprocessing.webtask.exchangeoffice.model.Users;
import com.futureprocessing.webtask.exchangeoffice.model.Wallets;
import com.futureprocessing.webtask.exchangeoffice.model.WalletsId;
import com.futureprocessing.webtask.exchangeoffice.repository.AuthoritiesRepository;
import com.futureprocessing.webtask.exchangeoffice.repository.UsersRepository;
import com.futureprocessing.webtask.exchangeoffice.repository.WalletsRepository;

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
    protected WalletsRepository walletsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Users> findAllUsers() {
        return (List<Users>) usersRepository.findAll();
    }

    @Override
    public Users findByUsername(String username) {
        return usersRepository.findOne(username);
    }

    @Override
    public boolean isUserExist(Users user) {
        return usersRepository.exists(user.getUsername());
    }

    @Override
    public void saveUser(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        usersRepository.save(user);
        authoritiesRepository.save(new Authorities(user.getUsername(), environment.getRequiredProperty("default.user.role")));
        walletsRepository.save(new Wallets(user.getUsername(), "PLN", environment.getRequiredProperty("default.user.amount.PLN", Double.class)));
    }

    @Override
    public void updateUser(Users currentUser) {
        currentUser.setPassword(passwordEncoder.encode(currentUser.getPassword()));
        usersRepository.save(currentUser);
    }

    @Override
    public void deleteAllUsers() {
        usersRepository.deleteAll();
        authoritiesRepository.deleteAll();
        walletsRepository.deleteAll();
    }

    @Override
    public void deleteUserByUsername(String username) {
        usersRepository.delete(username);
        authoritiesRepository.delete(username);
        for (Wallets walletEntry : walletsRepository.findByUsernameOrderByCurrency(username)) {
            walletsRepository.delete(new WalletsId(username, walletEntry.getCurrency()));
        }
    }

}
