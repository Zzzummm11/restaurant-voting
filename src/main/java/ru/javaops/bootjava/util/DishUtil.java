package ru.javaops.bootjava.util;

import lombok.experimental.UtilityClass;
import ru.javaops.bootjava.model.Dish;
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.to.DishTo;

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
