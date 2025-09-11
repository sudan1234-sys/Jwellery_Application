package com.example.jwellery.Controllers;

import com.example.jwellery.Entity.User;
import com.example.jwellery.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/saveUser")
    public ResponseEntity<String> addUsers(@Valid @RequestBody User user){
        userService.addUser(user);
        return ResponseEntity.ok("user saved successfully");
    }
}
