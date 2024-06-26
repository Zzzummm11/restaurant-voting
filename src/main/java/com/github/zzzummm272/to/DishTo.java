package com.github.zzzummm272.to;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class DishTo extends NamedTo {
    @NotNull
    private BigDecimal price;

    private LocalDate date;

    public DishTo(Integer id, String name, BigDecimal price) {
        super(id, name);
        this.price = price;
    }
}
