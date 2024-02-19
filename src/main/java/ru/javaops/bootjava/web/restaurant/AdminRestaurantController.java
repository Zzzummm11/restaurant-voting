package ru.javaops.bootjava.web.restaurant;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.repository.RestaurantRepository;
import ru.javaops.bootjava.to.RestaurantTo;
import ru.javaops.bootjava.util.RestaurantUtil;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javaops.bootjava.util.RestaurantUtil.updateFromTo;
import static ru.javaops.bootjava.validation.ValidationUtil.checkNew;
import static ru.javaops.bootjava.web.RestValidation.assureIdConsistent;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantController {
    static final String REST_URL = "/api/admin/restaurants";

    protected RestaurantRepository repository;

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        log.info("get {}", id);
        return updateFromTo(repository.getExisted(id));
    }

    @GetMapping
    public List<RestaurantTo> getAll() {
        log.info("getAll");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
                .map(RestaurantUtil::updateFromTo)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        Assert.notNull(restaurant, "restaurant must not be null");
        checkNew(restaurant);
        Restaurant created = repository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        repository.save(restaurant);
    }
}
