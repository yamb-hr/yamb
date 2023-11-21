package com.tejko.models.payload;

import java.util.List;
import java.util.UUID;

import com.tejko.models.enums.BoxType;
import com.tejko.models.enums.ColumnType;

public class GameResponse {

    public UUID id;
    public Sheet sheet;
    public List<Dice> diceList;
    public int rollCount;
    public BoxType announcement;

    public GameResponse(UUID id, Sheet sheet, List<Dice> diceList, int rollCount, BoxType announcement) {
        this.id = id;
        this.sheet = sheet;
        this.diceList = diceList;
        this.rollCount = rollCount;
        this.announcement = announcement;
    }

    public static class Dice {
        
        public int order;
        public int value;

        public Dice(int order, int value) {
            this.order = order;
            this.value = value;
        }
    }

    public static class Sheet {

        public List<Column> columns;
        public int topSectionSum;
        public int middleSectionSum;
        public int bottomSectionSum;
        public int totalSum;
        public boolean isCompleted;

        public Sheet(List<Column> columns, int topSectionSum, int middleSectionSum, int bottomSectionSum, int totalSum, boolean isCompleted) {
            this.columns = columns;
            this.topSectionSum = topSectionSum;
            this.middleSectionSum = middleSectionSum;
            this.bottomSectionSum = bottomSectionSum;
            this.totalSum = totalSum;
            this.isCompleted = isCompleted;
        }

    }
    
    public static class Column {

        public ColumnType type;
        public List<Box> boxes;
        public int topSectionSum;
        public int middleSectionSum;
        public int bottomSectionSum;
        public boolean isCompleted;

        public Column(ColumnType type, List<Box> boxes, int topSectionSum, int middleSectionSum, int bottomSectionSum, boolean isCompleted) {
            this.type = type;
            this.boxes = boxes;
            this.topSectionSum = topSectionSum;
            this.middleSectionSum = middleSectionSum;
            this.bottomSectionSum = bottomSectionSum;
            this.isCompleted = isCompleted;
        }
        
    }

    public static class Box {
        
        public BoxType type;
        public int value;
        public boolean isAvailable;
        public boolean isFilled;

        public Box(BoxType type, int value, boolean isAvailable, boolean isFilled) {
            this.type = type;
            this.value = value;
            this.isAvailable = isAvailable;
            this.isFilled = isFilled;
        }
        
    }
    
}
