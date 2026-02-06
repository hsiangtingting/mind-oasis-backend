package com.mindoasis.backend.service;

import com.mindoasis.backend.model.AppUser;
import com.mindoasis.backend.repository.AppUserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository userRepository;

    public AppUser registerUser(AppUser user) {

        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        if (user.getUuid() == null || user.getUuid().isEmpty()) {
            user.setUuid(UUID.randomUUID().toString());
        }

        return userRepository.save(user);
    }

    public AppUser findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}