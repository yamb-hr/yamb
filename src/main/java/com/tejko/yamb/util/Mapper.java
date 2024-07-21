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
public class Mapper {

    public PlayerResponse toDTO(Player player) {
        PlayerResponse playerDTO = new PlayerResponse();
        playerDTO.id = player.getExternalId();
        playerDTO.createdAt = player.getCreatedAt();
        playerDTO.updatedAt = player.getUpdatedAt();
        playerDTO.name = player.getUsername();
        playerDTO.roles = player.getRoles().stream().map(role -> role.getLabel()).collect(Collectors.toList());
        return playerDTO;
    }

    public ScoreResponse toDTO(Score score) {
        ScoreResponse scoreDTO = new ScoreResponse();
        scoreDTO.id = score.getExternalId();
        scoreDTO.createdAt = score.getCreatedAt();
        scoreDTO.updatedAt = score.getUpdatedAt();
        scoreDTO.player = score.getPlayer().getUsername();
        scoreDTO.value = score.getValue();
        return scoreDTO;
    }

    public RoleResponse toDTO(Role role) {
        RoleResponse roleDTO = new RoleResponse();
        roleDTO.id = role.getExternalId();
        roleDTO.name = role.getLabel();
        roleDTO.description = role.getDescription();
        return roleDTO;
    }

    public GameResponse toDTO(Game game) {
        GameResponse gameDTO = new GameResponse();
        gameDTO.id = game.getExternalId();
        gameDTO.createdAt = game.getCreatedAt();
        gameDTO.updatedAt = game.getUpdatedAt();
        gameDTO.player = game.getPlayer().getUsername();
        gameDTO.sheet = toDTO(game.getSheet());
        gameDTO.dices = game.getDices().stream().map(dice -> toDTO(dice)).collect(Collectors.toList());
        gameDTO.rollCount = game.getRollCount();
        gameDTO.announcement = game.getAnnouncement();
        gameDTO.status = game.getStatus();
        return gameDTO;
    }

    public GameResponse.SheetDTO toDTO(Game.Sheet sheet) {
        GameResponse.SheetDTO sheetDTO = new GameResponse.SheetDTO();
        sheetDTO.columns = sheet.getColumns().stream().map(this::toDTO).collect(Collectors.toList());
        return sheetDTO;
    }

    public GameResponse.ColumnDTO toDTO(Game.GameColumn column) {
        GameResponse.ColumnDTO columnDTO = new GameResponse.ColumnDTO();
        columnDTO.type = column.getType();
        columnDTO.boxes = column.getBoxes().stream().map(this::toDTO).collect(Collectors.toList());
        return columnDTO;
    }

    public GameResponse.BoxDTO toDTO(Game.Box box) {
        GameResponse.BoxDTO boxDTO = new GameResponse.BoxDTO();
        boxDTO.type = box.getType();
        boxDTO.value = box.getValue();
        return boxDTO;
    }

    public GameResponse.DiceDTO toDTO(Game.Dice dice) {
        GameResponse.DiceDTO diceDTO = new GameResponse.DiceDTO();
        diceDTO.index = dice.getIndex();
        diceDTO.value = dice.getValue();
        return diceDTO;
    }

    public LogResponse toDTO(Log log) {
        LogResponse logDTO = new LogResponse();
        logDTO.id = log.getExternalId();
        logDTO.player = log.getPlayer().getUsername();
        logDTO.data = Json.pretty(log.getData());
        logDTO.message = log.getMessage();
        logDTO.level = log.getLevel();
        logDTO.createdAt = log.getCreatedAt();
        return logDTO;
    }

}