package com.tejko.models;

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

import com.tejko.constants.GameConstants;
import com.tejko.models.enums.BoxType;
import com.tejko.models.enums.ColumnType;
import com.tejko.util.ScoreCalculator;

@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = true)
    private Player player;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Sheet sheet;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<Dice> dices;

    @Column
    private int rollCount = 0;
    
    @Column
    private BoxType announcement;

    private Game() {}

    private Game(Player player, Sheet sheet, List<Dice> dices, int rollCount, BoxType announcement) {
        this.player = player;
        this.sheet = sheet;
        this.dices = dices;
        this.rollCount = rollCount;
        this.announcement = announcement;
    }

    public static Game getInstance(Player player) {
        return new Game(player, Sheet.getInstance(), generateDices(), 0, null);
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

    private static List<Dice> generateDices() {
        List<Dice> dices = new ArrayList<>();
        for (int i = 0; i < GameConstants.DICE_LIMIT; i++) {
            dices.add(Dice.getInstance(i));
        }
        return dices;
    }

    public boolean isAnnouncementRequired() {
        return announcement != null && sheet.areAllNonAnnouncementColumnsCompleted();
    }
    
    public void rollDice(List<Integer> diceToRoll) {
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
    
    public void fillBox(ColumnType columnType, BoxType boxType) {
        validateFillBoxAction(columnType, boxType);
        sheet.fillBox(columnType, boxType, ScoreCalculator.calculateScore(boxType, dices));
    }
    
    public void announce(BoxType boxType) {
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
            throw new IllegalStateException(GameConstants.ERROR_ROLL_LIMIT_REACHED);
        } else if (rollCount == 1 && isAnnouncementRequired()) {
            throw new IllegalStateException(GameConstants.ERROR_ANNOUNCEMENT_REQUIRED);
        }
    }

    private void validateFillBoxAction(ColumnType columnType, BoxType boxType) {
        if (rollCount == 0) {
			throw new IllegalStateException(GameConstants.ERROR_DICE_ROLL_REQUIRED);
        } else if (announcement != null && (columnType != ColumnType.ANNOUNCEMENT || boxType != announcement)) {
			throw new IllegalStateException(GameConstants.ERROR_BOX_NOT_ANNOUNCED);
        }
    }

    private void validateAnnouncementAction(BoxType boxType) {
        if (announcement != null) {
            throw new IllegalStateException(GameConstants.ERROR_ANNOUNCEMENT_ALREADY_DECLARED);
        } else if (rollCount > 1) {
            throw new IllegalStateException(GameConstants.ERROR_ANNOUNCEMENT_NOT_AVAILABLE);
        }
    }

    private void validateRestartAction() {
        if (sheet.isCompleted()) {
            throw new IllegalStateException(GameConstants.ERROR_RESTART_COMPLETED_SHEET);
        }
    }    

}
