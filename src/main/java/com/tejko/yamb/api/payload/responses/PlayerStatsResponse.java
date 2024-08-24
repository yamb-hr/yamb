package com.tejko.yamb.api.payload.responses;

import java.time.LocalDateTime;

public class PlayerStatsResponse {

    private LocalDateTime lastActivity;
    private double averageScore;
    private int topScore;
    private long gamesPlayed;

    public PlayerStatsResponse() {}

    public PlayerStatsResponse(LocalDateTime lastActivity, double averageScore, int topScore, long gamesPlayed) {
        this.lastActivity = lastActivity;
        this.averageScore = averageScore;
        this.topScore = topScore;
        this.gamesPlayed = gamesPlayed;
    }

    public LocalDateTime getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public int getTopScore() {
        return topScore;
    }

    public void setTopScore(int topScore) {
        this.topScore = topScore;
    }

    public long getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(long gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

}
