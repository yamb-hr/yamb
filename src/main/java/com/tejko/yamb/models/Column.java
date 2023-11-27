package com.tejko.yamb.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.tejko.yamb.constants.GameConstants;
import com.tejko.yamb.models.enums.BoxType;
import com.tejko.yamb.models.enums.ColumnType;

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

    @JsonProperty(access = Access.READ_ONLY)
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

    @JsonProperty(access = Access.READ_ONLY)
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

    @JsonProperty(access = Access.READ_ONLY)
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

    @JsonProperty(access = Access.READ_ONLY)
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
