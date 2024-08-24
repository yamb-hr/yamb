package com.tejko.yamb.api.payload.responses;

import java.util.List;

public class ScoreboardResponse {

    private long gamesPlayed;
    private double averageScore;
    private int topScore;
    private List<ScoreResponse> topToday;
    private List<ScoreResponse> topThisWeek;
    private List<ScoreResponse> topThisMonth;
    private List<ScoreResponse> topThisYear;
    private List<ScoreResponse> topAllTime;

    public ScoreboardResponse() {}

    public ScoreboardResponse(long gamesPlayed, double averageScore, int topScore, List<ScoreResponse> topToday, List<ScoreResponse> topThisWeek, List<ScoreResponse> topThisMonth, List<ScoreResponse> topThisYear, List<ScoreResponse> topAllTime) {
        this.gamesPlayed = gamesPlayed;
        this.averageScore = averageScore;
        this.topScore = topScore;
        this.topToday = topToday;
        this.topThisWeek = topThisWeek;
        this.topThisMonth = topThisMonth;
        this.topThisYear = topThisYear;
        this.topAllTime = topAllTime;
    }

    public long getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(long gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
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
