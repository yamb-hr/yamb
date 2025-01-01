package com.tejko.yamb.domain.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.tejko.yamb.domain.constants.GameConstants;
import com.tejko.yamb.domain.enums.BoxType;
import com.tejko.yamb.domain.enums.ColumnType;
import com.tejko.yamb.domain.enums.GameStatus;
import com.tejko.yamb.domain.enums.GameType;
import com.tejko.yamb.domain.exceptions.AnnouncementAlreadyMadeException;
import com.tejko.yamb.domain.exceptions.AnnouncementNotAllowedException;
import com.tejko.yamb.domain.exceptions.AnnouncementRequiredException;
import com.tejko.yamb.domain.exceptions.BoxUnavailableException;
import com.tejko.yamb.domain.exceptions.GameLockedException;
import com.tejko.yamb.domain.exceptions.GameNotCompletedException;
import com.tejko.yamb.domain.exceptions.IllegalGameStateException;
import com.tejko.yamb.domain.exceptions.RollLimitExceededException;
import com.tejko.yamb.domain.exceptions.RollRequiredException;
import com.tejko.yamb.domain.exceptions.UndoClashGameException;
import com.tejko.yamb.util.ScoreCalculator;

@Document(collection = "games")
public class Game {

    @Id
    private String id;

    @Indexed
    @Field(name = "external_id")
    private UUID externalId = UUID.randomUUID();

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;
    
    @Field("player_id")
    private UUID playerId;

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

    @Field("type")
    private GameType type;

    @Field("latest_dice_rolled")
    private int[] latestDiceRolled;

    @Field("previous_roll_count")
    private int previousRollCount;

    @Field("latest_column_filled")
    private ColumnType latestColumnFilled;

    @Field("latest_box_filled")
    private BoxType latestBoxFilled;

    protected Game() {}

    protected Game(UUID playerId, Sheet sheet, List<Dice> dices, int rollCount, BoxType announcement, GameStatus status, GameType type) {
        this.playerId = playerId;
        this.sheet = sheet;
        this.dices = dices;
        this.rollCount = rollCount;
        this.announcement = announcement;
        this.status = status;
        this.type = type;
    }

    public static Game getInstance(UUID playerId, GameType type) {
        return new Game(playerId, Sheet.getInstance(), generateDices(), 0, null, GameStatus.IN_PROGRESS, type);
    }

    public String getId() {
        return id;
    }
    
