package com.tejko.yamb.api.dto.responses;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

public class GlobalScoreStatsResponse extends RepresentationModel<GlobalScoreStatsResponse> {

    private long scoreCount;
    private double averageScore;
    private ScoreResponse highScore;
    private List<ScoreResponse> topToday;
    private List<ScoreResponse> topThisWeek;
    private List<ScoreResponse> topThisMonth;
    private List<ScoreResponse> topThisYear;
    private List<ScoreResponse> topAllTime;

    public GlobalScoreStatsResponse() {}

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

    public ScoreResponse getHighScore() {
        return highScore;
    }

    public void setHighScore(ScoreResponse highScore) {
        this.highScore = highScore;
    }

    public List<ScoreResponse> getTopToday() {
        return topToday;
    }

    public void setTopToday(List<ScoreResponse> topToday) {
        this.topToday = topToday;
    }

    public List<ScoreResponse> getTopThisWeek() {
        return topThisWeek;
    }

    public void setTopThisWeek(List<ScoreResponse> topThisWeek) {
        this.topThisWeek = topThisWeek;
    }

    public List<ScoreResponse> getTopThisMonth() {
        return topThisMonth;
    }

    public void setTopThisMonth(List<ScoreResponse> topThisMonth) {
        this.topThisMonth = topThisMonth;
    }

    public List<ScoreResponse> getTopThisYear() {
        return topThisYear;
    }

    public void setTopThisYear(List<ScoreResponse> topThisYear) {
        this.topThisYear = topThisYear;
    }

    public List<ScoreResponse> getTopAllTime() {
        return topAllTime;
    }

    public void setTopAllTime(List<ScoreResponse> topAllTime) {
        this.topAllTime = topAllTime;
    }
    
}
