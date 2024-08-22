package com.tejko.yamb.util;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.tejko.yamb.api.payload.responses.GameResponse;
import com.tejko.yamb.api.payload.responses.LogResponse;
import com.tejko.yamb.api.payload.responses.PlayerResponse;
import com.tejko.yamb.api.payload.responses.RoleResponse;
import com.tejko.yamb.api.payload.responses.ScoreResponse;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.domain.models.Log;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.Role;
import com.tejko.yamb.domain.models.Score;

import io.swagger.v3.core.util.Json;

@Component
public class PayloadMapper {

    public PlayerResponse toDTO(Player player) {
        PlayerResponse playerResponse = new PlayerResponse();
        playerResponse.id = player.getExternalId();
        playerResponse.createdAt = player.getCreatedAt();
        playerResponse.updatedAt = player.getUpdatedAt();
        playerResponse.name = player.getUsername();
        playerResponse.tempUser = player.isTempUser();
        playerResponse.roles = player.getRoles().stream().map(role -> toDTO(role)).collect(Collectors.toList());
        return playerResponse;
    }

    public ScoreResponse toDTO(Score score) {
        ScoreResponse scoreResponse = new ScoreResponse();
        scoreResponse.id = score.getExternalId();
        scoreResponse.createdAt = score.getCreatedAt();
        scoreResponse.updatedAt = score.getUpdatedAt();
        scoreResponse.player = toDTO(score.getPlayer());
        scoreResponse.value = score.getValue();
        return scoreResponse;
    }

    public RoleResponse toDTO(Role role) {
        RoleResponse roleResponse = new RoleResponse();
        roleResponse.id = role.getExternalId();
        roleResponse.name = role.getLabel();
        roleResponse.description = role.getDescription();
        return roleResponse;
    }

    public GameResponse toDTO(Game game) {
        GameResponse gameResponse = new GameResponse();
        gameResponse.id = game.getExternalId();
        gameResponse.createdAt = game.getCreatedAt();
        gameResponse.updatedAt = game.getUpdatedAt();
        gameResponse.player = toDTO(game.getPlayer());
        gameResponse.sheet = toDTO(game.getSheet());
        gameResponse.dices = game.getDices().stream().map(dice -> toDTO(dice)).collect(Collectors.toList());
        gameResponse.rollCount = game.getRollCount();
        gameResponse.announcement = game.getAnnouncement();
        gameResponse.status = game.getStatus();
        return gameResponse;
    }

    public GameResponse.SheetResponse toDTO(Game.Sheet sheet) {
        GameResponse.SheetResponse sheetResponse = new GameResponse.SheetResponse();
        sheetResponse.columns = sheet.getColumns().stream().map(this::toDTO).collect(Collectors.toList());
        return sheetResponse;
    }

    public GameResponse.ColumnResponse toDTO(Game.GameColumn column) {
        GameResponse.ColumnResponse columnResponse = new GameResponse.ColumnResponse();
        columnResponse.type = column.getType();
        columnResponse.boxes = column.getBoxes().stream().map(this::toDTO).collect(Collectors.toList());
        return columnResponse;
    }

    public GameResponse.BoxResponse toDTO(Game.Box box) {
        GameResponse.BoxResponse boxResponse = new GameResponse.BoxResponse();
        boxResponse.type = box.getType();
        boxResponse.value = box.getValue();
        return boxResponse;
    }

    public GameResponse.DiceResponse toDTO(Game.Dice dice) {
        GameResponse.DiceResponse diceResponse = new GameResponse.DiceResponse();
        diceResponse.index = dice.getIndex();
        diceResponse.value = dice.getValue();
        return diceResponse;
    }

    public LogResponse toDTO(Log log) {
        LogResponse logResponse = new LogResponse();
        logResponse.id = log.getExternalId();
        logResponse.player = toDTO(log.getPlayer());
        logResponse.data = Json.pretty(log.getData());
        logResponse.message = log.getMessage();
        logResponse.level = log.getLevel();
        logResponse.createdAt = log.getCreatedAt();
        return logResponse;
    }

}