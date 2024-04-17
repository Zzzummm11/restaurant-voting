package com.github.zzzummm272.web.restaurant;

import com.github.zzzummm272.model.Restaurant;
import com.github.zzzummm272.web.MatcherFactory;

import java.util.List;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "dishes", "votes");

    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = RESTAURANT1_ID + 1;

    public static final int NOT_FOUND = 40;
    public static final String RESTAURANT2_NAME = "restaurant2";
    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "restaurant1");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT1_ID + 1, RESTAURANT2_NAME);
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT1_ID + 2, "restaurant3");

    public static final List<Restaurant> restaurants = List.of(restaurant1, restaurant2, restaurant3);

    public static Restaurant getNew() {
        return new Restaurant(null, "newRestaurant");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT1_ID, "updateRestaurant");
    }
}