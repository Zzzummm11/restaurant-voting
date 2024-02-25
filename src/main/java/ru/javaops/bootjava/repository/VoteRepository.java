package ru.javaops.bootjava.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.error.NotFoundException;
import ru.javaops.bootjava.model.Vote;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId AND v.date = CURRENT_DATE")
    Optional<Vote> findByUserIdAndCurrentDate(int userId);

    default Vote getVoteForToday(int userId) {
        return findByUserIdAndCurrentDate(userId).orElseThrow(() -> new NotFoundException("User with id=" + userId +
                " hasn't voted yet today"));
    }

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId")
    List<Vote> getAllByUserId(@Param("userId") int userId);
}