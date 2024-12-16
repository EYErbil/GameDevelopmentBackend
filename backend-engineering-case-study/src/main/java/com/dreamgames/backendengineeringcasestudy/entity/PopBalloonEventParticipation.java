package com.dreamgames.backendengineeringcasestudy.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pop_balloon_event_participation")
public class PopBalloonEventParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Integer userId;

    @Column(name = "partner_user_id")
    private Integer partnerUserId;

    @Column(name = "has_partner", nullable = false)
    private boolean hasPartner = false;

    @Column(name = "helium_collected", nullable = false)
    private int heliumCollected = 0;

    @Column(name = "balloon_inflated_amount", nullable = false)
    private int balloonInflatedAmount = 0;

    @Column(name = "has_popped", nullable = false)
    private boolean hasPopped = false;

    @Column(name = "reward_claimed", nullable = false)
    private boolean rewardClaimed = false;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    // Getters and Setters
    public Integer getId() {
        return id;
    }
    public Integer getUserId() {
        return userId;
    }
    public Integer getPartnerUserId() {
        return partnerUserId;
    }
    public boolean isHasPartner() {
        return hasPartner;
    }
    public int getHeliumCollected() {
        return heliumCollected;
    }
    public int getBalloonInflatedAmount() {
        return balloonInflatedAmount;
    }
    public boolean isHasPopped() {
        return hasPopped;
    }
    public boolean isRewardClaimed() {
        return rewardClaimed;
    }
    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public void setPartnerUserId(Integer partnerUserId) {
        this.partnerUserId = partnerUserId;
    }
    public void setHasPartner(boolean hasPartner) {
        this.hasPartner = hasPartner;
    }
    public void setHeliumCollected(int heliumCollected) {
        this.heliumCollected = heliumCollected;
    }
    public void setBalloonInflatedAmount(int balloonInflatedAmount) {
        this.balloonInflatedAmount = balloonInflatedAmount;
    }
    public void setHasPopped(boolean hasPopped) {
        this.hasPopped = hasPopped;
    }
    public void setRewardClaimed(boolean rewardClaimed) {
        this.rewardClaimed = rewardClaimed;
    }
    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }
}
