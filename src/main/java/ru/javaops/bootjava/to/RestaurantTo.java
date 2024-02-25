package ru.javaops.bootjava.to;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.javaops.bootjava.model.Dish;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo {

    private List<Dish> dishes;

    private Integer votes;

    public RestaurantTo(Integer id, String name, List<Dish> dishes) {
        super(id, name);
        this.dishes = dishes;
    }
}
