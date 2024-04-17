package com.github.zzzummm272.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "menu_item", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "date_dish", "name"},
        name = "dish_unique_idx")})
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Dish extends NamedEntity {

    @Column(name = "date_dish", nullable = false)
    @NotNull
    @DateTimeFormat
    private LocalDate date;

    @Column(name = "price", nullable = false)
    @NotNull
    private BigDecimal price;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public Dish(Dish dish) {
        this(dish.id, dish.name, dish.date, dish.price, dish.restaurant);
    }

    public Dish(Integer id, String name, LocalDate date, BigDecimal price) {
        this(id, name, date, price, null);
    }

    public Dish(Integer id, String name, LocalDate date, BigDecimal price, Restaurant restaurant) {
        super(id, name);
        this.date = date;
        this.price = price;
        this.restaurant = restaurant;
    }
}