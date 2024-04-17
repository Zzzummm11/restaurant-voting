package com.github.zzzummm272.repository;

import com.github.zzzummm272.error.NotFoundException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.github.zzzummm272.model.Dish;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.id=:id AND d.restaurant.id=:restaurantId")
    Optional<Dish> findByIdAndRestaurantId(int id, int restaurantId);

    default Dish getExistedByRestaurantId(int id, int restaurantId) {
        return findByIdAndRestaurantId(id, restaurantId).orElseThrow(() -> new NotFoundException("Dish with id=" + id +
                " and restaurantId=" + restaurantId + " not found"));
    }

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId")
    List<Dish> getAll(@Param("restaurantId") int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId AND d.date=:date")
    List<Dish> getAllByDate(@Param("restaurantId") int restaurantId, @Param("date") LocalDate date);
}
