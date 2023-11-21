package com.tejko.models;

import java.beans.Transient;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tejko.models.enums.BoxType;
import com.tejko.models.enums.ColumnType;

@JsonIgnoreProperties(ignoreUnknown = true)
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
    
    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public List<Column> getColumnList() { 
        return columnMap.values().stream().collect(Collectors.toList());
    }

    public int getTopSectionSum() {  
        int topSectionSum = 0;
        for (Column column : columnMap.values()) {
            topSectionSum += column.getTopSectionSum();
        }
        return topSectionSum;
    }

    public int getMiddleSectionSum() { 
        int middleSectionSum = 0;
        for (Column column : columnMap.values()) {
            middleSectionSum += column.getMiddleSectionSum();
        }
        return middleSectionSum;
    }

    public int getBottomSectionSum() {
        int bottomSectionSum = 0;
        for (Column column : columnMap.values()) {
            bottomSectionSum += column.getBottomSectionSum();
        }
        return bottomSectionSum;
    }
    
    public int getTotalSum() { 
        return getTopSectionSum() + getMiddleSectionSum() + getBottomSectionSum();
    }
    
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
