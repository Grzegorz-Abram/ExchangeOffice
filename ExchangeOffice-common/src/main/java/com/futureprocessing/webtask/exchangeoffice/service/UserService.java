package com.futureprocessing.webtask.exchangeoffice.service;

import java.util.List;

import com.futureprocessing.webtask.exchangeoffice.model.Users;

public interface UserService {

    List<Users> findAllUsers();

    Users findByUsername(String username);

    boolean isUserExist(Users user);

    void saveUser(Users user);

    void updateUser(Users currentUser);

    void deleteAllUsers();

    void deleteUserByUsername(String username);

}
