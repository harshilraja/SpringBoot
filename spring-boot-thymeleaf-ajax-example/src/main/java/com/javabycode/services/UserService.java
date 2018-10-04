package com.javabycode.services;

import com.javabycode.model.User;
import com.javabycode.model.LoginForm;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    private List<User> users;

    public List<User> login(LoginForm loginForm) {

        //do stuffs
    	//dump user data
    	User user = new User(loginForm.getUsername(), loginForm.getPassword(), "email@javabycode.com");
    	
        return new ArrayList<User>(Arrays.asList(user));

    }

}
