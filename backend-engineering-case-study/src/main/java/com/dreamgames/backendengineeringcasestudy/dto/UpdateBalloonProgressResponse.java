package com.dreamgames.backendengineeringcasestudy.dto;

public class UpdateBalloonProgressResponse {
    private int balloonInflatedAmount;
    private boolean popped;

    public int getBalloonInflatedAmount() { return balloonInflatedAmount; }
    public boolean isPopped() { return popped; }

    public void setBalloonInflatedAmount(int balloonInflatedAmount) { this.balloonInflatedAmount = balloonInflatedAmount; }
    public void setPopped(boolean popped) { this.popped = popped; }
}