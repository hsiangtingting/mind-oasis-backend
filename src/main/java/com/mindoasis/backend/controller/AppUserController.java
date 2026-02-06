package com.mindoasis.backend.controller;

import com.mindoasis.backend.model.AppUser;
import com.mindoasis.backend.service.AppUserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
//@CrossOrigin(origins = "*")
public class AppUserController {

    @Autowired
    private AppUserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody AppUser user) {
        if (userService.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("The Email has been used.");
        }
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody Map<String, String> loginData) {

        System.out.println("DEBUG [1] - Received loginData: " + loginData);

        String email = loginData.get("email");
        String password = loginData.get("password");

        AppUser user = userService.findByEmail(email);

        if (user == null) {
            System.out.println("DEBUG [2] - User not found in DB for email: [" + email + "]");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        System.out.println("DEBUG [3] - User found! DB Hashed Password: [" + user.getPassword() + "]");

        boolean isMatch = BCrypt.checkpw(password, user.getPassword());

        System.out.println("DEBUG [4] - BCrypt match result: " + isMatch);

        if (isMatch) {
            user.setPassword(null);
            return ResponseEntity.ok(Map.of(
                    "email", user.getEmail(),
                    "token", user.getUuid()
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }
    }