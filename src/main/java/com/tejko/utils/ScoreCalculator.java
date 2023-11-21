package com.tejko.utils;

import java.util.Collection;

import com.tejko.constants.YambConstants;
import com.tejko.exceptions.UnknownBoxTypeException;
import com.tejko.models.Dice;
import com.tejko.models.enums.BoxType;

public class ScoreCalculator {

	public static int calculateScore(Collection<Dice> diceList, BoxType boxType) {
		switch (boxType) {
			case ONES:
			case TWOS:
			case THREES:
			case FOURS:
			case FIVES:
			case SIXES:
				return calculateSum(diceList, boxType);
			case MAX:
			case MIN:
				return calculateSum(diceList);
			case TRIPS:
				return calculateTrips(diceList);
			case STRAIGHT:
				return calculateStraight(diceList);
			case BOAT:
				return calculateBoat(diceList);
			case CARRIAGE:
				return calculateCarriage(diceList);
			case YAMB:
				return calculateYamb(diceList);
			default:
				throw new UnknownBoxTypeException("Unknown Box Type: " + boxType);
		}
	}

	// returns sum of all dice values
	private static int calculateSum(Collection<Dice> diceList) {
		int sum = 0;
        for (Dice dice : diceList) {
            sum += dice.getValue();
        }
        return sum;
	}

	// returns sum of dice  values that equal the selected box type 
	// only applicable for box types [ONES-SIXES]
	private static int calculateSum(Collection<Dice> diceList, BoxType boxType) {
		int sum = 0;
        for (Dice dice : diceList) {
			// if dice value is equal to box type add to sum
            if (dice.getValue() == (boxType.ordinal() + 1)) {
                sum += dice.getValue();; 
            }
        }
        return sum;
	}

	private static int calculateTrips(Collection<Dice> diceList) {
		return calculateRecurringValueSum(diceList, 3) + YambConstants.BONUS_TRIPS;
	}

	// returns bonus scores if dice values contain small or large straights
	// small straight [1, 2, 3, 4 , 5]
	// large straight [2, 3, 4, 5, 6]
	private static int calculateStraight(Collection<Dice> diceList) {
		Boolean[] foundValues = { false, false, false, false, false, false };
		for (Dice dice : diceList) {
			foundValues[dice.getValue()] = true;
		}
		if (foundValues[0] && foundValues[1] && foundValues[2] && foundValues[3] && foundValues[4]) {
			return YambConstants.BONUS_STRAIGHT_SMALL;
		} else if (foundValues[1] && foundValues[2] && foundValues[3] && foundValues[4] && foundValues[5]) {
			return YambConstants.BONUS_STRAIGHT_LARGE;
		}
		return 0;
	}
	
	// checks if two distinct dice values repeat 2 and 3 times respectively
	public static int calculateBoat(Collection<Dice> diceList) {
		int pairSum = calculateRecurringValueSum(diceList, 2);
		int tripsSum = calculateRecurringValueSum(diceList, 3);
		
        if (pairSum > 0 && tripsSum > 0 && (pairSum / 2) != (tripsSum / 3)) {
            return pairSum + tripsSum + YambConstants.BONUS_BOAT;
        }
        return 0;
	}

	private static int calculateCarriage(Collection<Dice> diceList) {
		return calculateRecurringValueSum(diceList, 4) + YambConstants.BONUS_CARRIAGE;
	}

	private static int calculateYamb(Collection<Dice> diceList) {
		return calculateRecurringValueSum(diceList, 5) + YambConstants.BONUS_YAMB;
	}

	// returns sum of dice values if the number of times that the value ocurrs is equal to or larger than the repeat threshold
	// values exceeding the threshold are ignored 
	private static int calculateRecurringValueSum(Collection<Dice> diceList, int threshold) {
		int sum = 0;
        for (Integer i = 1; i <= 6; i++) {
            int count = 0;
            for (Dice dice : diceList) {
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