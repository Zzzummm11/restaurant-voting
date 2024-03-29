package ru.javaops.bootjava.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "dish", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "name", "date_dish"},
        name = "dish_unique_idx")})
@Getter
@Setter
@NoArgsConstructor
public class Dish extends NamedEntity {

    @Column(name = "date_dish", nullable = false)
    @NotNull
    @DateTimeFormat
    private LocalDate dateTime;

    @Column(name = "price", nullable = false)
    @NotNull
    private BigDecimal price;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public Dish(Integer id, String name, LocalDate dateTime, BigDecimal price) {
        super(id, name);
        this.dateTime = dateTime;
        this.price = price;
    }

    public Dish(Integer id, String name, LocalDate dateTime, BigDecimal price, Restaurant restaurant) {
        super(id, name);
        this.dateTime = dateTime;
        this.price = price;
        this.restaurant = restaurant;
    }

    public Dish(Dish dish) {
        super(dish.getId(), dish.getName());
        this.dateTime = dish.getDateTime();
        this.price = dish.getPrice();
        this.restaurant = dish.getRestaurant();
    }
}