package com.dreamgames.backendengineeringcasestudy.repository;

import com.dreamgames.backendengineeringcasestudy.entity.InvitationStatus;
import com.dreamgames.backendengineeringcasestudy.entity.PopBalloonEventInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PopBalloonEventInvitationRepository extends JpaRepository<PopBalloonEventInvitation, Integer> {
    List<PopBalloonEventInvitation> findByInviteeUserIdAndEventDateAndStatus(Integer inviteeUserId, LocalDate eventDate, InvitationStatus status);

    List<PopBalloonEventInvitation> findByInviterUserIdAndEventDateAndStatus(Integer inviterUserId, LocalDate eventDate, InvitationStatus status);

    PopBalloonEventInvitation findByIdAndEventDate(Integer id, LocalDate eventDate);
}
