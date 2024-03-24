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
import ru.javaops.bootjava.web.AuthUser;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static ru.javaops.bootjava.util.VoteUtil.createListFromTo;
import static ru.javaops.bootjava.util.VoteUtil.createNewFromTo;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    static final String REST_URL = "/api/votes";

    protected RestaurantRepository restaurantRepository;

    protected UserRepository userRepository;

    protected VoteRepository voteRepository;

    @GetMapping("/current-vote")
    public VoteTo getCurrent(@AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.getUser().id();
        log.info("get current vote for user with id={}", userId);
        return createNewFromTo(voteRepository.getVoteForToday((userId)));
    }

    @GetMapping
    public List<VoteTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.getUser().id();
        log.info("get all votes for user with id={}", userId);
        return createListFromTo(voteRepository.getAllByUserId(userId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteTo> create(@RequestBody Integer restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.getUser().id();
        log.info("user with id={} votes for the restaurant with id={}", userId, restaurantId);
        restaurantRepository.checkExisted(restaurantId);
        Optional<Vote> vote = voteRepository.findByUserIdAndCurrentDate((userId));
        if (vote.isEmpty()) {
            Vote created = new Vote(userRepository.getReferenceById(userId),
                    restaurantRepository.getReferenceById(restaurantId), LocalDate.now());
            voteRepository.save(created);
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(REST_URL + "/{id}")
                    .buildAndExpand(created.getId()).toUri();
            return ResponseEntity.created(uriOfNewResource).body(createNewFromTo(created));
        } else {
            throw new IllegalRequestDataException("You have already voted");
        }
    }

    @Transactional
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Integer restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.getUser().id();
        log.info("update user's vote with userId={} for the restaurant with id={}", userId, restaurantId);
        restaurantRepository.checkExisted(restaurantId);
        if (LocalTime.now().isBefore(LocalTime.of(11, 0, 0))) {
            Vote vote = voteRepository.getVoteForToday((userId));
            Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
            vote.setRestaurant(restaurant);
            voteRepository.save(vote);
        } else {
            throw new IllegalRequestDataException("It is too late, vote can't be changed");
        }
    }
}
