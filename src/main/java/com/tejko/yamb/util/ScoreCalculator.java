package com.tejko.yamb.util;

import org.hibernate.cfg.NotYetImplementedException;

import com.tejko.yamb.domain.constants.GameConstants;
import com.tejko.yamb.domain.enums.BoxType;

public class ScoreCalculator {

	public static int calculateScore(int[] diceValues, BoxType boxType) {
		switch (boxType) {
			case ONES:
			case TWOS:
			case THREES:
			case FOURS:
			case FIVES:
			case SIXES:
				return calculateSum(diceValues, boxType);
			case MAX:
			case MIN:
				return calculateSum(diceValues);
			case TRIPS:
				return calculateTrips(diceValues);
			case STRAIGHT:
				return calculateStraight(diceValues);
			case BOAT:
				return calculateBoat(diceValues);
			case CARRIAGE:
				return calculateCarriage(diceValues);
			case YAMB:
				return calculateYamb(diceValues);
			default: 
			 	throw new NotYetImplementedException();
		}
	}

	// returns sum of all dice values
	private static int calculateSum(int[] diceValues) {
		int sum = 0;
        for (int dice : diceValues) {
            sum += dice;
        }
        return sum;
	}

	// returns sum of dice  values that equal the selected box type 
	// only applicable for box types [ONES-SIXES]
	private static int calculateSum(int[] diceValues, BoxType boxType) {
		int sum = 0;
        for (int value : diceValues) {
			// if dice value is equal to box type add to sum
            if (value == (boxType.ordinal() + 1)) {
                sum += value;; 
            }
        }
        return sum;
	}

	private static int calculateTrips(int[] diceValues) {
		int sum = calculateRecurringValueSum(diceValues, 3);
		if (sum > 0) {
			sum += GameConstants.BONUS_TRIPS;
		}
		return sum;
	}

	// returns bonus scores if dice values contain small or large straights
	// small straight [1, 2, 3, 4 , 5]
	// large straight [2, 3, 4, 5, 6]
	private static int calculateStraight(int[] diceValues) {
		Boolean[] foundValues = { false, false, false, false, false, false };
		for (int value : diceValues) {
			foundValues[value - 1] = true;
		}
		if (foundValues[0] && foundValues[1] && foundValues[2] && foundValues[3] && foundValues[4]) {
			return GameConstants.BONUS_STRAIGHT_SMALL;
		} else if (foundValues[1] && foundValues[2] && foundValues[3] && foundValues[4] && foundValues[5]) {
			return GameConstants.BONUS_STRAIGHT_LARGE;
		}
		return 0;
	}
	
	// boat consists of a pair and trips
	public static int calculateBoat(int[] diceValues) {
		int tripsSum = calculateRecurringValueSum(diceValues, 3);
		if (tripsSum > 0) {
			int[] remainingDiceValues = new int[3];
			int i = 0;
			for (int value : diceValues) {
				if (value != tripsSum / 3) {
					remainingDiceValues[i++] = value;
				}
			}
			int pairSum = calculateRecurringValueSum(remainingDiceValues, 2);
			if (pairSum > 0) {
				return pairSum + tripsSum + GameConstants.BONUS_BOAT;
			}
		}
        return 0;
	}

	// four of a kind
	private static int calculateCarriage(int[] diceValues) {
		int sum = calculateRecurringValueSum(diceValues, 4);
		if (sum > 0) {
			sum += GameConstants.BONUS_CARRIAGE;
		}
		return sum;
	}

	// five of a kind
	private static int calculateYamb(int[] diceValues) {
		int sum = calculateRecurringValueSum(diceValues, 5);
		if (sum > 0) {
			sum += GameConstants.BONUS_YAMB;
		}
		return sum;
	}

	// returns sum of dice values if the number of times that the value ocurrs is equal to or larger than the threshold
	// values exceeding the threshold are ignored 
	private static int calculateRecurringValueSum(int[] diceValues, int threshold) {
		int sum = 0;
        for (int i = 1; i <= 6; i++) {
            int count = 0;
            for (int value : diceValues) {
                if (value == i) {
                    count++;
                }
            }
			// if count has reached given number return sum increased by given bonus
            if (count >= threshold) {
                sum = i * threshold; // multiply by repeat number instead of count (having 4 dice of same value has no added value for TRIPS)
                break;
            }
        }
        return sum; // if count has not reached given number for any dice value return 0
	}

}
