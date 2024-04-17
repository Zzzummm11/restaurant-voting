package com.github.zzzummm272.util;

import lombok.experimental.UtilityClass;
import com.github.zzzummm272.model.Dish;
import com.github.zzzummm272.model.Restaurant;
import com.github.zzzummm272.to.DishTo;

@UtilityClass
public class DishUtil {

    public static Dish createNewFromTo(DishTo dishTo, Restaurant restaurant) {
        Dish dish = new Dish();
        dish.setName(dishTo.getName());
        dish.setPrice(dishTo.getPrice());
        dish.setDate(dishTo.getDate());
        dish.setRestaurant(restaurant);
        return dish;
    }
}
