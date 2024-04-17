package com.github.zzzummm272.web.restaurant;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.zzzummm272.model.Restaurant;
import com.github.zzzummm272.repository.RestaurantRepository;

import java.util.List;

import static com.github.zzzummm272.util.DateUtil.CURRENT_DATE;

@Slf4j
@RestController
@AllArgsConstructor
@Transactional(readOnly = true)
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    static final String REST_URL = "/api/restaurants";

    private RestaurantRepository repository;

    @Cacheable(value = "restaurantsWithMenu", key = "#id")
    @GetMapping("/{id}/with-menu")
    public Restaurant getWithMenu(@PathVariable int id) {
        log.info("get restaurant with menu by date={}, restaurantId={}", CURRENT_DATE, id);
        return repository.getExistedWithMenuByDate(id, CURRENT_DATE);
    }

    @Cacheable(value = "restaurantsWithMenu")
    @GetMapping("/with-menu")
    public List<Restaurant> getAllWithMenu() {
        log.info("get all restaurants with menu by date={}", CURRENT_DATE);
        return repository.getAllWithMenuByDate(CURRENT_DATE);
    }
}
