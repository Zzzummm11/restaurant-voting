package ru.javaops.bootjava.util;

import lombok.experimental.UtilityClass;
import ru.javaops.bootjava.model.Vote;
import ru.javaops.bootjava.to.VoteTo;

@UtilityClass
public class VoteUtil {

    public static VoteTo createNewFromTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getUser().getId(),vote.getRestaurant().getId(), vote.getDate());
    }
}
