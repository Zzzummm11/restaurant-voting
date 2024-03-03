package ru.javaops.bootjava.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.error.NotFoundException;
import ru.javaops.bootjava.model.Restaurant;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.dishes m WHERE r.id = :id AND m.dateTime = CURRENT_DATE")
    Optional<Restaurant> findWithMenu(@Param("id") int id);

    default Restaurant getWithMenu(int id) {
        return findWithMenu(id).orElseThrow(() -> new NotFoundException("Restaurant with id=" + id + " doesn't have a menu"));
    }

    @Query("SELECT v.restaurant.id, COUNT(v) as vote_count FROM Vote v WHERE v.date = CURRENT_DATE GROUP BY v.restaurant.id")
    List<Object[]> getRestaurantsWithVoteCount();

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.restaurant.id = :id AND v.date = CURRENT_DATE")
    int getVoteCount(@Param("id") int id);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.dishes m WHERE m.dateTime = CURRENT_DATE")
    List<Restaurant> getAllWithMenu();

    default void checkExisted(int id) {
        if (!existsById(id)) {
            throw new NotFoundException("Restaurant with id=" + id + " does not exist");
        }
    }
}
