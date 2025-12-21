package com.fooddelivery.service;

import com.fooddelivery.dto.FoodRequest;
import com.fooddelivery.model.Food;
import com.fooddelivery.repository.FoodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public Food insertOrUpdateFood(FoodRequest request) throws Exception {
        try {
            Food food;
            if (request.getId() == null || request.getId().isEmpty()) {
                logger.info("Creating new food item: {}", request.getName());
                food = new Food();
            } else {
                logger.info("Updating food with ID: {}", request.getId());
                food = foodRepository.findById(request.getId())
                        .orElseThrow(() -> new Exception("Food not found"));
            }

            food.setName(request.getName());
            food.setDescription(request.getDescription());
            food.setPrice(request.getPrice());
            food.setImageUrl(request.getImageUrl());

            Food result = foodRepository.save(food);
            logger.info("Food saved successfully with id: {}", result.getId());
            return result;

        } catch (Exception e) {
            logger.error("Error saving food: ", e);
            throw e;
        }
    }

    public Page<Food> getAllFoods(Integer page, Integer size) throws Exception{
        try {
            logger.info("Getting all foods");
            Pageable pageable = PageRequest.of(page, size);
            return foodRepository.findAll(pageable);
        } catch (Exception e) {
            logger.error("Error getting all foods: ", e);
            throw e;
        }
    }

    public Food getFood(String id) throws Exception {
        try {
            logger.info("Getting food item with ID: {}", id);
            Food food = foodRepository.findById(id)
                    .orElseThrow(() -> new Exception("Food not found"));
            return food;
        } catch (Exception e) {
            logger.error("Error getting food item with ID: {}", id, e);
            throw e;
        }
    }

    public void deleteFood(String id) throws Exception {
        try {
            logger.warn("Deleting food item with ID: {}", id);
            foodRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting food item with ID: {}", id, e);
            throw e;
        }
    }
}