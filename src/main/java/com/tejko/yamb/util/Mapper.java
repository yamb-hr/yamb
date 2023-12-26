package com.tejko.yamb.util;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.tejko.yamb.models.Game;
import com.tejko.yamb.models.Log;
import com.tejko.yamb.models.Player;
import com.tejko.yamb.models.Role;
import com.tejko.yamb.models.Score;
import com.tejko.yamb.models.game.Box;
import com.tejko.yamb.models.game.Column;
import com.tejko.yamb.models.game.Dice;
import com.tejko.yamb.models.game.Sheet;
import com.tejko.yamb.models.payload.dto.BoxDTO;
import com.tejko.yamb.models.payload.dto.ColumnDTO;
import com.tejko.yamb.models.payload.dto.DiceDTO;
import com.tejko.yamb.models.payload.dto.GameDTO;
import com.tejko.yamb.models.payload.dto.LogDTO;
import com.tejko.yamb.models.payload.dto.PlayerDTO;
import com.tejko.yamb.models.payload.dto.RoleDTO;
import com.tejko.yamb.models.payload.dto.ScoreDTO;
import com.tejko.yamb.models.payload.dto.SheetDTO;

import io.swagger.v3.core.util.Json;

@Component
public class Mapper {

    public PlayerDTO toDTO(Player player) {
        return new PlayerDTO(player.getId(), player.getUsername(), player.getRoles().stream().map(role -> role.getLabel()).collect(Collectors.toList()));
    }

    public ScoreDTO toDTO(Score score) {
        return new ScoreDTO(score.getId(), score.getPlayer().getUsername(), score.getValue(), score.getDate());
    }

    public RoleDTO toDTO(Role role) {
        return new RoleDTO(role.getId(), role.getLabel(), role.getDescription());
    }

    public GameDTO toDTO(Game game) {
        return new GameDTO(game.getId(), game.getPlayer().getUsername(), toDTO(game.getSheet()), game.getDices().stream().map(dice -> toDTO(dice)).collect(Collectors.toList()), game.getRollCount(), game.getAnnouncement(), game.getStatus());
    }

    public SheetDTO toDTO(Sheet sheet) {
        return new SheetDTO(sheet.getColumns().stream().map(this::toDTO).collect(Collectors.toList()));
    }

    public ColumnDTO toDTO(Column column) {
        return new ColumnDTO(column.getType(), column.getBoxes().stream().map(this::toDTO).collect(Collectors.toList()));
    }

    public BoxDTO toDTO(Box box) {
        return new BoxDTO(box.getType(), box.getValue());
    }

    public DiceDTO toDTO(Dice dice) {
        return new DiceDTO(dice.getIndex(), dice.getValue());
    }

    public LogDTO toDTO(Log log) {
        return new LogDTO(log.getId(), log.getPlayer().getUsername(), Json.pretty(log.getData()), log.getMessage(), log.getLevel(), log.getTime());
    }

}