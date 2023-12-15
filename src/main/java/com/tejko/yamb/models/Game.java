package com.tejko.yamb.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tejko.yamb.constants.GameConstants;
import com.tejko.yamb.exceptions.AnnouncementAlreadyDeclaredException;
import com.tejko.yamb.exceptions.AnnouncementUnavailableException;
import com.tejko.yamb.exceptions.BoxUnavailableException;
import com.tejko.yamb.exceptions.AnnouncementRequiredException;
import com.tejko.yamb.exceptions.DiceRollRequiredException;
import com.tejko.yamb.exceptions.RestartFinishedGameException;
import com.tejko.yamb.exceptions.RollLimitExceededException;
import com.tejko.yamb.models.enums.BoxType;
import com.tejko.yamb.models.enums.ColumnType;
import com.tejko.yamb.models.enums.GameStatus;
import com.tejko.yamb.util.ScoreCalculator;

@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Sheet sheet;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<Dice> dices;

    @Column
    private int rollCount;
    
    @Column
    private BoxType announcement;

    @Column
    private GameStatus status;

    private Game() {}

    private Game(Player player, Sheet sheet, List<Dice> dices, int rollCount, BoxType announcement, GameStatus status) {
        this.player = player;
        this.sheet = sheet;
        this.dices = dices;
        this.rollCount = rollCount;
        this.announcement = announcement;
        this.status = status;
    }

    public static Game getInstance(Player player) {
        return new Game(player, Sheet.getInstance(), generateDices(), 0, null, GameStatus.IN_PROGRESS);
    }

    public Long getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public Sheet getSheet() {
        return sheet;
    }
    
    public List<Dice> getDices() {
        return dices;
    }

    public int getRollCount() {
        return rollCount;
    }

    public BoxType getAnnouncement() {
        return announcement;
    }

    public GameStatus getStatus() {
        return status;
    }

    private static List<Dice> generateDices() {
        List<Dice> dices = new ArrayList<>();
        for (int i = 0; i < GameConstants.DICE_LIMIT; i++) {
            dices.add(Dice.getInstance(i));
        }
        return dices;
    }

    @JsonIgnore
    public boolean isAnnouncementRequired() {
        return rollCount == 1 && announcement == null && sheet.areAllNonAnnouncementColumnsCompleted();
    }
    
    public void rollDice(int[] diceToRoll) {
        validateRollAction();
        // always roll all dice for the first roll
        if (rollCount == 0) {
            for (Dice dice : dices) {
                dice.roll();
            }
        } else {
            for (int index : diceToRoll) {
                Dice dice = dices.get(index);
                dice.roll();
            }
        }
        rollCount += 1;
    }

    @JsonIgnore
    public int[] getDiceValues() {
        return dices.stream().map(Dice::getValue).mapToInt(Number::intValue).toArray();
    }
    
    public void fillBox(ColumnType columnType, BoxType boxType) {
        validateFillBoxAction(columnType, boxType);
        sheet.fillBox(columnType, boxType, ScoreCalculator.calculateScore(getDiceValues(), boxType));
        if (sheet.isCompleted() ) {
            status = GameStatus.FINISHED;
        }
        rollCount = 0;
        announcement = null;
    }
    
    public void makeAnnouncement(BoxType boxType) {
        validateAnnouncementAction(boxType);
        announcement = boxType;
    }

    public void restart() {
        validateRestartAction();
        rollCount = 0;
        announcement = null;
        sheet = Sheet.getInstance();
        dices = generateDices();
    }

    private void validateRollAction() {
        if (rollCount == 3) {
            throw new RollLimitExceededException();
        } else if (isAnnouncementRequired()) {
            throw new AnnouncementRequiredException();
        }
    }

    private void validateFillBoxAction(ColumnType columnType, BoxType boxType) {
        if (rollCount == 0) {
			throw new DiceRollRequiredException();
        } else if (!isBoxAvailable(columnType, boxType)) {
            throw new BoxUnavailableException();
        }
    }

    private void validateAnnouncementAction(BoxType boxType) {
        if (announcement != null) {
            throw new AnnouncementAlreadyDeclaredException();
        } else if (rollCount == 0) {
            throw new DiceRollRequiredException();
        } else if (rollCount > 1) {
            throw new AnnouncementUnavailableException();
        }
    }

    private void validateRestartAction() {
        if (status == GameStatus.FINISHED) {
            throw new RestartFinishedGameException();
        }
    }

    
    private boolean isBoxAvailable(ColumnType columnType, BoxType boxType) {
        if (sheet.getColumns().get(columnType.ordinal()).getBoxes().get(boxType.ordinal()).getValue() != null) {
            return false;
        }   
        if (announcement != null) {
            return columnType == ColumnType.ANNOUNCEMENT && boxType == announcement;
        } else if (columnType == ColumnType.FREE) {
            return true;
        } else if (columnType == ColumnType.DOWNWARDS) {
            return boxType == BoxType.ONES || sheet.getColumns().get(columnType.ordinal()).getBoxes().get(boxType.ordinal() - 1).getValue() != null;
        } else if (columnType == ColumnType.UPWARDS) {
            return boxType == BoxType.YAMB || sheet.getColumns().get(columnType.ordinal()).getBoxes().get(boxType.ordinal() + 1).getValue() != null;
        } else if (columnType == ColumnType.ANNOUNCEMENT) {
            return boxType == announcement;
        }
        return false;
    }


}
