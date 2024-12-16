package com.dreamgames.backendengineeringcasestudy.repository;

import com.dreamgames.backendengineeringcasestudy.entity.PopBalloonEventParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dreamgames.backendengineeringcasestudy.entity.AbGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface PopBalloonEventParticipationRepository extends JpaRepository<PopBalloonEventParticipation, Integer> {
    Optional<PopBalloonEventParticipation> findByUserIdAndEventDate(Integer userId, LocalDate eventDate);

    List<PopBalloonEventParticipation> findByEventDateAndHasPartnerAndHasPopped(LocalDate eventDate, boolean hasPartner, boolean hasPopped);

    // New JPQL query for more efficient suggestions
    @Query("SELECT p.userId FROM PopBalloonEventParticipation p JOIN User u ON u.id = p.userId " +
            "WHERE p.eventDate = :eventDate " +
            "AND p.hasPartner = false " +
            "AND p.hasPopped = false " +
            "AND u.abGroup = :abGroup " +
            "AND u.id <> :currentUserId")
    List<Integer> findEligibleSuggestions(@Param("eventDate") LocalDate eventDate,
                                          @Param("abGroup") AbGroup abGroup,
                                          @Param("currentUserId") Integer currentUserId);
}
