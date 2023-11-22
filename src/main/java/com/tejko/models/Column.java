package com.tejko.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.tejko.constants.GameConstants;
import com.tejko.models.enums.BoxType;
import com.tejko.models.enums.ColumnType;

public class Column implements Serializable { 

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
            boxes.add(Box.getInstance(boxType, isBoxAvailableAtStart(columnType, boxType)));
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

    @JsonProperty(access = Access.READ_ONLY)
    public int getTopSectionSum() {
        int topSectionSum = 0;
        for (BoxType boxType : GameConstants.TOP_SECTION) {
            topSectionSum += boxes.get(boxType.ordinal()).getValue();
        }
        if (topSectionSum >= GameConstants.TOP_SECTION_SUM_BONUS_THRESHOLD) {
            topSectionSum += GameConstants.TOP_SECTION_SUM_BONUS;
        }
        return topSectionSum;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public int getMiddleSectionSum() {
        int middleSectionSum = 0;
        Box ones = boxes.get(BoxType.ONES.ordinal());
        Box max = boxes.get(BoxType.MAX.ordinal());
        Box min = boxes.get(BoxType.MIN.ordinal());
        if (ones.isFilled() && max.isFilled() && min.isFilled()) {
            middleSectionSum = ones.getValue() * (max.getValue() - min.getValue());
        }
        return middleSectionSum;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public int getBottomSectionSum() {
        int bottomSectionSum = 0;
        for (BoxType boxType : GameConstants.BOTTOM_SECTION) {
            bottomSectionSum += boxes.get(boxType.ordinal()).getValue();
        }
        return bottomSectionSum;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public boolean isCompleted() {
        return getNumOfAvailableBoxes() == 0;
    }

    private int getNumOfAvailableBoxes() {
        int numOfAvailableBoxes = 0;
        for (Box box : boxes) {
            if (box.isAvailable()) {
                numOfAvailableBoxes += 1;
            }
        }
        return numOfAvailableBoxes;
    }  

    public void fillBox(BoxType boxType, int value) { 
        Box selectedBox = boxes.get(boxType.ordinal());
        selectedBox.fill(value);
        makeNextBoxAvailable(boxType);
    }

    private void makeNextBoxAvailable(BoxType selectedBoxType) {
        Box nextBox = null;
        if (type == ColumnType.DOWNWARDS && selectedBoxType != BoxType.YAMB) {
            nextBox = boxes.get(selectedBoxType.ordinal() + 1);
        } else if (type == ColumnType.UPWARDS && selectedBoxType != BoxType.ONES) {
            nextBox = boxes.get(selectedBoxType.ordinal() - 1);
        }
    
        if (nextBox != null) {
            nextBox.makeAvailable();
        }
    }

    private static boolean isBoxAvailableAtStart(ColumnType columnType, BoxType boxType) {
        return columnType == ColumnType.FREE
            || columnType == ColumnType.ANNOUNCEMENT
            || columnType == ColumnType.DOWNWARDS
                && boxType == BoxType.ONES
            || columnType == ColumnType.UPWARDS
                && boxType == BoxType.YAMB;
    }

    
}
