package com.tejko.yamb.domain.models;

import java.util.List;

import com.tejko.yamb.domain.models.entities.Score;

public class GlobalScoreStats {

    private long scoreCount;
    private double averageScore;
    private Score highScore;
    private List<Score> topToday;
    private List<Score> topThisWeek;
    private List<Score> topThisMonth;
    private List<Score> topThisYear;
    private List<Score> topAllTime;

    public GlobalScoreStats() {}

    public long getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(long scoreCount) {
        this.scoreCount = scoreCount;
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

    public List<Score> getTopToday() {
        return topToday;
    }

    public void setTopToday(List<Score> topToday) {
        this.topToday = topToday;
    }

    public List<Score> getTopThisWeek() {
        return topThisWeek;
    }

    public void setTopThisWeek(List<Score> topThisWeek) {
        this.topThisWeek = topThisWeek;
    }

    public List<Score> getTopThisMonth() {
        return topThisMonth;
    }

    public void setTopThisMonth(List<Score> topThisMonth) {
        this.topThisMonth = topThisMonth;
    }

    public List<Score> getTopThisYear() {
        return topThisYear;
    }

    public void setTopThisYear(List<Score> topThisYear) {
        this.topThisYear = topThisYear;
    }

    public List<Score> getTopAllTime() {
        return topAllTime;
    }

    public void setTopAllTime(List<Score> topAllTime) {
        this.topAllTime = topAllTime;
    }
    
}
