package com.dreamgames.backendengineeringcasestudy.dto;

public class UpdateBalloonProgressResponse {
    private int balloonInflatedAmount; // user’s individual inflated amount
    private boolean popped;
    private int userContribution;
    private int partnerContribution;
    private int totalInflation;

    public int getBalloonInflatedAmount() { return balloonInflatedAmount; }
    public boolean isPopped() { return popped; }
    public int getUserContribution() { return userContribution; }
    public int getPartnerContribution() { return partnerContribution; }
    public int getTotalInflation() { return totalInflation; }

    public void setBalloonInflatedAmount(int balloonInflatedAmount) { this.balloonInflatedAmount = balloonInflatedAmount; }
    public void setPopped(boolean popped) { this.popped = popped; }
    public void setUserContribution(int userContribution) { this.userContribution = userContribution; }
    public void setPartnerContribution(int partnerContribution) { this.partnerContribution = partnerContribution; }
    public void setTotalInflation(int totalInflation) { this.totalInflation = totalInflation; }
}
