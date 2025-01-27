package com.tejko.yamb.api.dto.responses;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.tejko.yamb.domain.enums.BoxType;
import com.tejko.yamb.domain.enums.ColumnType;
import com.tejko.yamb.domain.enums.GameAction;
import com.tejko.yamb.domain.enums.GameStatus;
import com.tejko.yamb.domain.enums.GameType;

@Relation(collectionRelation = "games")
public class GameDetailResponse extends RepresentationModel<GameDetailResponse> {

    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Sheet sheet;
    private List<Dice> dices;
    private int rollCount;
    private BoxType announcement;
    private GameStatus status;
    private PlayerResponse player;
    private int totalSum;
    private int previousRollCount;
    private int[] latestDiceRolled;
    private ColumnType latestColumnFilled;
    private BoxType latestBoxFilled;
    private GameType type;
    private float progress;
    private GameAction lastAction;

    public GameDetailResponse() {}
    
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public List<Dice> getDices() {
        return dices;
    }

    public void setDices(List<Dice> dices) {
        this.dices = dices;
    }

    public int getRollCount() {
        return rollCount;
    }

    public void setRollCount(int rollCount) {
        this.rollCount = rollCount;
    }

    public BoxType getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(BoxType announcement) {
        this.announcement = announcement;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public int getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(int totalSum) {
        this.totalSum = totalSum;
    }

    public PlayerResponse getPlayer() {
        return player;
    }

    public void setPlayer(PlayerResponse player) {
        this.player = player;
    }

    public int getPreviousRollCount() {
        return previousRollCount;
    }

    public void setPreviousRollCount(int previousRollCount) {
        this.previousRollCount = previousRollCount;
    }

    public int[] getLatestDiceRolled() {
        return latestDiceRolled;
    }

    public void setLatestDiceRolled(int[] latestDiceRolled) {
        this.latestDiceRolled = latestDiceRolled;
    }

    public ColumnType getLatestColumnFilled() {
        return latestColumnFilled;
    }

    public void setLatestColumnFilled(ColumnType latestColumnFilled) {
        this.latestColumnFilled = latestColumnFilled;
    }

    public BoxType getLatestBoxFilled() {
        return latestBoxFilled;
    }

    public void setLatestBoxFilled(BoxType latestBoxFilled) {
        this.latestBoxFilled = latestBoxFilled;
    }

    public GameType getType() {
        return type;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public GameAction getLastAction() {
        return lastAction;
    }

    public void setLastAction(GameAction lastAction) {
        this.lastAction = lastAction;
    }
    
    public static class Dice {

        private int index;
        private int value;

        public Dice() {}

        public Dice(int index, int value) {
            this.index = index;
            this.value = value;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    
    }

    public static class Sheet {

        private List<Column> columns;

        public Sheet() {}

        public Sheet(List<Column> columns) {
            this.columns = columns;
        }

        public List<Column> getColumns() {
            return columns;
        }

        public void setColumns(List<Column> columns) {
            this.columns = columns;
        }

    }

    public static class Column {

        private ColumnType type;
        private List<Box> boxes;

        public Column() {}

        public Column(ColumnType type, List<Box> boxes) {
            this.type = type;
            this.boxes = boxes;
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

        public void setBoxes(List<Box> boxes) {
            this.boxes = boxes;
        }

    }

    public static class Box {

        private BoxType type;
        private Integer value;

        public Box() {}

        public Box(BoxType type, Integer value) {
            this.type = type;
            this.value = value;
        }

        public BoxType getType() {
            return type;
        }

        public void setType(BoxType type) {
            this.type = type;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }
    
    }

}


