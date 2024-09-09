package com.tejko.yamb.domain.enums;

public enum GameStatus {

    IN_PROGRESS, // represents the state when the game is active and currently being played. The user is still making moves, and the game isn't over yet.
    COMPLETED, // represents the state when the game has been fully played, and all boxes have been filled. The game is over, and the final score can be calculated or displayed.
    ARCHIVED, // represents the state when the user has chosen to start a new game, effectively retiring the old game. The old game is considered finished, and no further actions can be taken on it.

}
