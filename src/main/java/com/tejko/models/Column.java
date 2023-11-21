package com.tejko.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.tejko.constants.YambConstants;
import com.tejko.models.enums.BoxType;
import com.tejko.models.enums.ColumnType;

public class Column implements Serializable { 

    private ColumnType type;
    private List<Box> boxList;

    private Column() {}

    private Column (ColumnType type, List<Box> boxList) {
        this.type = type; 
        this.boxList = boxList;
    }
    
    public static Column getInstance(ColumnType type) {
        return new Column(type, generateBoxList(type));
    }

    private static List<Box> generateBoxList(ColumnType columnType) {
        List<Box> boxList = new ArrayList<>();
        for (BoxType boxType : BoxType.values()) {
            boxList.add(Box.getInstance(boxType, isBoxAvailableAtStart(columnType, boxType)));
        }
        return boxList;
    }
    
    public ColumnType getType() {
        return type;
    }

    public void setType(ColumnType type) {
        this.type = type;
    }
    
    public List<Box> getBoxList() {
        return boxList;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public int getTopSectionSum() {
        int topSectionSum = 0;
        for (BoxType boxType : YambConstants.TOP_SECTION) {
            topSectionSum += boxList.get(boxType.ordinal()).getValue();
        }
        if (topSectionSum >= YambConstants.TOP_SECTION_SUM_BONUS_THRESHOLD) {
            topSectionSum += YambConstants.TOP_SECTION_SUM_BONUS;
        }
        return topSectionSum;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public int getMiddleSectionSum() {
        int middleSectionSum = 0;
        Box ones = boxList.get(BoxType.ONES.ordinal());
        Box max = boxList.get(BoxType.MAX.ordinal());
        Box min = boxList.get(BoxType.MIN.ordinal());
        if (ones.isFilled() && max.isFilled() && min.isFilled()) {
            middleSectionSum = ones.getValue() * (max.getValue() - min.getValue());
        }
        return middleSectionSum;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public int getBottomSectionSum() {
        int bottomSectionSum = 0;
        for (BoxType boxType : YambConstants.BOTTOM_SECTION) {
            bottomSectionSum += boxList.get(boxType.ordinal()).getValue();
        }
        return bottomSectionSum;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public boolean isCompleted() {
        return getNumOfAvailableBoxes() == 0;
    }

    private int getNumOfAvailableBoxes() {
        int numOfAvailableBoxes = 0;
        for (Box box : boxList) {
            if (box.isAvailable()) {
                numOfAvailableBoxes += 1;
            }
        }
        return numOfAvailableBoxes;
    }  

    public void fillBox(BoxType boxType, int value) { 
        Box selectedBox = boxList.get(boxType.ordinal());
        selectedBox.fill(value);
        makeNextBoxAvailable(boxType);
    }

    private void makeNextBoxAvailable(BoxType selectedBoxType) {
        Box nextBox = null;
        if (type == ColumnType.DOWNWARDS && selectedBoxType != BoxType.YAMB) {
            nextBox = boxList.get(selectedBoxType.ordinal() + 1);
        } else if (type == ColumnType.UPWARDS && selectedBoxType != BoxType.ONES) {
            nextBox = boxList.get(selectedBoxType.ordinal() - 1);
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
