package ru.javaops.bootjava.to;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class RestaurantToForAdmin extends NamedTo {

    public RestaurantToForAdmin(Integer id, String name) {
        super(id, name);
    }
}
