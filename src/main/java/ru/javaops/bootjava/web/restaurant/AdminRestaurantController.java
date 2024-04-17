package ru.javaops.bootjava.web.restaurant;

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
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.repository.RestaurantRepository;
import ru.javaops.bootjava.to.RestaurantTo;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.javaops.bootjava.util.RestaurantUtil.createListTo;
import static ru.javaops.bootjava.util.RestaurantUtil.createNewTo;
import static ru.javaops.bootjava.web.RestValidation.checkNew;

@Slf4j
@RestController
@AllArgsConstructor
@Transactional(readOnly = true)
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantController {
    static final String REST_URL = "/api/admin/restaurants";

    private RestaurantRepository repository;

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        log.info("get restaurant with id={}", id);
        return createNewTo(repository.getExisted(id));
    }

    @GetMapping
    public List<RestaurantTo> getAll() {
        log.info("get all restaurants");
        return createListTo(repository.findAll());
    }

    @GetMapping("/{id}/with-menu")
    public Restaurant getWithMenuByDate(@PathVariable int id, @RequestParam LocalDate date) {
        log.info("get restaurant with menu by date={}, restaurantId={}", date, id);
        return repository.getExistedWithMenuByDate(id, date);
    }

    @GetMapping("/with-menu")
    public List<Restaurant> getAllWithMenuByDate(@RequestParam LocalDate date) {
        log.info("get all restaurants with menu by date={}", date);
        return repository.getAllWithMenuByDate(date);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CacheEvict(value = "restaurantsWithMenu", allEntries = true)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant with id={}", id);
        repository.deleteExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @CacheEvict(value = "restaurantsWithMenu", allEntries = true)
    public ResponseEntity<RestaurantTo> create(@Valid @RequestBody Restaurant restaurant) {
        log.info("create restaurant {}", restaurant);
        checkNew(restaurant);
        Restaurant created = repository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(createNewTo(created));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CacheEvict(value = "restaurantsWithMenu", allEntries = true)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update restaurant with id={}", id);
        repository.checkExisted(id);
        restaurant.setId(id);
        repository.save(restaurant);
    }
}