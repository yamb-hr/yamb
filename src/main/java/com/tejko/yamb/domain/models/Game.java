package com.tejko.yamb.domain.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tejko.yamb.domain.constants.GameConstants;
import com.tejko.yamb.domain.enums.BoxType;
import com.tejko.yamb.domain.enums.ColumnType;
import com.tejko.yamb.domain.enums.GameStatus;
import com.tejko.yamb.domain.exceptions.AnnouncementAlreadyDeclaredException;
import com.tejko.yamb.domain.exceptions.AnnouncementUnavailableException;
import com.tejko.yamb.domain.exceptions.BoxUnavailableException;
import com.tejko.yamb.domain.exceptions.AnnouncementRequiredException;
import com.tejko.yamb.domain.exceptions.DiceRollRequiredException;
import com.tejko.yamb.domain.exceptions.LockedGameException;
import com.tejko.yamb.domain.exceptions.RollLimitExceededException;
import com.tejko.yamb.util.ScoreCalculator;

@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Game extends BaseEntity {

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

    public int getTotalSum() {
        return sheet.getTotalSum();
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
            throw new LockedGameException();
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

    public static class Dice implements Serializable {
    
        private int index;
        private int value;

        private Dice() {}

        private Dice(int index, int value) {
            this.index = index;
            this.value = value;
        }

        public static Dice getInstance(int index) {
            return new Dice(index, 6);
        }

        public int getValue() {
            return value;
        }

        public int getIndex() {
            return index;
        }

        public void roll() {
            this.value = ThreadLocalRandom.current().nextInt(1, 7);
        }

    }

    public static class Sheet implements Serializable {

        private List<GameColumn> columns;
    
        private Sheet() { }
    
        private Sheet(List<GameColumn> columns) {
            this.columns = columns;
        }
    
        public static Sheet getInstance() {
            return new Sheet(generateColumns());
        }
    
        private static List<GameColumn> generateColumns() {
            List<GameColumn> columns = new ArrayList<>();
            for (ColumnType columnType : ColumnType.values()) {
                columns.add(GameColumn.getInstance(columnType));
            }
            return columns;
        }
    
        public List<GameColumn> getColumns() {
            return columns;
        }
    
        @JsonIgnore
        public int getTopSectionSum() {  
            int topSectionSum = 0;
            for (GameColumn column : columns) {
                topSectionSum += column.getTopSectionSum();
            }
            return topSectionSum;
        }
    
        @JsonIgnore
        public int getMiddleSectionSum() {
            int middleSectionSum = 0;
            for (GameColumn column : columns) {
                middleSectionSum += column.getMiddleSectionSum();
            }
            return middleSectionSum;
        }
    
        @JsonIgnore
        public int getBottomSectionSum() {
            int bottomSectionSum = 0;
            for (GameColumn column : columns) {
                bottomSectionSum += column.getBottomSectionSum();
            }
            return bottomSectionSum;
        }
        
        @JsonIgnore
        public int getTotalSum() { 
            return getTopSectionSum() + getMiddleSectionSum() + getBottomSectionSum();
        }
        
        @JsonIgnore
        public boolean isCompleted() {
            for (GameColumn column : columns) {
                if (!column.isCompleted()) {
                    return false;
                }
            }
            return true;
        }
    
        public void fillBox(ColumnType columnType, BoxType boxType, int value) {
            columns.get(columnType.ordinal()).fillBox(boxType, value);
        }
    
        public boolean areAllNonAnnouncementColumnsCompleted() {
            for (GameColumn column : columns) {
                if (column.getType() != ColumnType.ANNOUNCEMENT && !column.isCompleted()) {
                    return false;
                }
            }
            return true;
        }
    }

    public static class GameColumn implements Serializable { 

        private ColumnType type;
        private List<Box> boxes;

        private GameColumn() {}

        private GameColumn (ColumnType type, List<Box> boxes) {
            this.type = type; 
            this.boxes = boxes;
        }
        
        public static GameColumn getInstance(ColumnType type) {
            return new GameColumn(type, generateBoxes(type));
        }

        private static List<Box> generateBoxes(ColumnType columnType) {
            List<Box> boxes = new ArrayList<>();
            for (BoxType boxType : BoxType.values()) {
                boxes.add(Box.getInstance(boxType));
            }
            return boxes;
        }
        
        public ColumnType getType() {
            return type;
        }

        public void setType(ColumnType type) {
            this.type = type;
        }
        
        public List<Box> getBoxes() {
            return boxes;
        }

        @JsonIgnore
        public int getTopSectionSum() {
            int topSectionSum = 0;
            for (BoxType boxType : GameConstants.TOP_SECTION) {
                Box box = boxes.get(boxType.ordinal());
                if (box.getValue() != null) {
                    topSectionSum += box.getValue();
                }
            }
            if (topSectionSum >= GameConstants.TOP_SECTION_SUM_BONUS_THRESHOLD) {
                topSectionSum += GameConstants.TOP_SECTION_SUM_BONUS;
            }
            return topSectionSum;
        }

        @JsonIgnore
        public int getMiddleSectionSum() {
            int middleSectionSum = 0;
            Box ones = boxes.get(BoxType.ONES.ordinal());
            Box max = boxes.get(BoxType.MAX.ordinal());
            Box min = boxes.get(BoxType.MIN.ordinal());
            if (ones.getValue() != null && max.getValue() != null && min.getValue() != null) {
                middleSectionSum = ones.getValue() * (max.getValue() - min.getValue());
            }
            return middleSectionSum;
        }

        @JsonIgnore
        public int getBottomSectionSum() {
            int bottomSectionSum = 0;
            for (BoxType boxType : GameConstants.BOTTOM_SECTION) {
                Box box = boxes.get(boxType.ordinal());
                if (box.getValue() != null) {
                    bottomSectionSum += box.getValue();
                }
            }
            return bottomSectionSum;
        }

        @JsonIgnore
        public boolean isCompleted() {
            return getNumOfEmptyBoxes() == 0;
        }

        private int getNumOfEmptyBoxes() {
            int numOfEmptyBoxes = 0;
            for (Box box : boxes) {
                if (box.getValue() == null) {
                    numOfEmptyBoxes += 1;
                }
            }
            return numOfEmptyBoxes;
        }  

        public void fillBox(BoxType boxType, int value) { 
            Box selectedBox = boxes.get(boxType.ordinal());
            selectedBox.fill(value);
        }

    }

    public static class Box implements Serializable {

        private BoxType type;
        private Integer value;
    
        private Box() {}
    
        private Box(BoxType type, Integer value) {
            this.type = type;
            this.value = value;
        }  
    
        public static Box getInstance(BoxType type) {
            return new Box(type, null);
        }    
    
        public BoxType getType() {
            return type;
        }
    
        public Integer getValue() {
            return value;
        }
    
        public void fill(int value) {
            this.value = value;
        }
    
    }

}
