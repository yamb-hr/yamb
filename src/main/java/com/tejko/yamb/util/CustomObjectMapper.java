package com.tejko.yamb.util;

import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import com.tejko.yamb.api.dto.responses.GameResponse;
import com.tejko.yamb.api.dto.responses.LogResponse;
import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.api.dto.responses.RoleResponse;
import com.tejko.yamb.api.dto.responses.ScoreResponse;
import com.tejko.yamb.api.dto.responses.ShortPlayerResponse;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.domain.models.Log;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.RegisteredPlayer;
import com.tejko.yamb.domain.models.Role;
import com.tejko.yamb.domain.models.Score;

@Component
public class CustomObjectMapper {

    public PlayerResponse mapToResponse(Player player) {
        PlayerResponse playerResponse = new PlayerResponse();
        playerResponse.setId(player.getId());
        playerResponse.setCreatedAt(player.getCreatedAt());
        playerResponse.setUpdatedAt(player.getUpdatedAt());
        playerResponse.setName(player.getUsername());
        playerResponse.setRoles(player.getRoles().stream().map(role -> mapToResponse(role)).collect(Collectors.toList()));
        playerResponse.setRegistered(Hibernate.getClass(player).equals(RegisteredPlayer.class));
        return playerResponse;
    }

    public ScoreResponse mapToResponse(Score score) {
        ScoreResponse scoreResponse = new ScoreResponse();
        scoreResponse.setId(score.getId());
        scoreResponse.setCreatedAt(score.getCreatedAt());
        scoreResponse.setPlayer(mapToResponse(score.getPlayer()));
        scoreResponse.setValue(score.getValue());
        return scoreResponse;
    }

    public RoleResponse mapToResponse(Role role) {
        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setName(role.getLabel());
        roleResponse.setDescription(role.getDescription());
        return roleResponse;
    }

    public GameResponse mapToResponse(Game game) {
        GameResponse gameResponse = new GameResponse();
        gameResponse.setId(game.getId());
        gameResponse.setCreatedAt(game.getCreatedAt());
        gameResponse.setUpdatedAt(game.getUpdatedAt());
        gameResponse.setStatus(game.getStatus());
        gameResponse.setPlayer(new ShortPlayerResponse(game.getPlayerId(), game.getPlayerName()));
        gameResponse.setSheet(mapToResponse(game.getSheet()));
        gameResponse.setDices(game.getDices().stream().map(dice -> mapToResponse(dice)).collect(Collectors.toList()));
        gameResponse.setRollCount(game.getRollCount());
        gameResponse.setAnnouncement(game.getAnnouncement());
        gameResponse.setTotalSum(game.getTotalSum());
        return gameResponse;
    }

    public GameResponse.SheetResponse mapToResponse(Game.Sheet sheet) {
        GameResponse.SheetResponse sheetResponse = new GameResponse.SheetResponse();
        sheetResponse.setColumns(sheet.getColumns().stream().map(this::mapToResponse).collect(Collectors.toList()));
        return sheetResponse;
    }

    public GameResponse.ColumnResponse mapToResponse(Game.GameColumn column) {
        GameResponse.ColumnResponse columnResponse = new GameResponse.ColumnResponse();
        columnResponse.setType(column.getType());
        columnResponse.setBoxes(column.getBoxes().stream().map(this::mapToResponse).collect(Collectors.toList()));
        return columnResponse;
    }

    public GameResponse.BoxResponse mapToResponse(Game.Box box) {
        GameResponse.BoxResponse boxResponse = new GameResponse.BoxResponse();
        boxResponse.setType(box.getType());
        boxResponse.setValue(box.getValue());
        return boxResponse;
    }

    public GameResponse.DiceResponse mapToResponse(Game.Dice dice) {
        GameResponse.DiceResponse diceResponse = new GameResponse.DiceResponse();
        diceResponse.setIndex(dice.getIndex());
        diceResponse.setValue(dice.getValue());
        return diceResponse;
    }

    public LogResponse mapToResponse(Log log) {
        LogResponse logResponse = new LogResponse();
        logResponse.setId(log.getId());
        if (log.getPlayer() != null) {
            logResponse.setPlayer(mapToResponse(log.getPlayer()));
        }
        logResponse.setData(log.getData());
        logResponse.setMessage(log.getMessage());
        logResponse.setLevel(log.getLevel());
        logResponse.setCreatedAt(log.getCreatedAt());
        return logResponse;
    }

}