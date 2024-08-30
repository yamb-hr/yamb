package com.tejko.yamb.config;

import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tejko.yamb.api.dto.responses.GameResponse;
import com.tejko.yamb.api.dto.responses.LogResponse;
import com.tejko.yamb.api.dto.responses.PlayerPreferencesResponse;
import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.api.dto.responses.RoleResponse;
import com.tejko.yamb.api.dto.responses.ScoreResponse;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.domain.models.Log;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.PlayerPreferences;
import com.tejko.yamb.domain.models.RegisteredPlayer;
import com.tejko.yamb.domain.models.Role;
import com.tejko.yamb.domain.models.Score;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        // Set the matching strategy to STRICT for precise mapping
        modelMapper.getConfiguration()
                   .setMatchingStrategy(MatchingStrategies.STRICT)
                   .setFieldMatchingEnabled(true)
                   .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        // Custom mappings
        configureMappings(modelMapper);

        return modelMapper;
    }

    private void configureMappings(ModelMapper modelMapper) {

        modelMapper.typeMap(Player.class, PlayerResponse.class)
            .addMapping(Player::getUsername, PlayerResponse::setName)
            .addMapping(src -> Hibernate.getClass(src).equals(RegisteredPlayer.class), PlayerResponse::setRegistered);

        modelMapper.typeMap(PlayerPreferences.class, PlayerPreferencesResponse.class);

        modelMapper.typeMap(Score.class, ScoreResponse.class)
            .addMapping(Score::getPlayer, ScoreResponse::setPlayer);

        modelMapper.typeMap(Role.class, RoleResponse.class)
            .addMapping(Role::getLabel, RoleResponse::setName)
            .addMapping(Role::getDescription, RoleResponse::setDescription);

        modelMapper.typeMap(Game.class, GameResponse.class)
            .addMapping(Game::getId, GameResponse::setId)
            .addMapping(Game::getPlayerId, (dest, v) -> dest.getPlayer().setId((Long) v))
            .addMapping(Game::getPlayerName, (dest, v) -> dest.getPlayer().setName((String) v))
            .addMapping(Game::getSheet, GameResponse::setSheet)
            .addMapping(Game::getDices, GameResponse::setDices);

        modelMapper.typeMap(Game.Sheet.class, GameResponse.SheetResponse.class)
            .addMapping(Game.Sheet::getColumns, GameResponse.SheetResponse::setColumns);

        modelMapper.typeMap(Game.GameColumn.class, GameResponse.ColumnResponse.class)
            .addMapping(Game.GameColumn::getType, GameResponse.ColumnResponse::setType)
            .addMapping(Game.GameColumn::getBoxes, GameResponse.ColumnResponse::setBoxes);

        modelMapper.typeMap(Game.Box.class, GameResponse.BoxResponse.class)
            .addMapping(Game.Box::getType, GameResponse.BoxResponse::setType)
            .addMapping(Game.Box::getValue, GameResponse.BoxResponse::setValue);

        modelMapper.typeMap(Game.Dice.class, GameResponse.DiceResponse.class)
            .addMapping(Game.Dice::getIndex, GameResponse.DiceResponse::setIndex)
            .addMapping(Game.Dice::getValue, GameResponse.DiceResponse::setValue);

        modelMapper.typeMap(Log.class, LogResponse.class)
            .addMapping(Log::getPlayer, LogResponse::setPlayer)
            .addMapping(Log::getData, LogResponse::setData)
            .addMapping(Log::getMessage, LogResponse::setMessage)
            .addMapping(Log::getLevel, LogResponse::setLevel)
            .addMapping(Log::getCreatedAt, LogResponse::setCreatedAt);
    }
}
