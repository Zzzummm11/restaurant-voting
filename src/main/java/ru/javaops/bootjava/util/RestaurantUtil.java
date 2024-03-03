package ru.javaops.bootjava.util;

import lombok.experimental.UtilityClass;
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.to.RestaurantTo;

import java.util.List;

@UtilityClass
public class RestaurantUtil {

    public static RestaurantTo createNewFromTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName());
    }

    public static List<RestaurantTo> createListFromTo(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantUtil::createNewFromTo).toList();
    }
}