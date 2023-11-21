package com.tejko.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.tejko.models.enums.BoxType;
import com.tejko.models.enums.ColumnType;

public class Sheet implements Serializable {

    private List<Column> columnList;

    private Sheet() { }

    private Sheet(List<Column> columnList) {
        this.columnList = columnList;
    }

    public static Sheet getInstance() {
        return new Sheet(generateColumnList());
    }

    private static List<Column> generateColumnList() {
        List<Column> columnList = new ArrayList<>();
        for (ColumnType columnType : ColumnType.values()) {
            columnList.add(Column.getInstance(columnType));
        }
        return columnList;
    }

    public List<Column> getColumnList() {
        return columnList;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public int getTopSectionSum() {  
        int topSectionSum = 0;
        for (Column column : columnList) {
            topSectionSum += column.getTopSectionSum();
        }
        return topSectionSum;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public int getMiddleSectionSum() { 
        int middleSectionSum = 0;
        for (Column column : columnList) {
            middleSectionSum += column.getMiddleSectionSum();
        }
        return middleSectionSum;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public int getBottomSectionSum() {
        int bottomSectionSum = 0;
        for (Column column : columnList) {
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
        for (Column column : columnList) {
            if (!column.isCompleted()) {
                return false;
            }
        }
        return true;
    }

    public void fillBox(ColumnType columnType, BoxType boxType, int value) {
        columnList.get(columnType.ordinal()).fillBox(boxType, value);
    }

    public boolean areAllNonAnnouncementColumnsCompleted() {
        for (Column column : columnList) {
            if (column.getType() != ColumnType.ANNOUNCEMENT && !column.isCompleted()) {
                return false;
            }
        }
        return true;
    }

}
