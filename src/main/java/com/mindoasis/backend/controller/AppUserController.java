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
        AppUser user = userService.findByEmail(loginData.get("email"));
        if (user != null && BCrypt.checkpw(loginData.get("password"), user.getPassword())) {
            user.setPassword(null);
            return ResponseEntity.ok(Map.of(
                    "email", user.getEmail(),
                    "token", user.getUuid()
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid account or password");
    }
}