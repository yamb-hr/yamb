package com.tejko.util;

import java.util.Collection;

import com.tejko.constants.GameConstants;
import com.tejko.models.Dice;
import com.tejko.models.enums.BoxType;

public class ScoreCalculator {

	public static int calculateScore(BoxType boxType, Collection<Dice> dices) {
		switch (boxType) {
			case ONES:
			case TWOS:
			case THREES:
			case FOURS:
			case FIVES:
			case SIXES:
				return calculateSum(dices, boxType);
			case MAX:
			case MIN:
				return calculateSum(dices);
			case TRIPS:
				return calculateTrips(dices);
			case STRAIGHT:
				return calculateStraight(dices);
			case BOAT:
				return calculateBoat(dices);
			case CARRIAGE:
				return calculateCarriage(dices);
			case YAMB:
				return calculateYamb(dices);
			default: 
			 	throw new IllegalArgumentException("Invalid BoxType: " + boxType);
		}
	}

	// returns sum of all dice values
	private static int calculateSum(Collection<Dice> dices) {
		int sum = 0;
        for (Dice dice : dices) {
            sum += dice.getValue();
        }
        return sum;
	}

	// returns sum of dice  values that equal the selected box type 
	// only applicable for box types [ONES-SIXES]
	private static int calculateSum(Collection<Dice> dices, BoxType boxType) {
		int sum = 0;
        for (Dice dice : dices) {
			// if dice value is equal to box type add to sum
            if (dice.getValue() == (boxType.ordinal() + 1)) {
                sum += dice.getValue();; 
            }
        }
        return sum;
	}

	private static int calculateTrips(Collection<Dice> dices) {
		return calculateRecurringValueSum(dices, 3) + GameConstants.BONUS_TRIPS;
	}

	// returns bonus scores if dice values contain small or large straights
	// small straight [1, 2, 3, 4 , 5]
	// large straight [2, 3, 4, 5, 6]
	private static int calculateStraight(Collection<Dice> dices) {
		Boolean[] foundValues = { false, false, false, false, false, false };
		for (Dice dice : dices) {
			foundValues[dice.getValue()] = true;
		}
		if (foundValues[0] && foundValues[1] && foundValues[2] && foundValues[3] && foundValues[4]) {
			return GameConstants.BONUS_STRAIGHT_SMALL;
		} else if (foundValues[1] && foundValues[2] && foundValues[3] && foundValues[4] && foundValues[5]) {
			return GameConstants.BONUS_STRAIGHT_LARGE;
		}
		return 0;
	}
	
	// boat consists of a pair and trips
	public static int calculateBoat(Collection<Dice> dices) {
		int pairSum = calculateRecurringValueSum(dices, 2);
		int tripsSum = calculateRecurringValueSum(dices, 3);
        if (pairSum > 0 && tripsSum > 0 && (pairSum / 2) != (tripsSum / 3)) {
            return pairSum + tripsSum + GameConstants.BONUS_BOAT;
        }
        return 0;
	}

	// four of a kind
	private static int calculateCarriage(Collection<Dice> dices) {
		return calculateRecurringValueSum(dices, 4) + GameConstants.BONUS_CARRIAGE;
	}

	// five of a kind
	private static int calculateYamb(Collection<Dice> dices) {
		return calculateRecurringValueSum(dices, 5) + GameConstants.BONUS_YAMB;
	}

	// returns sum of dice values if the number of times that the value ocurrs is equal to or larger than the threshold
	// values exceeding the threshold are ignored 
	private static int calculateRecurringValueSum(Collection<Dice> dices, int threshold) {
		int sum = 0;
        for (Integer i = 1; i <= 6; i++) {
            int count = 0;
            for (Dice dice : dices) {
                if (dice.getValue() == i) {
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
