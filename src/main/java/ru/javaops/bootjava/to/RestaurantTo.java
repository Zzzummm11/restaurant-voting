package ru.javaops.bootjava.to;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.javaops.bootjava.model.Dish;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantTo extends NamedTo {

    private List<Dish> dishes;

    private Integer votes;

    public RestaurantTo(Integer id, String name) {
        super(id, name);
    }
}
