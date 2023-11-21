package com.tejko.models;

import java.io.Serializable;
import java.util.HashMap; 
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.tejko.models.enums.BoxType;
import com.tejko.models.enums.ColumnType;

public class Sheet implements Serializable {

    private Map<ColumnType, Column> columnMap;

    public Sheet() {
        this.columnMap = generateColumnMap();
    }

    private static Map<ColumnType, Column> generateColumnMap() {
        Map<ColumnType, Column> columnMap = new HashMap<>();
        for (ColumnType columnType : ColumnType.values()) {
            columnMap.put(columnType, new Column(columnType));
        }
        return columnMap;
    }

    public Map<ColumnType, Column> getColumnMap() {
        return columnMap;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public int getTopSectionSum() {  
        int topSectionSum = 0;
        for (Column column : columnMap.values()) {
            topSectionSum += column.getTopSectionSum();
        }
        return topSectionSum;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public int getMiddleSectionSum() { 
        int middleSectionSum = 0;
        for (Column column : columnMap.values()) {
            middleSectionSum += column.getMiddleSectionSum();
        }
        return middleSectionSum;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public int getBottomSectionSum() {
        int bottomSectionSum = 0;
        for (Column column : columnMap.values()) {
            bottomSectionSum += column.getBottomSectionSum();
        }
        return bottomSectionSum;
    }
    
    @JsonProperty(access = Access.READ_ONLY)
    public int getTotalSum() { 
        return getTopSectionSum() + getMiddleSectionSum() + getBottomSectionSum();
    }
    
    @JsonProperty(access = Access.READ_ONLY)
    public boolean isCompleted() {
        for (ColumnType columnType : columnMap.keySet()) {
            if (!columnMap.get(columnType).isCompleted()) {
                return false;
            }
        }
        return true;
    }

    public void fillBox(ColumnType columnType, BoxType boxType, int value) {
        columnMap.get(columnType).fillBox(boxType, value);
    }

    public boolean areAllNonAnnouncementColumnsCompleted() {
        for (Column column : columnMap.values()) {
            if (column.getType() != ColumnType.ANNOUNCEMENT && !column.isCompleted()) {
                return false;
            }
        }
        return true;
    }

}
