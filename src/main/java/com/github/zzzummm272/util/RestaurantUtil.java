package com.github.zzzummm272.util;

import com.github.zzzummm272.to.RestaurantTo;
import lombok.experimental.UtilityClass;
import com.github.zzzummm272.model.Restaurant;

import java.util.List;

@UtilityClass
public class RestaurantUtil {

    public static RestaurantTo createNewTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName());
    }

    public static List<RestaurantTo> createListTo(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantUtil::createNewTo).toList();
    }
}