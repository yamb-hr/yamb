package com.tejko.yamb.api.payload.responses;

import java.util.List;

import com.tejko.yamb.domain.models.Score;

public class ScoreboardResponse {

    public long gamesPlayed;
    public double averageScore;
    public int topScore;
    public List<ScoreResponse> topToday;
    public List<ScoreResponse> topThisWeek;
    public List<ScoreResponse> topThisMonth;
    public List<ScoreResponse> topThisYear;
    public List<ScoreResponse> topAllTime;

}
