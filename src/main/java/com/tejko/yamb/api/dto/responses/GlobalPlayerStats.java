package com.tejko.yamb.api.dto.responses;

public class GlobalPlayerStats {

    private long playerCount;
    private long mostScoresByAnyPlayer;
    private PlayerResponse playerWithMostScores;
    private double highestAverageScoreByAnyPlayer;
    private PlayerResponse playerWithHighestAverageScore;
    private ScoreResponse highScore;
    private PlayerResponse newestPlayer;
    private PlayerResponse oldestPlayer;

    public GlobalPlayerStats() {}

    public GlobalPlayerStats(long playerCount, long mostScoresByAnyPlayer, PlayerResponse playerWithMostScores,
            double highestAverageScoreByAnyPlayer, PlayerResponse playerWithHighestAverageScore, ScoreResponse highScore,
            PlayerResponse newestPlayer, PlayerResponse oldestPlayer) {
        this.playerCount = playerCount;
        this.mostScoresByAnyPlayer = mostScoresByAnyPlayer;
        this.playerWithMostScores = playerWithMostScores;
        this.highestAverageScoreByAnyPlayer = highestAverageScoreByAnyPlayer;
        this.playerWithHighestAverageScore = playerWithHighestAverageScore;
        this.highScore = highScore;
        this.newestPlayer = newestPlayer;
        this.oldestPlayer = oldestPlayer;
    }

    public long getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(long playerCount) {
        this.playerCount = playerCount;
    }

    public long getMostScoresByAnyPlayer() {
        return mostScoresByAnyPlayer;
    }

    public void setMostScoresByAnyPlayer(long mostScoresByAnyPlayer) {
        this.mostScoresByAnyPlayer = mostScoresByAnyPlayer;
    }

    public PlayerResponse getPlayerWithMostScores() {
        return playerWithMostScores;
    }

    public void setPlayerWithMostScores(PlayerResponse playerWithMostScores) {
        this.playerWithMostScores = playerWithMostScores;
    }

    public double getHighestAverageScoreByAnyPlayer() {
        return highestAverageScoreByAnyPlayer;
    }

    public void setHighestAverageScoreByAnyPlayer(double highestAverageScoreByAnyPlayer) {
        this.highestAverageScoreByAnyPlayer = highestAverageScoreByAnyPlayer;
    }

    public PlayerResponse getPlayerWithHighestAverageScore() {
        return playerWithHighestAverageScore;
    }

    public void setPlayerWithHighestAverageScore(PlayerResponse playerWithHighestAverageScore) {
        this.playerWithHighestAverageScore = playerWithHighestAverageScore;
    }

    public ScoreResponse getHighScore() {
        return highScore;
    }

    public void setHighScore(ScoreResponse highScore) {
        this.highScore = highScore;
    }

    public PlayerResponse getNewestPlayer() {
        return newestPlayer;
    }

    public void setNewestPlayer(PlayerResponse newestPlayer) {
        this.newestPlayer = newestPlayer;
    }

    public PlayerResponse getOldestPlayer() {
        return oldestPlayer;
    }

    public void setOldestPlayer(PlayerResponse oldestPlayer) {
        this.oldestPlayer = oldestPlayer;
    }
    
}
