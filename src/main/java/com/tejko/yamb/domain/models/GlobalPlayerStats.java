package com.tejko.yamb.domain.models;

public class GlobalPlayerStats {

    private long playerCount;
    private long mostScoresByAnyPlayer;
    private Player playerWithMostScores;
    private double highestAverageScoreByAnyPlayer;
    private Player playerWithHighestAverageScore;
    private Score highScore;
    private Player newestPlayer;
    private Player oldestPlayer;

    public GlobalPlayerStats() {}
    
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

    public Player getPlayerWithMostScores() {
        return playerWithMostScores;
    }

    public void setPlayerWithMostScores(Player playerWithMostScores) {
        this.playerWithMostScores = playerWithMostScores;
    }

    public double getHighestAverageScoreByAnyPlayer() {
        return highestAverageScoreByAnyPlayer;
    }

    public void setHighestAverageScoreByAnyPlayer(double highestAverageScoreByAnyPlayer) {
        this.highestAverageScoreByAnyPlayer = highestAverageScoreByAnyPlayer;
    }

    public Player getPlayerWithHighestAverageScore() {
        return playerWithHighestAverageScore;
    }

    public void setPlayerWithHighestAverageScore(Player playerWithHighestAverageScore) {
        this.playerWithHighestAverageScore = playerWithHighestAverageScore;
    }

    public Score getHighScore() {
        return highScore;
    }

    public void setHighScore(Score highScore) {
        this.highScore = highScore;
    }

    public Player getNewestPlayer() {
        return newestPlayer;
    }

    public void setNewestPlayer(Player newestPlayer) {
        this.newestPlayer = newestPlayer;
    }

    public Player getOldestPlayer() {
        return oldestPlayer;
    }

    public void setOldestPlayer(Player oldestPlayer) {
        this.oldestPlayer = oldestPlayer;
    }
    
}
