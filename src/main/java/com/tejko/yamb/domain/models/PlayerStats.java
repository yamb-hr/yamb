package com.tejko.yamb.domain.models;

import java.time.LocalDateTime;

import com.tejko.yamb.domain.models.entities.Score;

public class PlayerStats {

    private LocalDateTime lastActivity;
    private double averageScore;
    private Score highScore;
    private long scoreCount;

    public PlayerStats() {}

    public PlayerStats(LocalDateTime lastActivity, double averageScore, Score highScore, long scoreCount) {
        this.lastActivity = lastActivity;
        this.averageScore = averageScore;
        this.highScore = highScore;
        this.scoreCount = scoreCount;
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

    public Score getHighScore() {
        return highScore;
    }

    public void setHighScore(Score highScore) {
        this.highScore = highScore;
    }

    public long getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(long scoreCount) {
        this.scoreCount = scoreCount;
    }

}
