package ru.javaops.bootjava.web.dish;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.model.Dish;
import ru.javaops.bootjava.repository.DishRepository;
import ru.javaops.bootjava.repository.RestaurantRepository;
import ru.javaops.bootjava.to.DishTo;

import java.net.URI;
import java.util.List;

import static ru.javaops.bootjava.util.DishUtil.createNewFromTo;
import static ru.javaops.bootjava.validation.ValidationUtil.checkNew;
import static ru.javaops.bootjava.web.RestValidation.assureIdConsistent;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishController {
    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/dishes";

    protected DishRepository dishRepository;
    protected RestaurantRepository restaurantRepository;

    @GetMapping("/{id}")
    public Dish get(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get dish with id={} and restaurantId={}, ", id, restaurantId);
        return dishRepository.getExistedByRestaurantId(id, restaurantId);
    }

    @GetMapping
    public List<Dish> getAllByRestaurant(@PathVariable int restaurantId) {
        log.info("get all dishes for restaurant with id={}, ", restaurantId);
        restaurantRepository.getExisted(restaurantId);
        return dishRepository.getAllByRestaurant(restaurantId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete dishId={} for restaurantId={}", id, restaurantId);
        dishRepository.getExistedByRestaurantId(id, restaurantId);
        dishRepository.deleteExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> create(@Valid @RequestBody DishTo dishTo, @PathVariable int restaurantId) {
        log.info("create new dish for restaurant with id={}", restaurantId);
        restaurantRepository.getExisted(restaurantId);
        Dish created = createNewFromTo(dishTo, restaurantRepository.getReferenceById(restaurantId));
        checkNew(created);
        dishRepository.save(created);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id, @PathVariable int restaurantId) {
        log.info("update dish with id={} and restaurantId={}", id, restaurantId);
        assureIdConsistent(dish, id);
        restaurantRepository.getExisted(restaurantId);
        dishRepository.getExistedByRestaurantId(id, restaurantId);
        dish.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        dishRepository.save(dish);
    }
}
