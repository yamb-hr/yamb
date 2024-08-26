package com.tejko.yamb.api.dto.responses;

import java.time.LocalDateTime;
import java.util.List;

import com.tejko.yamb.domain.enums.BoxType;
import com.tejko.yamb.domain.enums.ColumnType;
import com.tejko.yamb.domain.enums.GameStatus;

public class GameResponse {
 
    private String id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ShortPlayerResponse player;
    private SheetResponse sheet;
    private List<DiceResponse> dices;
    private int rollCount;
    private BoxType announcement;
    private GameStatus status;
    private int totalSum;

    public GameResponse() {}

    public GameResponse(String id, LocalDateTime createdAt, LocalDateTime updatedAt, ShortPlayerResponse player, SheetResponse sheet, List<DiceResponse> dices, int rollCount, BoxType announcement, GameStatus status, int totalSum) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.player = player;
        this.sheet = sheet;
        this.dices = dices;
        this.rollCount = rollCount;
        this.announcement = announcement;
        this.status = status;
        this.totalSum = totalSum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public ShortPlayerResponse getPlayer() {
        return player;
    }

    public void setPlayer(ShortPlayerResponse player) {
        this.player = player;
    }

    public SheetResponse getSheet() {
        return sheet;
    }

    public void setSheet(SheetResponse sheet) {
        this.sheet = sheet;
    }

    public List<DiceResponse> getDices() {
        return dices;
    }

    public void setDices(List<DiceResponse> dices) {
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

    public static class DiceResponse {

        private int index;
        private int value;

        public DiceResponse() {}

        public DiceResponse(int index, int value) {
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

    public static class SheetResponse {

        private List<ColumnResponse> columns;

        public SheetResponse() {}

        public SheetResponse(List<ColumnResponse> columns) {
            this.columns = columns;
        }

        public List<ColumnResponse> getColumns() {
            return columns;
        }

        public void setColumns(List<ColumnResponse> columns) {
            this.columns = columns;
        }

    }

    public static class ColumnResponse {

        private ColumnType type;
        private List<BoxResponse> boxes;

        public ColumnResponse() {}

        public ColumnResponse(ColumnType type, List<BoxResponse> boxes) {
            this.type = type;
            this.boxes = boxes;
        }

        public ColumnType getType() {
            return type;
        }

        public void setType(ColumnType type) {
            this.type = type;
        }

        public List<BoxResponse> getBoxes() {
            return boxes;
        }

        public void setBoxes(List<BoxResponse> boxes) {
            this.boxes = boxes;
        }

    }

    public static class BoxResponse {

        private BoxType type;
        private Integer value;

        public BoxResponse() {}

        public BoxResponse(BoxType type, Integer value) {
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


