package com.tejko.yamb.api.payload.responses;

import java.util.List;

import com.tejko.yamb.domain.models.Score;

public class ScoreboardResponse {

    private List<Score> topToday;
    private List<Score> topThisWeek;
    private List<Score> topThisMonth;
    private List<Score> topThisYear;
    private List<Score> topAllTime;

    public ScoreboardResponse(List<Score> topToday, List<Score> topThisWeek, List<Score> topThisMonth, List<Score> topThisYear, List<Score> topAllTime) {
        this.topToday = topToday;
        this.topThisWeek = topThisWeek;
        this.topThisMonth = topThisMonth;
        this.topThisYear = topThisYear;
        this.topAllTime = topAllTime;
    }
    
    public List<Score> getTopToday() {
        return topToday;
    }

    public List<Score> getTopThisWeek() {
        return topThisWeek;
    }

    public List<Score> getTopThisMonth() {
        return topThisMonth;
    }

    public List<Score> getTopThisYear() {
        return topThisYear;
    }

    public List<Score> getTopAllTime() {
        return topAllTime;
    }

}
