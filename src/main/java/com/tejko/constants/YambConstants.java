package com.tejko.constants;

import com.tejko.models.enums.BoxType;

public class YambConstants {

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

	public static final String ERROR_MESSAGE_ROLL_LIMIT_REACHED = "The roll limit has been reached!";
    public static final String ERROR_MESSAGE_ANNOUNCEMENT_REQUIRED = "An announcement is required!";
    public static final String ERROR_MESSAGE_ANNOUNCEMENT_ALREADY_DECLARED = "An announcement has already been made!";
    public static final String ERROR_MESSAGE_ANNOUNCEMENT_NOT_AVAILABLE = "An announcement is available only after first roll!";
    public static final String ERROR_MESSAGE_BOX_ALREADY_FILLED = "The box has already been filled!";
    public static final String ERROR_MESSAGE_BOX_NOT_AVAILABLE = "The box is not available!";
    public static final String ERROR_MESSAGE_DICE_ROLL_REQUIRED = "To fill a box, the first dice roll is required!";
    public static final String ERROR_MESSAGE_BOX_NOT_ANNOUNCED = "The box is different from announcement!";
    public static final String ERROR_MESSAGE_RESTART_COMPLETED_SHEET = "A completed sheet cannot be restarted!";

}
