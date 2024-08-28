package com.tejko.yamb.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import com.tejko.yamb.api.dto.responses.GameResponse;
import com.tejko.yamb.api.dto.responses.LogResponse;
import com.tejko.yamb.api.dto.responses.PlayerPreferencesResponse;
import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.api.dto.responses.RoleResponse;
import com.tejko.yamb.api.dto.responses.ScoreResponse;
import com.tejko.yamb.api.dto.responses.ShortPlayerResponse;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.domain.models.Log;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.PlayerPreferences;
import com.tejko.yamb.domain.models.RegisteredPlayer;
import com.tejko.yamb.domain.models.Role;
import com.tejko.yamb.domain.models.Score;

@Component
public class CustomObjectMapper {

    public <T, R, C extends Collection<R>> C mapCollection(Collection<T> sourceCollection, Function<T, R> mapToResponse, Supplier<C> collectionFactory) {
        return sourceCollection.stream()
            .map(mapToResponse)
            .collect(Collectors.toCollection(collectionFactory));
    }

    public PlayerResponse mapToResponse(Player player) {
        if (player == null) {
            return null;
        }

        PlayerResponse playerResponse = new PlayerResponse();
        playerResponse.setId(player.getId());
        playerResponse.setCreatedAt(player.getCreatedAt());
        playerResponse.setUpdatedAt(player.getUpdatedAt());
        playerResponse.setName(player.getUsername());
        playerResponse.setRoles(mapCollection(player.getRoles(), this::mapToResponse, HashSet::new));
        playerResponse.setRegistered(Hibernate.getClass(player).equals(RegisteredPlayer.class));
        
        return playerResponse;
    }

    public PlayerPreferencesResponse mapToResponse(PlayerPreferences playerPreferences) {
        if (playerPreferences == null) {
            return null;
        }

        PlayerPreferencesResponse preferencesResponse = new PlayerPreferencesResponse();
        preferencesResponse.setLanguage(playerPreferences.getLanguage());
        preferencesResponse.setTheme(playerPreferences.getTheme());

        return preferencesResponse;
    }

    public ScoreResponse mapToResponse(Score score) {
        if (score == null) {
            return null;
        }

        ScoreResponse scoreResponse = new ScoreResponse();
        scoreResponse.setId(score.getId());
        scoreResponse.setCreatedAt(score.getCreatedAt());
        scoreResponse.setPlayer(mapToResponse(score.getPlayer()));
        scoreResponse.setValue(score.getValue());

        return scoreResponse;
    }

    public RoleResponse mapToResponse(Role role) {
        if (role == null) {
            return null;
        }

        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setName(role.getLabel());
        roleResponse.setDescription(role.getDescription());
        
        return roleResponse;
    }

    public GameResponse mapToResponse(Game game) {
        if (game == null) {
            return null;
        }

        GameResponse gameResponse = new GameResponse();
        gameResponse.setId(game.getId());
        gameResponse.setCreatedAt(game.getCreatedAt());
        gameResponse.setUpdatedAt(game.getUpdatedAt());
        gameResponse.setPlayer(new ShortPlayerResponse(game.getPlayerId(), game.getPlayerName()));
        gameResponse.setSheet(mapToResponse(game.getSheet()));
        gameResponse.setDices(mapCollection(game.getDices(), this::mapToResponse, ArrayList::new));
        gameResponse.setRollCount(game.getRollCount());
        gameResponse.setAnnouncement(game.getAnnouncement());
        gameResponse.setStatus(game.getStatus());
        gameResponse.setTotalSum(game.getTotalSum());
    
        return gameResponse;
    }

    public GameResponse.SheetResponse mapToResponse(Game.Sheet sheet) {
        if (sheet == null) {
            return null;
        }
    
        GameResponse.SheetResponse sheetResponse = new GameResponse.SheetResponse();
        sheetResponse.setColumns(mapCollection(sheet.getColumns(), this::mapToResponse, ArrayList::new));

        return sheetResponse;
    }

    public GameResponse.ColumnResponse mapToResponse(Game.GameColumn column) {
        if (column == null) {
            return null;
        }

        GameResponse.ColumnResponse columnResponse = new GameResponse.ColumnResponse();
        columnResponse.setType(column.getType());
        columnResponse.setBoxes(mapCollection(column.getBoxes(), this::mapToResponse, ArrayList::new));

        return columnResponse;
    }

    public GameResponse.BoxResponse mapToResponse(Game.Box box) {
        if (box == null) {
            return null;
        }

        GameResponse.BoxResponse boxResponse = new GameResponse.BoxResponse();
        boxResponse.setType(box.getType());
        boxResponse.setValue(box.getValue());

        return boxResponse;
    }

    public GameResponse.DiceResponse mapToResponse(Game.Dice dice) {
        if (dice == null) {
            return null;
        }

        GameResponse.DiceResponse diceResponse = new GameResponse.DiceResponse();
        diceResponse.setIndex(dice.getIndex());
        diceResponse.setValue(dice.getValue());

        return diceResponse;
    }

    public LogResponse mapToResponse(Log log) {
        if (log == null) {
            return null;
        }

        LogResponse logResponse = new LogResponse();
        logResponse.setId(log.getId());
        logResponse.setPlayer(mapToResponse(log.getPlayer()));
        logResponse.setData(log.getData());
        logResponse.setMessage(log.getMessage());
        logResponse.setLevel(log.getLevel());
        logResponse.setCreatedAt(log.getCreatedAt());
        
        return logResponse;
    }

}