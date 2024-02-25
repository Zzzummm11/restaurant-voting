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
import ru.javaops.bootjava.to.RestaurantToForAdmin;
import ru.javaops.bootjava.util.RestaurantUtil;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javaops.bootjava.util.RestaurantUtil.createNewFromAdminTo;
import static ru.javaops.bootjava.validation.ValidationUtil.checkNew;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantController {
    static final String REST_URL = "/api/admin/restaurants";

    protected RestaurantRepository repository;

    @GetMapping("/{id}")
    public RestaurantToForAdmin get(@PathVariable int id) {
        log.info("get restaurant with id={}", id);
        return createNewFromAdminTo(repository.getExisted(id));
    }

    @GetMapping
    public List<RestaurantToForAdmin> getAll() {
        log.info("get all restaurants");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
                .map(RestaurantUtil::createNewFromAdminTo)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant with id={}", id);
        repository.deleteExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantToForAdmin> create(@Valid @RequestBody Restaurant restaurant) {
        log.info("create restaurant {}", restaurant);
        Assert.notNull(restaurant, "restaurant must not be null");
        checkNew(restaurant);
        Restaurant created = repository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(createNewFromAdminTo(created));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update restaurant with id={}", id);
        repository.getExisted(id);
        repository.save(restaurant);
    }
}
