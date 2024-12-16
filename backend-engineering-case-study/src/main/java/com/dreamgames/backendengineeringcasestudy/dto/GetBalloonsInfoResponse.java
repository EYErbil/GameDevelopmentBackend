package com.dreamgames.backendengineeringcasestudy.dto;

public class GetBalloonsInfoResponse {
    private Integer userId;
    private Integer partnerUserId;
    private int balloonInflatedAmount;
    private int heliumCollected;
    private boolean hasPopped;
    private boolean rewardClaimed;

    public Integer getUserId() { return userId; }
    public Integer getPartnerUserId() { return partnerUserId; }
    public int getBalloonInflatedAmount() { return balloonInflatedAmount; }
    public int getHeliumCollected() { return heliumCollected; }
    public boolean isHasPopped() { return hasPopped; }
    public boolean isRewardClaimed() { return rewardClaimed; }

    public void setUserId(Integer userId) { this.userId = userId; }
    public void setPartnerUserId(Integer partnerUserId) { this.partnerUserId = partnerUserId; }
    public void setBalloonInflatedAmount(int balloonInflatedAmount) { this.balloonInflatedAmount = balloonInflatedAmount; }
    public void setHeliumCollected(int heliumCollected) { this.heliumCollected = heliumCollected; }
    public void setHasPopped(boolean hasPopped) { this.hasPopped = hasPopped; }
    public void setRewardClaimed(boolean rewardClaimed) { this.rewardClaimed = rewardClaimed; }
}
