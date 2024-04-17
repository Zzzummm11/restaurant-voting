package com.github.zzzummm272.util;

import com.github.zzzummm272.to.VoteTo;
import lombok.experimental.UtilityClass;
import com.github.zzzummm272.model.Vote;

import java.util.List;

@UtilityClass
public class VoteUtil {

    public static VoteTo createNewTo(Vote vote) {
        if (vote == null) {
            return null;
        }
        return new VoteTo(vote.getId(), vote.getUser().getId(), vote.getRestaurant().getId(), vote.getDate());
    }

    public static List<VoteTo> createListTo(List<Vote> votes) {
        return votes.stream().map(VoteUtil::createNewTo).toList();
    }
}
