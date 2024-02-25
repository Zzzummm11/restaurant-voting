package ru.javaops.bootjava.web.restaurant;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.bootjava.HasId;
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
    public int getVoteCount(@PathVariable int id) {
        log.info("get restaurantId={} with vote count", id);
        repository.getExisted(id);
        return repository.getVoteCount(id);
    }

    @GetMapping("/votes")
    public Map<Integer, Integer> getAllWithVoteCount() {
        log.info("get all restaurantId with vote count");
        Map<Integer, Integer> map = repository.getRestaurantsWithVoteCount().stream()
                .collect(Collectors.toMap(
                        result -> (Integer) result[0],
                        result -> ((Number) result[1]).intValue()
                ));
        return repository.findAll().stream()
                .collect(Collectors.toMap(
                        HasId::id,
                        r -> (map.computeIfAbsent(r.getId(), k -> 0))
                ));
    }

    @GetMapping("/{id}/with-menu-and-votes")
    public RestaurantTo getWithMenuAndVoteCount(@PathVariable int id) {
        log.info("get restaurant with menu and vote count");
        repository.getExisted(id);
        int count = repository.getVoteCount(id);
        RestaurantTo restaurantTo = createNewFromTo(repository.getWithMenu(id));
        restaurantTo.setVotes(count);
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
