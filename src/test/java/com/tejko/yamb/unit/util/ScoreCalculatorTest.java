package com.tejko.yamb.unit.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.tejko.yamb.domain.enums.BoxType;
import com.tejko.yamb.util.ScoreCalculator;

public class ScoreCalculatorTest {

    @Test
    public void testCalculateSum() {
        int[] diceValues = {6, 6, 6, 6, 6};
        Assertions.assertEquals(30, ScoreCalculator.calculateScore(diceValues, BoxType.MAX));
    }

    @Test
    void testCalculateSumByBoxType() {
        int[] diceValues = {6, 6, 6, 1, 1};
        Assertions.assertEquals(18, ScoreCalculator.calculateScore(diceValues, BoxType.SIXES));
    }

    @Test
    void testCalculateSumByBoxTypeWithoutSpecifiedBox() {
        int[] diceValues = {1, 1, 1, 1, 1};
        Assertions.assertEquals(0, ScoreCalculator.calculateScore(diceValues, BoxType.SIXES));
    }

    @Test
    void testCalculateTrips() {
        int[] diceValues = {6, 6, 6, 1, 1};
        Assertions.assertEquals(28, ScoreCalculator.calculateScore(diceValues, BoxType.TRIPS));
    }

    @Test
    void testCalculateTripsWithFourOfAKind() {
        int[] diceValues = {6, 6, 6, 6, 1};
        Assertions.assertEquals(28, ScoreCalculator.calculateScore(diceValues, BoxType.TRIPS));
    }

    @Test
    void testCalculateTripsWithTwoOfAKind() {
        int[] diceValues = {6, 6, 1, 1, 2};
        Assertions.assertEquals(0, ScoreCalculator.calculateScore(diceValues, BoxType.TRIPS));
    }

    @Test
    void testCalculateStraightWithSmallStraight() {
        int[] diceValues = {1, 2, 3, 4, 5};
        Assertions.assertEquals(35, ScoreCalculator.calculateScore(diceValues, BoxType.STRAIGHT));
    }

    @Test
    void testCalculateStraightWithLargeStraight() {
        int[] diceValues = {2, 3, 4, 5, 6};
        Assertions.assertEquals(45, ScoreCalculator.calculateScore(diceValues, BoxType.STRAIGHT));
    }

    @Test
    void testCalculateStraightWithoutStraight() {
        int[] diceValues = {2, 3, 4, 5, 5};
        Assertions.assertEquals(0, ScoreCalculator.calculateScore(diceValues, BoxType.STRAIGHT));
    }

    @Test
    void testCalculateBoat() {
        int[] diceValues = {6, 6, 6, 1, 1};
        Assertions.assertEquals(50, ScoreCalculator.calculateScore(diceValues, BoxType.BOAT));
    }

    @Test
    void testCalculateBoatAlt() {
        int[] diceValues = {4, 4, 5, 5, 4};
        Assertions.assertEquals(52, ScoreCalculator.calculateScore(diceValues, BoxType.BOAT));
    }

    @Test
    void testCalculateBoatWithFourOfAKind() {
        int[] diceValues = {6, 6, 6, 6, 1};
        Assertions.assertEquals(0, ScoreCalculator.calculateScore(diceValues, BoxType.BOAT));
    }

    @Test
    void testCalculateBoatWithTwoPairs() {
        int[] diceValues = {6, 6, 1, 1, 2};
        Assertions.assertEquals(0, ScoreCalculator.calculateScore(diceValues, BoxType.BOAT));
    }

    @Test
    void testCalculateCarriage() {
        int[] diceValues = {6, 6, 6, 6, 1};
        Assertions.assertEquals(64, ScoreCalculator.calculateScore(diceValues, BoxType.CARRIAGE));
    }

    @Test
    void testCalculateCarriageWithFiveOfAKind() {
        int[] diceValues = {6, 6, 6, 6, 6};
        Assertions.assertEquals(64, ScoreCalculator.calculateScore(diceValues, BoxType.CARRIAGE));
    }

    @Test
    void testCalculateCarriageWithThreeOfAKind() {
        int[] diceValues = {6, 6, 6, 1, 1};
        Assertions.assertEquals(0, ScoreCalculator.calculateScore(diceValues, BoxType.CARRIAGE));
    }

    @Test
    void testCalculateYamb() {
        int[] diceValues = {6, 6, 6, 6, 6};
        Assertions.assertEquals(80, ScoreCalculator.calculateScore(diceValues, BoxType.YAMB));
    }

    @Test
    void testCalculateYambWithFourOfAKind() {
        int[] diceValues = {6, 6, 6, 6, 1};
        Assertions.assertEquals(0, ScoreCalculator.calculateScore(diceValues, BoxType.YAMB));
    }

}