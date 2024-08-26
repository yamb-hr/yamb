package com.tejko.yamb.unit.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.tejko.yamb.domain.enums.BoxType;
import com.tejko.yamb.util.ScoreCalculator;

public class ScoreCalculatorTest {

    @Test
    void testCalculateSum_Max() {
        int[] diceValues = {6, 6, 6, 6, 6};
        Assertions.assertEquals(30, ScoreCalculator.calculateScore(diceValues, BoxType.MAX));
    }

    @Test
    void testCalculateSum_Sixes() {
        int[] diceValues = {6, 6, 6, 1, 1};
        Assertions.assertEquals(18, ScoreCalculator.calculateScore(diceValues, BoxType.SIXES));
    }

    @Test
    void testCalculateSum_Sixes_NoSixes() {
        int[] diceValues = {1, 1, 1, 1, 1};
        Assertions.assertEquals(0, ScoreCalculator.calculateScore(diceValues, BoxType.SIXES));
    }

    @Test
    void testCalculateTrips_ValidTrips() {
        int[] diceValues = {6, 6, 6, 1, 1};
        Assertions.assertEquals(28, ScoreCalculator.calculateScore(diceValues, BoxType.TRIPS));
    }

    @Test
    void testCalculateTrips_FourOfAKind() {
        int[] diceValues = {6, 6, 6, 6, 1};
        Assertions.assertEquals(28, ScoreCalculator.calculateScore(diceValues, BoxType.TRIPS));
    }

    @Test
    void testCalculateTrips_TwoOfAKind() {
        int[] diceValues = {6, 6, 1, 1, 2};
        Assertions.assertEquals(0, ScoreCalculator.calculateScore(diceValues, BoxType.TRIPS));
    }

    @Test
    void testCalculateStraight_SmallStraight() {
        int[] diceValues = {1, 2, 3, 4, 5};
        Assertions.assertEquals(35, ScoreCalculator.calculateScore(diceValues, BoxType.STRAIGHT));
    }

    @Test
    void testCalculateStraight_LargeStraight() {
        int[] diceValues = {2, 3, 4, 5, 6};
        Assertions.assertEquals(45, ScoreCalculator.calculateScore(diceValues, BoxType.STRAIGHT));
    }

    @Test
    void testCalculateStraight_NoStraight() {
        int[] diceValues = {2, 3, 4, 5, 5};
        Assertions.assertEquals(0, ScoreCalculator.calculateScore(diceValues, BoxType.STRAIGHT));
    }

    @Test
    void testCalculateBoat_ValidBoat() {
        int[] diceValues = {6, 6, 6, 1, 1};
        Assertions.assertEquals(50, ScoreCalculator.calculateScore(diceValues, BoxType.BOAT));
    }

    @Test
    void testCalculateBoat_AlternateValidBoat() {
        int[] diceValues = {4, 4, 5, 5, 4};
        Assertions.assertEquals(52, ScoreCalculator.calculateScore(diceValues, BoxType.BOAT));
    }

    @Test
    void testCalculateBoat_FourOfAKind() {
        int[] diceValues = {6, 6, 6, 6, 1};
        Assertions.assertEquals(0, ScoreCalculator.calculateScore(diceValues, BoxType.BOAT));
    }

    @Test
    void testCalculateBoat_TwoPairs() {
        int[] diceValues = {6, 6, 1, 1, 2};
        Assertions.assertEquals(0, ScoreCalculator.calculateScore(diceValues, BoxType.BOAT));
    }

    @Test
    void testCalculateCarriage_ValidCarriage() {
        int[] diceValues = {6, 6, 6, 6, 1};
        Assertions.assertEquals(64, ScoreCalculator.calculateScore(diceValues, BoxType.CARRIAGE));
    }

    @Test
    void testCalculateCarriage_FiveOfAKind() {
        int[] diceValues = {6, 6, 6, 6, 6};
        Assertions.assertEquals(64, ScoreCalculator.calculateScore(diceValues, BoxType.CARRIAGE));
    }

    @Test
    void testCalculateCarriage_ThreeOfAKind() {
        int[] diceValues = {6, 6, 6, 1, 1};
        Assertions.assertEquals(0, ScoreCalculator.calculateScore(diceValues, BoxType.CARRIAGE));
    }

    @Test
    void testCalculateYamb_ValidYamb() {
        int[] diceValues = {6, 6, 6, 6, 6};
        Assertions.assertEquals(80, ScoreCalculator.calculateScore(diceValues, BoxType.YAMB));
    }

    @Test
    void testCalculateYamb_FourOfAKind() {
        int[] diceValues = {6, 6, 6, 6, 1};
        Assertions.assertEquals(0, ScoreCalculator.calculateScore(diceValues, BoxType.YAMB));
    }
}
