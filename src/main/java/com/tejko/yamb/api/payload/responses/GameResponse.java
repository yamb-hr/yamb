package com.tejko.yamb.api.payload.responses;

import java.util.List;

import com.tejko.yamb.domain.enums.BoxType;
import com.tejko.yamb.domain.enums.ColumnType;
import com.tejko.yamb.domain.enums.GameStatus;

public class GameResponse extends BaseResponse {

    public PlayerResponse player;
    public SheetResponse sheet;
    public List<DiceResponse> dices;
    public int rollCount;
    public BoxType announcement;
    public GameStatus status;

    public static class DiceResponse {

        public int index;
        public int value;
    
    }

    public static class SheetResponse {

        public List<ColumnResponse> columns;

    }

    public static class ColumnResponse {

        public ColumnType type;
        public List<BoxResponse> boxes;

    }

    public static class BoxResponse {

        public BoxType type;
        public Integer value;
    
    }
    
}


