package ru.javaops.bootjava.web.vote;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.error.IllegalRequestDataException;
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.model.Vote;
import ru.javaops.bootjava.repository.RestaurantRepository;
import ru.javaops.bootjava.repository.UserRepository;
import ru.javaops.bootjava.repository.VoteRepository;
import ru.javaops.bootjava.to.VoteTo;
import ru.javaops.bootjava.util.ClockProvider;
import ru.javaops.bootjava.web.AuthUser;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static ru.javaops.bootjava.util.DateUtil.CURRENT_DATE;
import static ru.javaops.bootjava.util.DateUtil.VOTE_TIME;
import static ru.javaops.bootjava.util.VoteUtil.createListTo;
import static ru.javaops.bootjava.util.VoteUtil.createNewTo;

@Slf4j
@RestController
@AllArgsConstructor
@Transactional(readOnly = true)
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    static final String REST_URL = "/api/votes";

    private RestaurantRepository restaurantRepository;

    private UserRepository userRepository;

    private VoteRepository voteRepository;

    private ClockProvider clockProvider;

    @GetMapping("/current-vote")
    public VoteTo getCurrent(@AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.getUser().id();
        log.info("get current vote for user with id={}", userId);
        return createNewTo(voteRepository.getVoteByDate(userId, CURRENT_DATE));
    }

    @GetMapping
    public List<VoteTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.getUser().id();
        log.info("get all votes for user with id={}", userId);
        return createListTo(voteRepository.getAllByUserId(userId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<VoteTo> create(@RequestBody Integer restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.getUser().id();
        log.info("user with id={} votes for the restaurant with id={}", userId, restaurantId);
        restaurantRepository.checkExisted(restaurantId);
        Optional<Vote> vote = voteRepository.findByUserIdAndDate(userId, CURRENT_DATE);
        if (vote.isEmpty()) {
            Vote created = new Vote(userRepository.getReferenceById(userId),
                    restaurantRepository.getReferenceById(restaurantId), LocalDate.now());
            voteRepository.save(created);
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(REST_URL + "/{id}")
                    .buildAndExpand(created.getId()).toUri();
            return ResponseEntity.created(uriOfNewResource).body(createNewTo(created));
        } else {
            throw new IllegalRequestDataException("You have already voted");
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@RequestBody Integer restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.getUser().id();
        log.info("update user's vote with userId={} for the restaurant with id={}", userId, restaurantId);
        restaurantRepository.checkExisted(restaurantId);
        if (clockProvider.getTime().isBefore(VOTE_TIME)) {
            Vote vote = voteRepository.getVoteByDate(userId, CURRENT_DATE);
            if (vote != null) {
                Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
                vote.setRestaurant(restaurant);
                voteRepository.save(vote);
            }
        } else {
            throw new IllegalRequestDataException("It is too late, vote can't be changed");
        }
    }
}