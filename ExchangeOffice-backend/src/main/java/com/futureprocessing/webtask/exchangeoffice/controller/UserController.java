package com.futureprocessing.webtask.exchangeoffice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.futureprocessing.webtask.exchangeoffice.model.Users;
import com.futureprocessing.webtask.exchangeoffice.service.UserService;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/user/", method = RequestMethod.GET)
    public ResponseEntity<List<Users>> listAllUsers() {
        List<Users> users = userService.findAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<List<Users>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Users>>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Users> getUser(@PathVariable("username") String username) {
        System.out.println("Fetching User with username " + username);
        Users user = userService.findByUsername(username);
        if (user == null) {
            System.out.println("User with username " + username + " not found");
            return new ResponseEntity<Users>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Users>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody Users user, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating User " + user.getUsername());

        if (userService.isUserExist(user)) {
            System.out.println("A User with username " + user.getUsername() + " already exist");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        userService.saveUser(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/user/{username}").buildAndExpand(user.getUsername()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/user/{username}", method = RequestMethod.PUT)
    public ResponseEntity<Users> updateUser(@PathVariable("username") String username, @RequestBody Users user) {
        System.out.println("Updating User " + username);

        Users currentUser = userService.findByUsername(username);

        if (currentUser == null) {
            System.out.println("User with username " + username + " not found");
            return new ResponseEntity<Users>(HttpStatus.NOT_FOUND);
        }

        currentUser.setPassword(user.getPassword());
        currentUser.setEnabled(user.isEnabled());

        userService.updateUser(currentUser);
        return new ResponseEntity<Users>(currentUser, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<Users> deleteUser(@PathVariable("username") String username) {
        System.out.println("Fetching & Deleting User with username " + username);

        Users user = userService.findByUsername(username);
        if (user == null) {
            System.out.println("Unable to delete. User with username " + username + " not found");
            return new ResponseEntity<Users>(HttpStatus.NOT_FOUND);
        }

        userService.deleteUserByUsername(username);
        return new ResponseEntity<Users>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/user/", method = RequestMethod.DELETE)
    public ResponseEntity<Users> deleteAllUsers() {
        System.out.println("Deleting All Users");

        userService.deleteAllUsers();
        return new ResponseEntity<Users>(HttpStatus.NO_CONTENT);
    }

}
