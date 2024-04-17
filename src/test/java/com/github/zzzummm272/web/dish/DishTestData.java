package com.github.zzzummm272.web.dish;

import com.github.zzzummm272.web.MatcherFactory;
import com.github.zzzummm272.model.Dish;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.github.zzzummm272.web.restaurant.RestaurantTestData.*;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "restaurant");
    public static final int DISH1_ID = 1;
    public static final int DISH2_ID = 2;
    public static final int DISH4_ID = 4;
    public static final int NOT_FOUND = 40;

    public static final String DISH2_NAME = "dish_2_rest_1";

    public static final Dish dish1 = new Dish(DISH1_ID, "dish_1_rest_1", LocalDate.of(2024, 2, 4), BigDecimal.valueOf(500).setScale(2), restaurant1);
    public static final Dish dish2 = new Dish(DISH2_ID, DISH2_NAME, LocalDate.of(2024, 2, 4), BigDecimal.valueOf(200).setScale(2), restaurant1);
    public static final Dish dish3 = new Dish(DISH1_ID + 2, "dish_3_rest_1", LocalDate.of(2024, 2, 4), BigDecimal.valueOf(10).setScale(2), restaurant1);
    public static final Dish dish4 = new Dish(DISH4_ID, "dish_4_rest_2", LocalDate.of(2024, 2, 4), BigDecimal.valueOf(110).setScale(2), restaurant2);
    public static final Dish dish5 = new Dish(DISH1_ID + 4, "dish_5_rest_2", LocalDate.of(2024, 2, 4), BigDecimal.valueOf(800).setScale(2), restaurant2);
    public static final Dish dish6 = new Dish(DISH1_ID + 5, "dish_6_rest_3", LocalDate.of(2024, 2, 4), BigDecimal.valueOf(90).setScale(2), restaurant3);
    public static final Dish dish7 = new Dish(DISH1_ID + 6, "dish_7_rest_1", LocalDate.now(), BigDecimal.valueOf(25).setScale(2), restaurant1);
    public static final Dish dish8 = new Dish(DISH1_ID + 7, "dish_8_rest_1", LocalDate.now(), BigDecimal.valueOf(25).setScale(2), restaurant1);
    public static final Dish dish9 = new Dish(DISH1_ID + 8, "dish_9_rest_1", LocalDate.now(), BigDecimal.valueOf(25).setScale(2), restaurant1);
    public static final Dish dish10 = new Dish(DISH1_ID + 9, "dish_10_rest_2", LocalDate.now(), BigDecimal.valueOf(90).setScale(2), restaurant2);

    public static final List<Dish> allDishesForRestaurant1 = List.of(dish1, dish2, dish3, dish7, dish8, dish9);
    public static final List<Dish> allDishesForRestaurant2 = List.of(dish4, dish5, dish10);
    public static final List<Dish> allDishesForRestaurant3 = List.of(dish6);

    public static Dish getNew() {
        return new Dish(null, "newDish", LocalDate.now(), BigDecimal.valueOf(90));
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "updateDish", LocalDate.of(2024, 2, 4), BigDecimal.valueOf(200).setScale(2));
    }
}
