package com.tejko.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tejko.constants.YambConstants;
import com.tejko.exceptions.IllegalMoveException;
import com.tejko.models.enums.BoxType;
import com.tejko.models.enums.ColumnType;
import com.tejko.utils.ScoreCalculator;

@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@JsonIgnoreProperties("hibernateLazyInitializer")
public class Game {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column
    UUID id;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = true)
    private Player player;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Sheet sheet;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<Integer, Dice> diceMap;

    @Column
    private int rollCount = 0;
    
    @Column
    private BoxType announcement = null;

    public Game() {}

    public Game(Player player) {
        this.player = player;
        this.sheet = new Sheet();
        this.diceMap = generateDiceMap();
    }

    public UUID getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public Sheet getSheet() {
        return sheet;
    }
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Map<Integer, Dice> getDiceMap() {
        return diceMap;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public List<Dice> getDiceList() {
        return diceMap.values().stream().collect(Collectors.toList());
    }

    public int getRollCount() {
        return rollCount;
    }

    public BoxType getAnnouncement() {
        return announcement;
    }

    private static Map<Integer, Dice> generateDiceMap() {
        Map<Integer, Dice> diceMap = new HashMap<>();
        for (int i = 0; i < YambConstants.DICE_LIMIT; i++) {
            diceMap.put(i, new Dice(i));
        }
        return diceMap;
    }

    public boolean isAnnouncementRequired() {
        return announcement != null && sheet.areAllNonAnnouncementColumnsCompleted();
    }
    
    public void rollDice(List<Integer> diceToRoll) {
        validateRollAction();
        // always roll all dice for the first roll
        if (rollCount == 0) {
            for (Dice dice : diceMap.values()) {
                dice.roll();
            }
        } else {
            for (int diceOrder : diceToRoll) {
                Dice dice = diceMap.get(diceOrder);
                dice.roll();
            }
        }
        rollCount += 1;
    }   
    
    public void fillBox(ColumnType columnType, BoxType boxType) {
        validateFillBoxAction(columnType, boxType);
        sheet.fillBox(columnType, boxType, ScoreCalculator.calculateScore(diceMap.values(), boxType));
    }
    
    public void announce(BoxType boxType) {
        validateAnnouncementAction(boxType);
        announcement = boxType;
    }

    public void restart() {
        validateRestartAction();
        rollCount = 0;
        announcement = null;
        sheet = new Sheet();
        diceMap = generateDiceMap();
    }

    private void validateRollAction() {
        if (rollCount == 3) {
            throw new IllegalMoveException(YambConstants.ERROR_MESSAGE_ROLL_LIMIT_REACHED);
        } else if (rollCount == 1 && isAnnouncementRequired()) {
            throw new IllegalMoveException(YambConstants.ERROR_MESSAGE_ANNOUNCEMENT_REQUIRED);
        }
    }

    private void validateFillBoxAction(ColumnType columnType, BoxType boxType) {
        if (rollCount == 0) {
			throw new IllegalMoveException(YambConstants.ERROR_MESSAGE_DICE_ROLL_REQUIRED);
        } else if (announcement != null && (columnType != ColumnType.ANNOUNCEMENT || boxType != announcement)) {
			throw new IllegalMoveException(YambConstants.ERROR_MESSAGE_BOX_NOT_ANNOUNCED);
        }
    }

    private void validateAnnouncementAction(BoxType boxType) {
        if (announcement != null) {
            throw new IllegalMoveException(YambConstants.ERROR_MESSAGE_ANNOUNCEMENT_ALREADY_DECLARED);
        } else if (rollCount > 1) {
            throw new IllegalMoveException(YambConstants.ERROR_MESSAGE_ANNOUNCEMENT_NOT_AVAILABLE);
        }
    }

    private void validateRestartAction() {
        if (sheet.isCompleted()) {
            throw new IllegalMoveException(YambConstants.ERROR_MESSAGE_RESTART_COMPLETED_SHEET);
        }
    }    

}
