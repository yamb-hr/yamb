package com.tejko.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.tejko.constants.YambConstants;
import com.tejko.models.enums.BoxType;
import com.tejko.models.enums.ColumnType;

public class Column implements Serializable { 

    private ColumnType type;

    private Map<BoxType, Box> boxMap;

    public Column() {}
    
    public Column(ColumnType type) {
        this.type = type;
        this.boxMap = generateBoxMap(type);
    }

    private Map<BoxType, Box> generateBoxMap(ColumnType columnType) {
        Map<BoxType, Box> boxMap = new HashMap<>();
        for (BoxType boxType : BoxType.values()) {
            boxMap.put(boxType, new Box(boxType, isBoxAvailableAtStart(type, boxType)));
        }
        return boxMap;
    }
    
    public ColumnType getType() {
        return type;
    }

    public void setType(ColumnType type) {
        this.type = type;
    }
    
    public Map<BoxType, Box> getBoxMap() {
        return boxMap;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public int getTopSectionSum() {
        int topSectionSum = 0;
        for (BoxType boxType : YambConstants.TOP_SECTION) {
            topSectionSum += boxMap.get(boxType).getValue();
        }
        if (topSectionSum >= YambConstants.TOP_SECTION_SUM_BONUS_THRESHOLD) {
            topSectionSum += YambConstants.TOP_SECTION_SUM_BONUS;
        }
        return topSectionSum;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public int getMiddleSectionSum() {
        int middleSectionSum = 0;
        Box ones = boxMap.get(BoxType.ONES);
        Box max = boxMap.get(BoxType.MAX);
        Box min = boxMap.get(BoxType.MIN);
        if (ones.isFilled() && max.isFilled() && min.isFilled()) {
            middleSectionSum = ones.getValue() * (max.getValue() - min.getValue());
        }
        return middleSectionSum;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public int getBottomSectionSum() {
        int bottomSectionSum = 0;
        for (BoxType boxType : YambConstants.BOTTOM_SECTION) {
            bottomSectionSum += boxMap.get(boxType).getValue();
        }
        return bottomSectionSum;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public boolean isCompleted() {
        return getNumOfAvailableBoxes() == 0;
    }

    private int getNumOfAvailableBoxes() {
        int numOfAvailableBoxes = 0;
        for (Box box : boxMap.values()) {
            if (box.isAvailable()) {
                numOfAvailableBoxes += 1;
            }
        }
        return numOfAvailableBoxes;
    }  

    public void fillBox(BoxType boxType, int value) { 
        Box selectedBox = boxMap.get(boxType);
        selectedBox.fill(value);
        makeNextBoxAvailable(boxType);
    }

    private void makeNextBoxAvailable(BoxType selectedBoxType) {
        Box nextBox = null;
        if (type == ColumnType.DOWNWARDS && selectedBoxType != BoxType.YAMB) {
            nextBox = boxMap.get(BoxType.values()[selectedBoxType.ordinal() + 1]);
        } else if (type == ColumnType.UPWARDS && selectedBoxType != BoxType.ONES) {
            nextBox = boxMap.get(BoxType.values()[selectedBoxType.ordinal() - 1]);
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
