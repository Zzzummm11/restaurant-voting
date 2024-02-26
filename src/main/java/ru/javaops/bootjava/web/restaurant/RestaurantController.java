package ru.javaops.bootjava.web.restaurant;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.repository.RestaurantRepository;
import ru.javaops.bootjava.to.RestaurantTo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.javaops.bootjava.util.RestaurantUtil.createNewFromTo;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    static final String REST_URL = "/api/restaurants";

    protected RestaurantRepository repository;

    @GetMapping("/{id}/with-menu")
    public Restaurant getWithMenu(@PathVariable int id) {
        log.info("get restaurant with menu, restaurantId={}", id);
        repository.getExisted(id);
        return repository.getWithMenu(id);
    }

    @GetMapping("/with-menu")
    public List<Restaurant> getAllWithMenu() {
        log.info("get all restaurants with menu");
        return repository.getAllWithMenu();
    }

    @GetMapping("/{id}/votes")
    public RestaurantTo getVoteCount(@PathVariable int id) {
        log.info("get restaurant with id={} with vote count", id);
        Restaurant restaurantExist = repository.getExisted(id);
        int count = repository.getVoteCount(id);
        RestaurantTo restaurantTo = createNewFromTo(restaurantExist);
        restaurantTo.setVotes(count);
        return restaurantTo;
    }

    @GetMapping("/votes")
    public List<RestaurantTo> getAllWithVoteCount() {
        log.info("get all restaurant with vote count");
        Map<Integer, Integer> map = repository.getRestaurantsWithVoteCount().stream()
                .collect(Collectors.toMap(
                        result -> (Integer) result[0],
                        result -> ((Number) result[1]).intValue()
                ));
        return repository.findAll().stream()
                .map(r -> {
                    RestaurantTo restaurantTo = createNewFromTo(r);
                    restaurantTo.setVotes(map.computeIfAbsent(r.getId(), k -> 0));
                    return restaurantTo;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/with-menu-and-votes")
    public RestaurantTo getWithMenuAndVoteCount(@PathVariable int id) {
        log.info("get restaurant with menu and vote count");
        repository.getExisted(id);
        int count = repository.getVoteCount(id);
        Restaurant restaurantWithMenu = repository.getWithMenu(id);
        RestaurantTo restaurantTo = createNewFromTo(restaurantWithMenu);
        restaurantTo.setVotes(count);
        restaurantTo.setDishes(restaurantWithMenu.getDishes());
        return restaurantTo;
    }

    @GetMapping("/with-menu-and-votes")
    public List<RestaurantTo> getAllWithMenuAndVoteCount() {
        log.info("get all restaurants with menu and vote count");
        Map<Integer, Integer> map = repository.getRestaurantsWithVoteCount().stream()
                .collect(Collectors.toMap(
                        result -> (Integer) result[0],
                        result -> ((Number) result[1]).intValue()
                ));
        return repository.getAllWithMenu().stream()
                .map(r -> {
                    RestaurantTo restaurantTo = createNewFromTo(r);
                    restaurantTo.setDishes(r.getDishes() != null ? r.getDishes() : null);
                    restaurantTo.setVotes(map.computeIfAbsent(r.getId(), k -> 0));
                    return restaurantTo;
                }).toList();
    }
}
