package ru.javaops.bootjava.util;

import ru.javaops.bootjava.model.Vote;
import ru.javaops.bootjava.to.VoteTo;

public class VoteUtil {

    public static VoteTo createNewFromTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getUser().getId(),vote.getRestaurant().getId(), vote.getDate());
    }
}
