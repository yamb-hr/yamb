package com.tejko.utils;

import java.util.List;
import java.util.stream.Collectors;

import com.tejko.models.Box;
import com.tejko.models.Column;
import com.tejko.models.Dice;
import com.tejko.models.Game;
import com.tejko.models.Sheet;
import com.tejko.models.payload.GameResponse;

public class ModelMapper {

    public GameResponse mapToGameResponse(Game game) {
        GameResponse.Sheet sheet = mapSheet(game.getSheet());
        List<GameResponse.Dice> diceList = game.getDiceList().stream().map(this::mapDice).collect(Collectors.toList());

        return new GameResponse(
            game.getId(),
            sheet, 
            diceList, 
            game.getRollCount(), 
            game.getAnnouncement()
        );
    }

    private GameResponse.Dice mapDice(Dice dice) {
        return new GameResponse.Dice(
            dice.getOrder(), 
            dice.getValue()
        );
    }

    private GameResponse.Sheet mapSheet(Sheet sheet) {
        List<GameResponse.Column> columnList = sheet.getColumnList().stream().map(this::mapColumn).collect(Collectors.toList());
        return new GameResponse.Sheet(
            columnList, 
            sheet.getTopSectionSum(), 
            sheet.getMiddleSectionSum(), 
            sheet.getBottomSectionSum(), 
            sheet.getTotalSum(), 
            sheet.isCompleted()
        );   
    }

    private GameResponse.Column mapColumn(Column column) {
        List<GameResponse.Box> boxList = column.getBoxList().stream().map(this::mapBox).collect(Collectors.toList());
        return new GameResponse.Column(
            column.getType(), 
            boxList, 
            column.getTopSectionSum(), 
            column.getMiddleSectionSum(), 
            column.getBottomSectionSum(), 
            column.isCompleted()
        );
    }

    public GameResponse.Box mapBox(Box box) {
        return new GameResponse.Box (
            box.getType(), 
            box.getValue(), 
            box.isFilled(), 
            box.isAvailable()
        );
    }
    
}
