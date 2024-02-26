package ru.javaops.bootjava.util;

import lombok.experimental.UtilityClass;
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.to.RestaurantTo;

@UtilityClass
public class RestaurantUtil {

    public static RestaurantTo createNewFromTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName());
    }
}