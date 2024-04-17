package com.github.zzzummm272.web.vote;

import com.github.zzzummm272.repository.VoteRepository;
import com.github.zzzummm272.to.VoteTo;
import com.github.zzzummm272.util.ClockProvider;
import com.github.zzzummm272.util.DateUtil;
import com.github.zzzummm272.util.JsonUtil;
import com.github.zzzummm272.util.VoteUtil;
import com.github.zzzummm272.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.zzzummm272.model.Vote;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.zzzummm272.web.restaurant.RestaurantTestData.*;
import static com.github.zzzummm272.web.user.UserTestData.*;

@ExtendWith(MockitoExtension.class)
public class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = VoteController.REST_URL + '/';

    @Autowired
    private VoteRepository voteRepository;

    @MockBean
    private ClockProvider clockProvider;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getCurrentForUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "/current-vote"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_TO_MATCHER.contentJson(VoteUtil.createNewTo(VoteTestData.vote1)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getCurrentForAdmin() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "/current-vote"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_TO_MATCHER.contentJson(VoteUtil.createNewTo(VoteTestData.vote2)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllForUser() throws Exception {
        perform(MockMvcRequestBuilders.get(VoteController.REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_TO_MATCHER.contentJson(VoteUtil.createListTo(VoteTestData.allVotesForUser)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllFoAdmin() throws Exception {
        perform(MockMvcRequestBuilders.get(VoteController.REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_TO_MATCHER.contentJson(VoteUtil.createListTo(VoteTestData.allVotesForAdmin)));
    }

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    void create() throws Exception {
        Integer vote = RESTAURANT1_ID;
        ResultActions action = perform(MockMvcRequestBuilders.post(VoteController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(vote)))
                .andDo(print())
                .andExpect(status().isCreated());

        VoteTo created = VoteTestData.VOTE_TO_MATCHER.readFromJson(action);
        int newId = created.getId();
        Vote newVote = new Vote(null, guest, restaurant1, LocalDate.now());
        newVote.setId(newId);

        VoteTestData.VOTE_TO_MATCHER.assertMatch(created, VoteUtil.createNewTo(newVote));
        VoteTestData.VOTE_TO_MATCHER.assertMatch(VoteUtil.createNewTo(voteRepository.getExisted(newId)), created);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createNotFoundRestaurant() throws Exception {
        Integer vote = VoteTestData.NOT_FOUND;
        perform(MockMvcRequestBuilders.post(VoteController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(vote)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicate() throws Exception {
        Integer vote = RESTAURANT1_ID;
        perform(MockMvcRequestBuilders.post(VoteController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(vote)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }


    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateBeforeVoteTime() throws Exception {

        when(clockProvider.getTime()).thenReturn(DateUtil.VOTE_BEFORE_TIME);

        Integer vote = RESTAURANT2_ID;
        perform(MockMvcRequestBuilders.put(VoteController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(vote)))
                .andExpect(status().isNoContent());
        Vote actual = voteRepository.getVoteByDate(ADMIN_ID, DateUtil.CURRENT_DATE);
        VoteTo updateVoteTo = new VoteTo(actual.getId(), ADMIN_ID, RESTAURANT2_ID, LocalDate.now());

        VoteTestData.VOTE_TO_MATCHER.assertMatch(VoteUtil.createNewTo(voteRepository.getExisted(VoteTestData.VOTE1_ID + 1)), updateVoteTo);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateAfterVoteTime() throws Exception {

        when(clockProvider.getTime()).thenReturn(DateUtil.VOTE_AFTER_TIME);

        Integer vote = RESTAURANT2_ID;
        perform(MockMvcRequestBuilders.put(VoteController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(vote)))
                .andExpect(status().isUnprocessableEntity());
    }
}
