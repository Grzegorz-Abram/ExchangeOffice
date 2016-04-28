package com.futureprocessing.webtask.exchangeoffice.controller;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.futureprocessing.webtask.exchangeoffice.model.Users;
import com.futureprocessing.webtask.exchangeoffice.service.UserService;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/user/current", method = RequestMethod.GET)
    public Map<String, String> user(Principal principal) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", principal.getName());

        return map;
    }

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
        Users user = userService.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<Users>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Users>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/", method = RequestMethod.POST)
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        if (userService.isUserExist(user)) {
            return new ResponseEntity<Users>(HttpStatus.CONFLICT);
        }

        userService.saveUser(user);

        return new ResponseEntity<Users>(user, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/user/{username}", method = RequestMethod.PUT)
    public ResponseEntity<Users> updateUser(@PathVariable("username") String username, @RequestBody Users user) {
        Users currentUser = userService.findByUsername(username);
        if (currentUser == null) {
            return new ResponseEntity<Users>(HttpStatus.NOT_FOUND);
        }
        if (!username.equals(user.getUsername())) {
            return new ResponseEntity<Users>(HttpStatus.CONFLICT);
        }

        userService.updateUser(user);

        return new ResponseEntity<Users>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<Users> deleteUser(@PathVariable("username") String username) {
        Users user = userService.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<Users>(HttpStatus.NOT_FOUND);
        }

        userService.deleteUserByUsername(username);

        return new ResponseEntity<Users>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/user/", method = RequestMethod.DELETE)
    public ResponseEntity<Users> deleteAllUsers() {
        userService.deleteAllUsers();

        return new ResponseEntity<Users>(HttpStatus.NO_CONTENT);
    }

}
