package com.tejko.yamb.unit.domain.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tejko.yamb.domain.enums.BoxType;
import com.tejko.yamb.domain.enums.ColumnType;
import com.tejko.yamb.domain.enums.GameStatus;
import com.tejko.yamb.domain.models.entities.Game;
import com.tejko.yamb.exceptions.custom.AnnouncementAlreadyMadeException;
import com.tejko.yamb.exceptions.custom.AnnouncementRequiredException;
import com.tejko.yamb.exceptions.custom.AnnouncementNotAllowedException;
import com.tejko.yamb.exceptions.custom.BoxUnavailableException;
import com.tejko.yamb.exceptions.custom.RollRequiredException;
import com.tejko.yamb.exceptions.custom.GameLockedException;
import com.tejko.yamb.exceptions.custom.GameNotCompletedException;
import com.tejko.yamb.exceptions.custom.RollLimitExceededException;

public class GameTest {

    private Game game;
    private static final int[] DICE_TO_ROLL = {0, 1, 2, 3, 4};

    @BeforeEach
    public void setUp() {
        game = Game.getInstance(1L);
    }

    @Test
    public void testRoll_Success() {
        game.roll(DICE_TO_ROLL);
        assertEquals(1, game.getRollCount());
    }

    @Test
    public void testRoll_LimitExceeded() {
        game.roll(DICE_TO_ROLL);
        game.roll(DICE_TO_ROLL);
        game.roll(DICE_TO_ROLL);

        assertThrows(RollLimitExceededException.class, () -> {
            game.roll(DICE_TO_ROLL);
        });
    }

    @Test
    public void testRoll_AnnouncementRequired() {
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
    public void testRoll_LockedGame() {
        game.complete();

        assertThrows(GameLockedException.class, () -> {
            game.roll(DICE_TO_ROLL);
        });
    }

    @Test
    public void testRoll_NullDice() {
        assertThrows(IllegalArgumentException.class, () -> {
            game.roll(null);
        });
    }

    @Test
    public void testRoll_EmptyDice() {
        assertThrows(IllegalArgumentException.class, () -> {
            game.roll(new int[]{});
        });
    }

    @Test
    public void testRoll_InvalidDice() {
        assertThrows(IllegalArgumentException.class, () -> {
            game.roll(new int[]{0, 1, 2, 3, 5}); // 5 is out of range
        });
    }

    @Test
    public void testFill_Success() {
        game.roll(DICE_TO_ROLL);

        game.fill(ColumnType.DOWNWARDS, BoxType.ONES);

        assertNotNull(game.getSheet().getColumns().get(ColumnType.DOWNWARDS.ordinal()).getBoxes().get(BoxType.ONES.ordinal()).getValue());
    }

    @Test
    public void testFill_RollRequired() {
        assertThrows(RollRequiredException.class, () -> {
            game.fill(ColumnType.DOWNWARDS, BoxType.ONES);
        });
    }

    @Test
    public void testFill_BoxUnavailable() {
        game.roll(DICE_TO_ROLL);
        game.fill(ColumnType.DOWNWARDS, BoxType.ONES);
        game.roll(DICE_TO_ROLL);

        assertThrows(BoxUnavailableException.class, () -> {
            game.fill(ColumnType.DOWNWARDS, BoxType.ONES);
        });
    }

    @Test
    public void testFill_BoxNotAnnounced() {
        game.roll(DICE_TO_ROLL);
        game.announce(BoxType.ONES);

        assertThrows(BoxUnavailableException.class, () -> {
            game.fill(ColumnType.ANNOUNCEMENT, BoxType.TWOS);
        });
    }

    @Test
    public void testFill_NullColumnType() {
        game.roll(DICE_TO_ROLL);

        assertThrows(IllegalArgumentException.class, () -> {
            game.fill(null, BoxType.ONES);
        });
    }

    @Test
    public void testFill_NullBoxType() {
        game.roll(DICE_TO_ROLL);

        assertThrows(IllegalArgumentException.class, () -> {
            game.fill(ColumnType.DOWNWARDS, null);
        });
    }

    @Test
    public void testFill_GameLocked() {
        game.complete();

        assertThrows(GameLockedException.class, () -> {
            game.fill(ColumnType.DOWNWARDS, null);
        });
    }

    @Test
    public void testAnnounce_Success() {
        game.roll(DICE_TO_ROLL);

        game.announce(BoxType.ONES);

        assertEquals(BoxType.ONES, game.getAnnouncement());
    }

    @Test
    public void testAnnounce_AnnouncementAlreadyDeclared() {
        game.roll(DICE_TO_ROLL);
        game.announce(BoxType.ONES);

        assertThrows(AnnouncementAlreadyMadeException.class, () -> {
            game.announce(BoxType.TWOS);
        });
    }

    @Test
    public void testAnnounce_AnnouncementUnavailable() {
        game.roll(DICE_TO_ROLL);
        game.roll(DICE_TO_ROLL);

        assertThrows(AnnouncementNotAllowedException.class, () -> {
            game.announce(BoxType.ONES);
        });
    }

    @Test
    public void testAnnounce_BoxUnavailable() {
        game.roll(DICE_TO_ROLL);
        game.announce(BoxType.ONES);
        game.fill(ColumnType.ANNOUNCEMENT, BoxType.ONES);
        game.roll(DICE_TO_ROLL);

        assertThrows(BoxUnavailableException.class, () -> {
            game.announce(BoxType.ONES);
        });
    }

    @Test
    public void testAnnounce_RollRequired() {
        assertThrows(RollRequiredException.class, () -> {
            game.announce(BoxType.ONES);
        });
    }

    @Test
    public void testAnnounce_NullBoxType() {
        game.roll(DICE_TO_ROLL);

        assertThrows(IllegalArgumentException.class, () -> {
            game.announce(null);
        });
    }

    @Test
    public void testAnnounce_GameLocked() {
        game.complete();

        assertThrows(GameLockedException.class, () -> {
            game.announce(BoxType.ONES);
        });
    }

    @Test
    public void testRestart_Success() {
        game.roll(DICE_TO_ROLL);

        game.restart();

        assertEquals(0, game.getRollCount());
    }

    @Test
    public void testRestart_GameLocked() {
        game.complete();

        assertThrows(GameLockedException.class, () -> {
            game.restart();
        });
    }

    @Test
    public void testArchive() {
        game.complete();

        game.archive();
        
        assertEquals(GameStatus.ARCHIVED, game.getStatus());
    }

    @Test
    public void testArchive_GameNotCompleted() {
        assertThrows(GameNotCompletedException.class, () -> {
            game.archive();
        });
    }
}
