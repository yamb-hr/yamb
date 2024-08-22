package com.tejko.yamb.unit.domain.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tejko.yamb.domain.enums.BoxType;
import com.tejko.yamb.domain.enums.ColumnType;
import com.tejko.yamb.domain.exceptions.AnnouncementAlreadyDeclaredException;
import com.tejko.yamb.domain.exceptions.AnnouncementUnavailableException;
import com.tejko.yamb.domain.exceptions.AnnouncementRequiredException;
import com.tejko.yamb.domain.exceptions.BoxUnavailableException;
import com.tejko.yamb.domain.exceptions.DiceRollRequiredException;
import com.tejko.yamb.domain.exceptions.LockedGameException;
import com.tejko.yamb.domain.exceptions.RollLimitExceededException;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.domain.models.Player;

public class GameTest {

    private Player player;

    private static final int[] DICE_TO_ROLL = {0, 1, 2, 3, 4};

    @BeforeEach
    public void setup() {
        player = Player.getInstance("username", "password", true);
    }

    @Test
    public void testRoll_Success() {
        Game game = Game.getInstance(player);

        game.roll(DICE_TO_ROLL);

        assertEquals(1, game.getRollCount());
    }

    @Test
    public void testRoll_LimitExceeded() {
        Game game = Game.getInstance(player);
        game.roll(DICE_TO_ROLL);
        game.roll(DICE_TO_ROLL);
        game.roll(DICE_TO_ROLL);

        assertThrows(RollLimitExceededException.class, () -> {    
            game.roll(DICE_TO_ROLL);
        });
    }

    @Test
    public void testRoll_FinishedGame() {
        Game game = Game.getInstance(player);
        finishGame(game);

        assertThrows(LockedGameException.class, () -> {    
            game.roll(DICE_TO_ROLL);
        });
    }

    @Test
    public void testRoll_AnnouncementRequired() {
        Game game = Game.getInstance(player);
        for (Integer i = 0; i < BoxType.values().length; i++) {    
            game.roll(DICE_TO_ROLL);
            game.fill(ColumnType.DOWNWARDS, BoxType.values()[i]);
        }
        for (Integer i = BoxType.values().length - 1; i >= 0; i--) {    
            game.roll(DICE_TO_ROLL);
            game.fill(ColumnType.UPWARDS, BoxType.values()[i]);
        }
        for (Integer i = 0; i < BoxType.values().length; i++) {    
            game.roll(DICE_TO_ROLL);
            game.fill(ColumnType.FREE, BoxType.values()[i]);
        } 
        game.roll(DICE_TO_ROLL);   

        assertThrows(AnnouncementRequiredException.class, () -> {    
            game.roll(DICE_TO_ROLL);
        });
    }
    
    @Test
    public void testFill_Success() {
        Game game = Game.getInstance(player);
        game.roll(DICE_TO_ROLL);
        
        game.fill(ColumnType.DOWNWARDS, BoxType.ONES);
        
        assertNotNull(game.getSheet().getColumns().get(ColumnType.DOWNWARDS.ordinal()).getBoxes().get(BoxType.ONES.ordinal()).getValue());
    }

    @Test
    public void testFill_DiceRollRequired() {
        Game game = Game.getInstance(player);
        assertThrows(DiceRollRequiredException.class, () -> {    
            game.fill(ColumnType.DOWNWARDS, BoxType.ONES);
        });
    }

    @Test
    public void testFill_BoxUnavailable() {
        Game game = Game.getInstance(player);
        game.roll(DICE_TO_ROLL);                        
        game.fill(ColumnType.DOWNWARDS, BoxType.ONES);
        game.roll(DICE_TO_ROLL);       
        
        assertThrows(BoxUnavailableException.class, () -> {    
            game.fill(ColumnType.DOWNWARDS, BoxType.ONES);
        });
    }

    @Test
    public void testFill_BoxNotAnnounced() {
        Game game = Game.getInstance(player);
        game.roll(DICE_TO_ROLL);    
        game.announce(BoxType.ONES);    
        
        assertThrows(BoxUnavailableException.class, () -> {    
            game.fill(ColumnType.ANNOUNCEMENT, BoxType.TWOS);
        });
    }
    
    @Test
    public void testAnnounce_Success() {
        Game game = Game.getInstance(player);
        game.roll(DICE_TO_ROLL);
        
        game.announce(BoxType.ONES);
        
        assertEquals(BoxType.ONES, game.getAnnouncement());
    }

    @Test
    public void testAnnounce_AnnouncementAlreadyDeclared() {
        Game game = Game.getInstance(player);
        game.roll(DICE_TO_ROLL);
        
        game.announce(BoxType.ONES);

        assertThrows(AnnouncementAlreadyDeclaredException.class, () -> {    
            game.announce(BoxType.TWOS);
        });
    }

    @Test
    public void testAnnounce_AnnouncementUnavailable() {
        Game game = Game.getInstance(player);
        game.roll(DICE_TO_ROLL);
        game.roll(DICE_TO_ROLL);

        assertThrows(AnnouncementUnavailableException.class, () -> {    
            game.announce(BoxType.ONES);
        });
    }

    @Test
    public void testAnnounce_DiceRollRequired() {
        Game game = Game.getInstance(player);
        assertThrows(DiceRollRequiredException.class, () -> {    
            game.announce(BoxType.ONES);
        });
    }
    
    @Test
    public void testRestart_Success() {
        Game game = Game.getInstance(player);
        game.roll(DICE_TO_ROLL);        
        
        game.restart();
        
        assertEquals(0, game.getRollCount());
    }   
    
    @Test
    public void testRestart_FinishedGame() {
        Game game = Game.getInstance(player);
        finishGame(game);

        assertThrows(LockedGameException.class, () -> {    
            game.restart();;
        });
    }

    private void finishGame(Game game) {
        for (Integer i = 0; i < BoxType.values().length; i++) {    
            game.roll(DICE_TO_ROLL);
            game.fill(ColumnType.DOWNWARDS, BoxType.values()[i]);
        }
        for (Integer i = BoxType.values().length - 1; i >= 0; i--) {    
            game.roll(DICE_TO_ROLL);
            game.fill(ColumnType.UPWARDS, BoxType.values()[i]);
        }
        for (Integer i = 0; i < BoxType.values().length; i++) {    
            game.roll(DICE_TO_ROLL);
            game.fill(ColumnType.FREE, BoxType.values()[i]);
        }
        for (Integer i = 0; i < BoxType.values().length; i++) {    
            game.roll(DICE_TO_ROLL);
            game.announce(BoxType.values()[i]);
            game.fill(ColumnType.ANNOUNCEMENT, BoxType.values()[i]);
        }
    }
    
}