    public UUID getExternalId() {
        return externalId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public UUID getPlayerId() {
        return playerId;
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

    public GameType getType() {
        return type;
    }

    public int[] getLatestDiceRolled() {
        return latestDiceRolled;
    }

    public int getPreviousRollCount() {
        return previousRollCount;
    }

    public ColumnType getLatestColumnFilled() {
        return latestColumnFilled;
    }

    public BoxType getLatestBoxFilled() {
        return latestBoxFilled;
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

    public float getProgress() {
        int completedBoxes = 0;
        for (Column column : sheet.getColumns()) {
            for (Box box : column.getBoxes()) {
                if (box.getValue() != null) {
                    completedBoxes++;
                }
            }
        }
        return completedBoxes / 52.0f;
    }
    
    public void roll(int[] diceToRoll) {
        validateRoll(diceToRoll);
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
        latestDiceRolled = diceToRoll;
        latestColumnFilled = null;
        latestBoxFilled = null;
    }

    public int[] getDiceValues() {
        return dices.stream().map(Dice::getValue).mapToInt(Number::intValue).toArray();
    }
    
    public void fill(ColumnType columnType, BoxType boxType) {
        validatefill(columnType, boxType);
        sheet.fill(columnType, boxType, ScoreCalculator.calculateScore(getDiceValues(), boxType));
        if (sheet.isCompleted() ) {
            status = GameStatus.COMPLETED;
        }
        previousRollCount = rollCount;
        rollCount = 0;
        announcement = null;
        latestColumnFilled = columnType;
        latestBoxFilled = boxType;
    }

    public void undoFill() {
        validateUndoFill();
        sheet.undoFill(latestColumnFilled, latestBoxFilled);
        if (ColumnType.ANNOUNCEMENT.equals(latestColumnFilled)) {
            announcement = latestBoxFilled;
        }
        latestColumnFilled = null;
        latestBoxFilled = null;
        rollCount = previousRollCount;
        previousRollCount = 0;
    }

    private void validateUndoFill() {
        if (latestColumnFilled == null || latestBoxFilled == null) {
            throw new IllegalGameStateException("Cannot be undone");
        } else if (isLocked()) {
            throw new GameLockedException();
        } else if (GameType.CLASH.equals(type)) {
            throw new UndoClashGameException();
        }
    }
    
    public void announce(BoxType boxType) {
        validateAnnouncement(boxType);
        announcement = boxType;
    }

    public void restart() {
        validateRestart();
        rollCount = 0;
        announcement = null;
        sheet = Sheet.getInstance();
        dices = generateDices();
    }

    public void archive() {
        validateArchive();
        status = GameStatus.ARCHIVED;
    }

    public boolean isLocked() {
        return status == GameStatus.COMPLETED || status == GameStatus.ARCHIVED;
    }

    private void validateRoll(int[] diceToRoll) {
        if (isLocked()) {
            throw new GameLockedException();
        } else if (rollCount == 3) {
            throw new RollLimitExceededException();
        } else if (isAnnouncementRequired()) {
            throw new AnnouncementRequiredException();
        } else if (diceToRoll == null) {
            throw new IllegalArgumentException("Dice to roll cannot be null.");
        } else if (diceToRoll.length == 0) {
            throw new IllegalArgumentException("Dice to roll cannot be empty.");
        } else if (diceToRoll.length > 5) {
            throw new IllegalArgumentException("Dice to roll cannot contain more than 5 elements.");
        } else {
            for (int dice : diceToRoll) {
                if (dice < 0 || dice > 4) {
                    throw new IllegalArgumentException("Invalid dice value: " + dice + ". Dice must be between 0 and 4.");
                }
            }
        }
    }

    private void validatefill(ColumnType columnType, BoxType boxType) {
        if (isLocked()) {
            throw new GameLockedException();
        } else if (rollCount == 0) {
			throw new RollRequiredException();
        } else if (columnType == null) {
            throw new IllegalArgumentException("Column type cannot be null.");
        } else if (boxType == null) {
            throw new IllegalArgumentException("Box type cannot be null.");
        } else if (!isBoxAvailable(columnType, boxType)) {
            throw new BoxUnavailableException();
        }
    }

    private void validateAnnouncement(BoxType boxType) {
        if (isLocked()) {
            throw new GameLockedException();
        } else if (announcement != null) {
            throw new AnnouncementAlreadyMadeException();
        } else if (rollCount == 0) {
            throw new RollRequiredException();
        } else if (rollCount > 1) {
            throw new AnnouncementNotAllowedException();
        } else if (boxType == null) {
            throw new IllegalArgumentException("Box type cannot be null.");
        } else if (this.sheet.getColumns().get(ColumnType.ANNOUNCEMENT.ordinal()).getBoxes().get(boxType.ordinal()).getValue() != null) {
            throw new BoxUnavailableException();
        }
    }

    private void validateRestart() {
        if (isLocked()) {
            throw new GameLockedException();
        } else if (GameType.CLASH.equals(type)) {
            throw new IllegalGameStateException("Clash cannot be restarted");
        }
    }

    private void validateArchive() {
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
        for (int i = 0; i < BoxType.values().length; i++) {    
            roll(diceToRoll);
            announce(BoxType.values()[i]);
            fill(ColumnType.ANNOUNCEMENT, BoxType.values()[i]);
        }
    }

    public static class Dice implements Serializable {
    
        private int index;
        private int value;

        protected Dice() {}

        protected Dice(int index, int value) {
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
    
        protected Sheet() { }
    
        protected Sheet(List<Column> columns) {
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

        public void undoFill(ColumnType columnType, BoxType boxType) {
            columns.get(columnType.ordinal()).undoFill(boxType);
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

        protected Column() {}

        protected Column (ColumnType type, List<Box> boxes) {
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
            return Math.max(middleSectionSum, 0);
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

        public void undoFill(BoxType boxType) {
            Box latestBoxFilled = boxes.get(boxType.ordinal());
            latestBoxFilled.undoFill();
        }

    }

    public static class Box implements Serializable {

        private BoxType type;
        private Integer value;
    
        protected Box() {}
    
        protected Box(BoxType type, Integer value) {
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

        public void undoFill() {
            this.value = null;
        }
    
    }

}
