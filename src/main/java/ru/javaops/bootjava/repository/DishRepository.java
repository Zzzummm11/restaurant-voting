package ru.javaops.bootjava.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.error.NotFoundException;
import ru.javaops.bootjava.model.Dish;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.id=:id AND d.restaurant.id=:restaurantId")
    Optional<Dish> findByIdAndRestaurantId(int id, int restaurantId);

    default Dish checkExistedByRestaurantId(int id, int restaurantId) {
        return findByIdAndRestaurantId(id, restaurantId).orElseThrow(() -> new NotFoundException("Dish with id=" + id +
                " and restaurantId=" + restaurantId + " not found"));
    }

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId")
    List<Dish> getAllByRestaurant(@Param("restaurantId") int restaurantId);

}
