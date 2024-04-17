package com.github.zzzummm272.web.dish;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.github.zzzummm272.model.Dish;
import com.github.zzzummm272.repository.DishRepository;
import com.github.zzzummm272.repository.RestaurantRepository;
import com.github.zzzummm272.to.DishTo;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.github.zzzummm272.util.DishUtil.createNewFromTo;
import static com.github.zzzummm272.web.RestValidation.assureIdConsistent;
import static com.github.zzzummm272.web.RestValidation.checkNew;

@Slf4j
@RestController
@AllArgsConstructor
@Transactional(readOnly = true)
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishController {
    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/dishes";

    private DishRepository dishRepository;
    private RestaurantRepository restaurantRepository;

    @GetMapping("/{id}")
    public Dish get(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get dish with id={} and restaurantId={}, ", id, restaurantId);
        return dishRepository.getExistedByRestaurantId(id, restaurantId);
    }

    @GetMapping
    public List<Dish> getAll(@PathVariable int restaurantId) {
        log.info("get all dishes for restaurant with id={}, ", restaurantId);
        return dishRepository.getAll(restaurantId);
    }

    @GetMapping("/menu")
    public List<Dish> getAllByDate(@PathVariable int restaurantId, @RequestParam LocalDate date) {
        log.info("get all dishes by date={} for restaurant with id={}, ", date, restaurantId);
        return dishRepository.getAllByDate(restaurantId, date);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CacheEvict(value = "restaurantsWithMenu", allEntries = true)
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete dishId={} for restaurantId={}", id, restaurantId);
        dishRepository.deleteExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @CacheEvict(value = "restaurantsWithMenu", allEntries = true)
    public ResponseEntity<Dish> create(@Valid @RequestBody DishTo dishTo, @PathVariable int restaurantId) {
        log.info("create new dish for restaurant with id={}", restaurantId);
        restaurantRepository.checkExisted(restaurantId);
        checkNew(dishTo);
        Dish created = createNewFromTo(dishTo, restaurantRepository.getReferenceById(restaurantId));
        dishRepository.save(created);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CacheEvict(value = "restaurantsWithMenu", allEntries = true)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id, @PathVariable int restaurantId) {
        log.info("update dish with id={} and restaurantId={}", id, restaurantId);
        assureIdConsistent(dish, id);
        restaurantRepository.checkExisted(restaurantId);
        dishRepository.getExistedByRestaurantId(id, restaurantId);
        dish.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        dishRepository.save(dish);
    }
}
