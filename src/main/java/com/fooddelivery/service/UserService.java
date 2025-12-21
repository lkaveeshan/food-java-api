package com.fooddelivery.service;

import com.fooddelivery.model.User;
import com.fooddelivery.repository.UserRepository;
import com.fooddelivery.security.JwtUtil;
import com.google.firebase.auth.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public User signIn(String firebaseToken) throws FirebaseAuthException {

        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(firebaseToken);
        String uid = decodedToken.getUid();

        Optional<User> existing = userRepository.findByUid(uid);

        User user;
        if (existing.isPresent()) {
            user = existing.get();
        } else {
            user = new User();
            user.setUid(uid);
            user.setEmail(decodedToken.getEmail());
            user.setName(decodedToken.getName());
        }

        String refreshToken = jwtUtil.generateToken(uid);

        return userRepository.save(user);
    }

    public String generateAccessToken(String refreshToken) {
        String uid = jwtUtil.validate(refreshToken);
        return jwtUtil.generateToken(uid);
    }

    public Optional<User> getUser(String id) {
        return userRepository.findById(id);
    }

    public User createUser(String email, String password, String fullName) throws FirebaseAuthException {

        try {
            logger.info("Creating user with email: {}", email);
            if (email == null || password == null || fullName == null) {
                throw new RuntimeException("email, password and fullName are required!");
            }

            // 1️⃣ Create user in Firebase
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password)
                    .setDisplayName(fullName);

            UserRecord firebaseUser  = FirebaseAuth.getInstance().createUser(request);
            logger.debug("Firebase user created with UID: {}", firebaseUser.getUid());
            String firebaseUid = firebaseUser.getUid();

            // 2️⃣ Save user in MongoDB
            User user = new User();
            user.setUid(firebaseUid);
            user.setEmail(email);
            user.setName(fullName);

            User savedUser = userRepository.save(user);

            logger.info("User saved to MongoDB with id: {}", savedUser.getId());
            return savedUser;
        } catch (Exception ex) {
            logger.error("Error creating user", ex);
            throw  ex;
        }
    }
}
