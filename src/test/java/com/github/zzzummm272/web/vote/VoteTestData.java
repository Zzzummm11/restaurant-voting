package com.github.zzzummm272.web.vote;

import com.github.zzzummm272.model.Vote;
import com.github.zzzummm272.to.VoteTo;
import com.github.zzzummm272.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static com.github.zzzummm272.web.restaurant.RestaurantTestData.*;
import static com.github.zzzummm272.web.user.UserTestData.admin;
import static com.github.zzzummm272.web.user.UserTestData.user;

public class VoteTestData {
    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatcherFactory.usingEqualsComparator(VoteTo.class);

    public static final int VOTE1_ID = 1;

    public static final int NOT_FOUND = 100;

    public static final Vote vote1 = new Vote(VOTE1_ID, user, restaurant1, LocalDate.now());
    public static final Vote vote2 = new Vote(VOTE1_ID + 1, admin, restaurant1, LocalDate.now());
    public static final Vote vote3 = new Vote(VOTE1_ID + 2, user, restaurant3, LocalDate.of(2024, 2, 4));
    public static final Vote vote4 = new Vote(VOTE1_ID + 3, admin, restaurant2, LocalDate.of(2024, 2, 4));

    public static final List<Vote> allVotesForUser = List.of(vote1, vote3);
    public static final List<Vote> allVotesForAdmin = List.of(vote2, vote4);
}
