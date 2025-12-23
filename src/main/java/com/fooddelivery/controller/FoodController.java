package com.fooddelivery.controller;

import com.fooddelivery.dto.FoodRequest;
import com.fooddelivery.model.Food;
import com.fooddelivery.service.FoodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/food")

public class FoodController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveFood(@RequestBody FoodRequest request) {
        try {
            logger.info("FoodController.saveFood");
            Food data = foodService.insertOrUpdateFood(request);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            logger.error("Error in saveFood controller", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllFoods(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size){
        try {
            logger.info("FoodController.getAllFoods");
            Page<Food> data = foodService.getAllFoods(page, size);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            logger.error("Error in getAllFoods controller", e);
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFood(@PathVariable String id) {
        try {
            logger.info("FoodController.getFood");
            Food data = foodService.getFood(id);
            if (data == null) {
                return ResponseEntity.status(404).body("Food item not found for id: " + id);
            }
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            logger.error("Error in getFood controller", e);
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFood(@PathVariable String id) {
        try {
            logger.info("FoodController.deleteFood");
            foodService.deleteFood(id);
            return ResponseEntity.ok("Food deleted successfully");
        } catch (Exception e) {
            logger.error("Error in deleteFood controller", e);
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/health")
    public String healthCheck() {
        return  "health";
    }
}