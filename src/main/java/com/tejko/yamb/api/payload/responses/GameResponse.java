package com.tejko.yamb.api.payload.responses;

import java.util.List;

import com.tejko.yamb.domain.enums.BoxType;
import com.tejko.yamb.domain.enums.ColumnType;
import com.tejko.yamb.domain.enums.GameStatus;

public class GameResponse extends BaseDTO {

    public String player;
    public SheetDTO sheet;
    public List<DiceDTO> dices;
    public int rollCount;
    public BoxType announcement;
    public GameStatus status;

    public static class DiceDTO {

        public int index;
        public int value;
    
    }

    public static class SheetDTO {

        public List<ColumnDTO> columns;

    }

    public static class ColumnDTO {

        public ColumnType type;
        public List<BoxDTO> boxes;

    }

    public static class BoxDTO {

        public BoxType type;
        public Integer value;
    
    }
    
}


