package com.fooddelivery.controller;

import com.fooddelivery.model.User;
import com.fooddelivery.service.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping("/signin")
    public User signIn(@RequestBody Map<String, String> body) throws FirebaseAuthException {
        return userService.signIn(body.get("firebaseToken"));
    }

    @PostMapping("/refresh")
    public Map<String, String> refresh(@RequestBody Map<String, String> body) {
        return Map.of("accessToken", userService.generateAccessToken(body.get("refreshToken")));
    }

    @PostMapping("/create")
    public User createUser(@RequestBody Map<String, String> body) {
        try{
            logger.info("FoodController.createUser");
            String email = body.get("email");
            String password = body.get("password");
            String fullName = body.get("fullName");

            return userService.createUser(email, password, fullName);
        } catch (Exception e){
            logger.error("Error in deleteFood controller", e);
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return userService.getUser(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"
                ));
    }
}
