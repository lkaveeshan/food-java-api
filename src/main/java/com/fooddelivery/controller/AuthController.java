package com.fooddelivery.controller;

import com.google.firebase.auth.FirebaseAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/generate-token")
    public ResponseEntity<?> generateToken(@RequestBody Map<String, String> body) {

        try {
            String uid = body.get("uid");

            if (uid == null || uid.isEmpty()) {
                return ResponseEntity.badRequest().body("uid is required");
            }

            logger.info("Generating Firebase custom token for UID: {}", uid);

            String customToken = FirebaseAuth.getInstance().createCustomToken(uid);

            Map<String, String> response = new HashMap<>();
            response.put("customToken", customToken);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error generating Firebase token", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> body) throws Exception {

        String refreshToken = body.get("refreshToken");

        if (refreshToken == null) {
            return ResponseEntity.badRequest().body("refreshToken is required");
        }

        String url = "https://securetoken.googleapis.com/v1/token?key=AIzaSyBRJ6BrYompGAbzmHAlNnT_y1B446D3H6g";

        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> payload = new HashMap<>();
        payload.put("grant_type", "refresh_token");
        payload.put("refresh_token", refreshToken);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, payload, Map.class);

        return ResponseEntity.ok(response.getBody());
    }
}