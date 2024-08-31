package com.tejko.yamb.domain.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.tejko.yamb.domain.constants.GameConstants;
import com.tejko.yamb.domain.enums.BoxType;
import com.tejko.yamb.domain.enums.ColumnType;
import com.tejko.yamb.domain.enums.GameStatus;
import com.tejko.yamb.exceptions.custom.GameNotCompletedException;
import com.tejko.yamb.exceptions.custom.AnnouncementAlreadyMadeException;
import com.tejko.yamb.exceptions.custom.AnnouncementRequiredException;
import com.tejko.yamb.exceptions.custom.AnnouncementNotAllowedException;
import com.tejko.yamb.exceptions.custom.BoxUnavailableException;
import com.tejko.yamb.exceptions.custom.RollRequiredException;
import com.tejko.yamb.exceptions.custom.GameLockedException;
import com.tejko.yamb.exceptions.custom.RollLimitExceededException;
import com.tejko.yamb.util.ScoreCalculator;

@Document(collection = "games")
public class Game {

    @Id
    private String id;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;
    
    @Field("player_id")
    private Long playerId;
    
    @Field("player_name")
    private String playerName;

    @Field("sheet")
    private Sheet sheet;

    @Field("dices")
    private List<Dice> dices;

    @Field("roll_count")
    private int rollCount;

    @Field("announcement")
    private BoxType announcement;

    @Field("status")
    private GameStatus status;

    protected Game() {}

    protected Game(Long playerId, String playerName, Sheet sheet, List<Dice> dices, int rollCount, BoxType announcement, GameStatus status) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.sheet = sheet;
        this.dices = dices;
        this.rollCount = rollCount;
        this.announcement = announcement;
        this.status = status;
    }

    public static Game getInstance(Long playerId, String playerName) {
        return new Game(playerId, playerName, Sheet.getInstance(), generateDices(), 0, null, GameStatus.IN_PROGRESS);
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
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

    public boolean isAnnouncementRequired() {
        return rollCount == 1 && announcement == null && sheet.areAllNonAnnouncementColumnsCompleted();
    }
    
    public void roll(int[] diceToRoll) {
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

    public int[] getDiceValues() {
        return dices.stream().map(Dice::getValue).mapToInt(Number::intValue).toArray();
    }
    
    public void fill(ColumnType columnType, BoxType boxType) {
        validatefillAction(columnType, boxType);
        sheet.fill(columnType, boxType, ScoreCalculator.calculateScore(getDiceValues(), boxType));
        if (sheet.isCompleted() ) {
            status = GameStatus.COMPLETED;
        }
        rollCount = 0;
        announcement = null;
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

    public void finish() {
        validateFinishAction();
        status = GameStatus.FINISHED;
    }


    private void validateRollAction() {
        if (rollCount == 3) {
            throw new RollLimitExceededException();
        } else if (isAnnouncementRequired()) {
            throw new AnnouncementRequiredException();
        } else if (status != GameStatus.IN_PROGRESS) {
            throw new GameLockedException();
        }
    }

    private void validatefillAction(ColumnType columnType, BoxType boxType) {
        if (rollCount == 0) {
			throw new RollRequiredException();
        } else if (!isBoxAvailable(columnType, boxType)) {
            throw new BoxUnavailableException();
        } else if (status != GameStatus.IN_PROGRESS) {
            throw new GameLockedException();
        }
    }

    private void validateAnnouncementAction(BoxType boxType) {
        if (announcement != null) {
            throw new AnnouncementAlreadyMadeException();
        } else if (rollCount == 0) {
            throw new RollRequiredException();
        } else if (rollCount > 1) {
            throw new AnnouncementNotAllowedException();
        } else if (status != GameStatus.IN_PROGRESS) {
            throw new GameLockedException();
        }
    }

    private void validateRestartAction() {
        if (status != GameStatus.IN_PROGRESS) {
            throw new GameLockedException();
        }
    }

    private void validateFinishAction() {
        if (status != GameStatus.COMPLETED) {
            throw new GameNotCompletedException();
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

    public void complete() {
        int[] diceToRoll = {0, 1, 2, 3, 4};
        for (int i = 0; i < BoxType.values().length; i++) {    
            roll(diceToRoll);
            fill(ColumnType.DOWNWARDS, BoxType.values()[i]);
        }
        for (int i = BoxType.values().length - 1; i >= 0; i--) {    
            roll(diceToRoll);
            fill(ColumnType.UPWARDS, BoxType.values()[i]);
        }
        for (int i = 0; i < BoxType.values().length; i++) {    
            roll(diceToRoll);
            fill(ColumnType.FREE, BoxType.values()[i]);
        }
        for (int i = 0; i < BoxType.values().length-1; i++) {    
            roll(diceToRoll);
            announce(BoxType.values()[i]);
            fill(ColumnType.ANNOUNCEMENT, BoxType.values()[i]);
        }
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

        private List<Column> columns;
    
        private Sheet() { }
    
        private Sheet(List<Column> columns) {
            this.columns = columns;
        }
    
        public static Sheet getInstance() {
            return new Sheet(generateColumns());
        }
    
        private static List<Column> generateColumns() {
            List<Column> columns = new ArrayList<>();
            for (ColumnType columnType : ColumnType.values()) {
                columns.add(Column.getInstance(columnType));
            }
            return columns;
        }
    
        public List<Column> getColumns() {
            return columns;
        }
    
        public int getTopSectionSum() {  
            int topSectionSum = 0;
            for (Column column : columns) {
                topSectionSum += column.getTopSectionSum();
            }
            return topSectionSum;
        }
    
        public int getMiddleSectionSum() {
            int middleSectionSum = 0;
            for (Column column : columns) {
                middleSectionSum += column.getMiddleSectionSum();
            }
            return middleSectionSum;
        }
    
        public int getBottomSectionSum() {
            int bottomSectionSum = 0;
            for (Column column : columns) {
                bottomSectionSum += column.getBottomSectionSum();
            }
            return bottomSectionSum;
        }
        
        public int getTotalSum() { 
            return getTopSectionSum() + getMiddleSectionSum() + getBottomSectionSum();
        }
        
        public boolean isCompleted() {
            for (Column column : columns) {
                if (!column.isCompleted()) {
                    return false;
                }
            }
            return true;
        }
    
        public void fill(ColumnType columnType, BoxType boxType, int value) {
            columns.get(columnType.ordinal()).fill(boxType, value);
        }
    
        public boolean areAllNonAnnouncementColumnsCompleted() {
            for (Column column : columns) {
                if (column.getType() != ColumnType.ANNOUNCEMENT && !column.isCompleted()) {
                    return false;
                }
            }
            return true;
        }
    }

    public static class Column implements Serializable { 

        private ColumnType type;
        private List<Box> boxes;

        private Column() {}

        private Column (ColumnType type, List<Box> boxes) {
            this.type = type; 
            this.boxes = boxes;
        }
        
        public static Column getInstance(ColumnType type) {
            return new Column(type, generateBoxes(type));
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

        public void fill(BoxType boxType, int value) { 
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
