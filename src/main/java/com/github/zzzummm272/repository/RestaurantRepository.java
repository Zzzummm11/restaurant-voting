package com.github.zzzummm272.repository;

import com.github.zzzummm272.error.NotFoundException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.github.zzzummm272.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.dishes m WHERE r.id = :id AND m.date = :date")
    Optional<Restaurant> findWithMenu(@Param("id") int id, LocalDate date);

    default Restaurant getExistedWithMenuByDate(int id, LocalDate date) {
        return findWithMenu(id, date).orElseThrow(() -> new NotFoundException("Restaurant with id=" + id + " doesn't have a menu by date=" + date));
    }

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.dishes m WHERE m.date = :date")
    List<Restaurant> getAllWithMenuByDate(LocalDate date);

    default void checkExisted(int id) {
        if (!existsById(id)) {
            throw new NotFoundException("Restaurant with id=" + id + " does not exist");
        }
    }
}
