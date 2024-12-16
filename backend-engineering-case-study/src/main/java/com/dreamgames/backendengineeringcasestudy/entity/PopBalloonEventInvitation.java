package com.dreamgames.backendengineeringcasestudy.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pop_balloon_event_invitations")
public class PopBalloonEventInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "inviter_user_id", nullable = false)
    private Integer inviterUserId;

    @Column(name = "invitee_user_id", nullable = false)
    private Integer inviteeUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InvitationStatus status = InvitationStatus.PENDING;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    // Getters and Setters
    public Integer getId() {
        return id;
    }
    public Integer getInviterUserId() {
        return inviterUserId;
    }
    public Integer getInviteeUserId() {
        return inviteeUserId;
    }
    public InvitationStatus getStatus() {
        return status;
    }
    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void setInviterUserId(Integer inviterUserId) {
        this.inviterUserId = inviterUserId;
    }
    public void setInviteeUserId(Integer inviteeUserId) {
        this.inviteeUserId = inviteeUserId;
    }
    public void setStatus(InvitationStatus status) {
        this.status = status;
    }
    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }
}
