package ru.javaops.bootjava.web.vote;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.bootjava.model.Vote;
import ru.javaops.bootjava.repository.VoteRepository;
import ru.javaops.bootjava.to.VoteTo;
import ru.javaops.bootjava.util.ClockProvider;
import ru.javaops.bootjava.util.JsonUtil;
import ru.javaops.bootjava.util.VoteUtil;
import ru.javaops.bootjava.web.AbstractControllerTest;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.bootjava.util.DateUtil.*;
import static ru.javaops.bootjava.util.VoteUtil.createListTo;
import static ru.javaops.bootjava.util.VoteUtil.createNewTo;
import static ru.javaops.bootjava.web.restaurant.RestaurantTestData.*;
import static ru.javaops.bootjava.web.user.UserTestData.*;
import static ru.javaops.bootjava.web.vote.VoteController.REST_URL;
import static ru.javaops.bootjava.web.vote.VoteTestData.NOT_FOUND;
import static ru.javaops.bootjava.web.vote.VoteTestData.*;

@ExtendWith(MockitoExtension.class)
public class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

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
                .andExpect(VOTE_TO_MATCHER.contentJson(createNewTo(vote1)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getCurrentForAdmin() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "/current-vote"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(createNewTo(vote2)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllForUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(createListTo(allVotesForUser)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllFoAdmin() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(createListTo(allVotesForAdmin)));
    }

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    void create() throws Exception {
        Integer vote = RESTAURANT1_ID;
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(vote)))
                .andDo(print())
                .andExpect(status().isCreated());

        VoteTo created = VOTE_TO_MATCHER.readFromJson(action);
        int newId = created.getId();
        Vote newVote = new Vote(null, guest, restaurant1, LocalDate.now());
        newVote.setId(newId);

        VOTE_TO_MATCHER.assertMatch(created, createNewTo(newVote));
        VOTE_TO_MATCHER.assertMatch(createNewTo(voteRepository.getExisted(newId)), created);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createNotFoundRestaurant() throws Exception {
        Integer vote = NOT_FOUND;
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(vote)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicate() throws Exception {
        Integer vote = RESTAURANT1_ID;
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(vote)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }


    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateBeforeVoteTime() throws Exception {

        when(clockProvider.getTime()).thenReturn(VOTE_BEFORE_TIME);

        Integer vote = RESTAURANT2_ID;
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(vote)))
                .andExpect(status().isNoContent());
        Vote actual = voteRepository.getVoteByDate(ADMIN_ID, CURRENT_DATE);
        VoteTo updateVoteTo = new VoteTo(actual.getId(), ADMIN_ID, RESTAURANT2_ID, LocalDate.now());

        VOTE_TO_MATCHER.assertMatch(VoteUtil.createNewTo(voteRepository.getExisted(VOTE1_ID + 1)), updateVoteTo);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateAfterVoteTime() throws Exception {

        when(clockProvider.getTime()).thenReturn(VOTE_AFTER_TIME);

        Integer vote = RESTAURANT2_ID;
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(vote)))
                .andExpect(status().isUnprocessableEntity());
    }
}
