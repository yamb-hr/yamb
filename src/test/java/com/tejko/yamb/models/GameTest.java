package com.tejko.yamb.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tejko.yamb.exceptions.AnnouncementAlreadyDeclaredException;
import com.tejko.yamb.exceptions.AnnouncementUnavailableException;
import com.tejko.yamb.exceptions.AnnouncementRequiredException;
import com.tejko.yamb.exceptions.BoxUnavailableException;
import com.tejko.yamb.exceptions.DiceRollRequiredException;
import com.tejko.yamb.exceptions.RestartFinishedGameException;
import com.tejko.yamb.exceptions.RollLimitExceededException;
import com.tejko.yamb.models.enums.BoxType;
import com.tejko.yamb.models.enums.ColumnType;

public class GameTest {

    private Player player;

    private static final int[] DICE_TO_ROLL = {0, 1, 2, 3, 4};

    @BeforeEach
    public void setup() {
        player = Player.getInstance("TEST", "TEST", true);
    }

    @Test
    public void testRollDice() {
        Game game = Game.getInstance(player);

        game.rollDice(DICE_TO_ROLL);

        assertEquals(1, game.getRollCount());
    }

    @Test
    public void testRollLimitExceeded() {
        Game game = Game.getInstance(player);
        game.rollDice(DICE_TO_ROLL);
        game.rollDice(DICE_TO_ROLL);
        game.rollDice(DICE_TO_ROLL);

        assertThrows(RollLimitExceededException.class, () -> {    
            game.rollDice(DICE_TO_ROLL);
        });
    }

    @Test
    public void testAnnouncementRequired() {
        Game game = Game.getInstance(player);
        for (Integer i = 0; i < BoxType.values().length; i++) {    
            game.rollDice(DICE_TO_ROLL);
            game.fillBox(ColumnType.DOWNWARDS, BoxType.values()[i]);
        }
        for (Integer i = BoxType.values().length - 1; i >= 0; i--) {    
            game.rollDice(DICE_TO_ROLL);
            game.fillBox(ColumnType.UPWARDS, BoxType.values()[i]);
        }
        for (Integer i = 0; i < BoxType.values().length; i++) {    
            game.rollDice(DICE_TO_ROLL);
            game.fillBox(ColumnType.FREE, BoxType.values()[i]);
        } 
        game.rollDice(DICE_TO_ROLL);   

        assertThrows(AnnouncementRequiredException.class, () -> {    
            game.rollDice(DICE_TO_ROLL);
        });
    }
    
    @Test
    public void testFillBox() {
        Game game = Game.getInstance(player);
        game.rollDice(DICE_TO_ROLL);
        
        game.fillBox(ColumnType.DOWNWARDS, BoxType.ONES);
        
        assertNotNull(game.getSheet().getColumns().get(ColumnType.DOWNWARDS.ordinal()).getBoxes().get(BoxType.ONES.ordinal()).getValue());
    }

    @Test
    public void testFillDiceRollRequired() {
        Game game = Game.getInstance(player);
        assertThrows(DiceRollRequiredException.class, () -> {    
            game.fillBox(ColumnType.DOWNWARDS, BoxType.ONES);
        });
    }

    @Test
    public void testBoxNotAvailable() {
        Game game = Game.getInstance(player);
        game.rollDice(DICE_TO_ROLL);                        
        game.fillBox(ColumnType.DOWNWARDS, BoxType.ONES);
        game.rollDice(DICE_TO_ROLL);       
        
        assertThrows(BoxUnavailableException.class, () -> {    
            game.fillBox(ColumnType.DOWNWARDS, BoxType.ONES);
        });
    }

    @Test
    public void testBoxNotAnnounced() {
        Game game = Game.getInstance(player);
        game.rollDice(DICE_TO_ROLL);    
        game.makeAnnouncement(BoxType.ONES);    
        
        assertThrows(BoxUnavailableException.class, () -> {    
            game.fillBox(ColumnType.ANNOUNCEMENT, BoxType.TWOS);
        });
    }
    
    @Test
    public void testMakeAnnouncement() {
        Game game = Game.getInstance(player);
        game.rollDice(DICE_TO_ROLL);
        
        game.makeAnnouncement(BoxType.ONES);
        
        assertEquals(BoxType.ONES, game.getAnnouncement());
    }

    @Test
    public void testAnnouncementAlreadyDeclared() {
        Game game = Game.getInstance(player);
        game.rollDice(DICE_TO_ROLL);
        
        game.makeAnnouncement(BoxType.ONES);

        assertThrows(AnnouncementAlreadyDeclaredException.class, () -> {    
            game.makeAnnouncement(BoxType.TWOS);
        });
    }

    @Test
    public void testAnnouncementNotAvailable() {
        Game game = Game.getInstance(player);
        game.rollDice(DICE_TO_ROLL);
        game.rollDice(DICE_TO_ROLL);

        assertThrows(AnnouncementUnavailableException.class, () -> {    
            game.makeAnnouncement(BoxType.ONES);
        });
    }

    @Test
    public void testAnnounceDiceRollRequired() {
        Game game = Game.getInstance(player);
        assertThrows(DiceRollRequiredException.class, () -> {    
            game.makeAnnouncement(BoxType.ONES);
        });
    }
    
    @Test
    public void testRestartGame() {
        Game game = Game.getInstance(player);
        game.rollDice(DICE_TO_ROLL);        
        
        game.restart();
        
        assertEquals(0, game.getRollCount());
    }   
    
    @Test
    public void testRestartCompletedGame() {
        Game game = Game.getInstance(player);
        for (Integer i = 0; i < BoxType.values().length; i++) {    
            game.rollDice(DICE_TO_ROLL);
            game.fillBox(ColumnType.DOWNWARDS, BoxType.values()[i]);
        }
        for (Integer i = BoxType.values().length - 1; i >= 0; i--) {    
            game.rollDice(DICE_TO_ROLL);
            game.fillBox(ColumnType.UPWARDS, BoxType.values()[i]);
        }
        for (Integer i = 0; i < BoxType.values().length; i++) {    
            game.rollDice(DICE_TO_ROLL);
            game.fillBox(ColumnType.FREE, BoxType.values()[i]);
        }
        for (Integer i = 0; i < BoxType.values().length; i++) {    
            game.rollDice(DICE_TO_ROLL);
            game.makeAnnouncement(BoxType.values()[i]);
            game.fillBox(ColumnType.ANNOUNCEMENT, BoxType.values()[i]);
        }

        assertThrows(RestartFinishedGameException.class, () -> {    
            game.restart();;
        });
    
    }
    
}
