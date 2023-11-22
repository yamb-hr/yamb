package com.tejko.constants;

import com.tejko.models.enums.BoxType;

public class GameConstants {

	public static final int DICE_ROLL_LIMIT = 3;
	public static final int DICE_LIMIT = 5;

	public static final int BONUS_TRIPS = 10;
	public static final int BONUS_STRAIGHT_SMALL = 35;
	public static final int BONUS_STRAIGHT_LARGE = 45;
	public static final int BONUS_BOAT = 30;
	public static final int BONUS_CARRIAGE = 40;
	public static final int BONUS_YAMB = 50;

	public static final int TOP_SECTION_SUM_BONUS_THRESHOLD = 60;
	public static final int TOP_SECTION_SUM_BONUS = 30;

	public static final BoxType[] TOP_SECTION = {
		BoxType.ONES,
		BoxType.TWOS,
		BoxType.THREES,
		BoxType.FOURS, 
		BoxType.FIVES, 
		BoxType.SIXES
	};

	public static final BoxType[] BOTTOM_SECTION = {
		BoxType.TRIPS,
		BoxType.STRAIGHT,
		BoxType.CARRIAGE,
		BoxType.BOAT,
		BoxType.YAMB
	};


	public static final String ERROR_BAD_REQUEST = "Bad Request";
	public static final String ERROR_FORBIDDEN = "Forbidden";
	public static final String ERROR_USERNAME_OR_PASSWORD_INCORRECT = "The username or password is incorrect!";
	public static final String ERROR_USER_ALREADY_EXISTS = "A user with that username already exists!";
	public static final String ERROR_PLAYER_NOT_FOUND = "Player not found!";
	public static final String ERROR_SCORE_NOT_FOUND = "Score not found!";
	public static final String ERROR_GAME_NOT_FOUND = "Game not found!";
	public static final String ERROR_ROLL_LIMIT_REACHED = "The roll limit has been reached!";
    public static final String ERROR_ANNOUNCEMENT_REQUIRED = "An announcement is required!";
    public static final String ERROR_ANNOUNCEMENT_ALREADY_DECLARED = "An announcement has already been made!";
    public static final String ERROR_ANNOUNCEMENT_NOT_AVAILABLE = "An announcement is available only after first roll!";
    public static final String ERROR_BOX_ALREADY_FILLED = "The box has already been filled!";
    public static final String ERROR_BOX_NOT_AVAILABLE = "The box is not available!";
    public static final String ERROR_DICE_ROLL_REQUIRED = "To fill a box, the first dice roll is required!";
    public static final String ERROR_BOX_NOT_ANNOUNCED = "The box is different from announcement!";
    public static final String ERROR_RESTART_COMPLETED_SHEET = "A completed sheet cannot be restarted!";

}
