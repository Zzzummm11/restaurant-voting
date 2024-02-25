package ru.javaops.bootjava.to;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {

    private int userId;
    @NotNull
    private Integer restaurantId;

    private LocalDate date;

    public VoteTo(Integer id, int userId, Integer restaurantId, LocalDate date) {
        super(id);
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.date = date;
    }
}