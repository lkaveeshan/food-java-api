package com.fooddelivery.dto;
import lombok.Data;

@Data
public class FoodRequest {
    private String id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
}