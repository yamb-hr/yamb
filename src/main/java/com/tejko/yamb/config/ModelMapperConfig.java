package com.tejko.yamb.config;

import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration.AccessLevel;
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
        
        modelMapper.getConfiguration()
                   .setMatchingStrategy(MatchingStrategies.STRICT)
                   .setFieldMatchingEnabled(true)
                   .setFieldAccessLevel(AccessLevel.PRIVATE);

        configureMappings(modelMapper);
        return modelMapper;
    }

    private void configureMappings(ModelMapper modelMapper) {
        TypeMap<Player, PlayerResponse> playerTypeMap = modelMapper.createTypeMap(Player.class, PlayerResponse.class);
        
        playerTypeMap.addMapping(Player::getUsername, PlayerResponse::setName)
                     .addMapping(src -> Hibernate.getClass(src).equals(RegisteredPlayer.class), PlayerResponse::setRegistered);

        modelMapper.createTypeMap(PlayerPreferences.class, PlayerPreferencesResponse.class);

        TypeMap<Score, ScoreResponse> scoreTypeMap = modelMapper.createTypeMap(Score.class, ScoreResponse.class);
        scoreTypeMap.addMapping(Score::getPlayer, ScoreResponse::setPlayer);

        TypeMap<Role, RoleResponse> roleTypeMap = modelMapper.createTypeMap(Role.class, RoleResponse.class);
        roleTypeMap.addMapping(Role::getLabel, RoleResponse::setName)
                   .addMapping(Role::getDescription, RoleResponse::setDescription);

        TypeMap<Game, GameResponse> gameTypeMap = modelMapper.createTypeMap(Game.class, GameResponse.class);
        gameTypeMap.addMapping(Game::getId, GameResponse::setId)
                   .addMapping(Game::getPlayerId, (dest, v) -> dest.getPlayer().setId((Long) v))
                   .addMapping(Game::getPlayerName, (dest, v) -> dest.getPlayer().setName((String) v))
                   .addMapping(Game::getSheet, GameResponse::setSheet)
                   .addMapping(Game::getDices, GameResponse::setDices);

        modelMapper.createTypeMap(Game.Sheet.class, GameResponse.SheetResponse.class)
                   .addMapping(Game.Sheet::getColumns, GameResponse.SheetResponse::setColumns);

        modelMapper.createTypeMap(Game.Column.class, GameResponse.ColumnResponse.class)
                   .addMapping(Game.Column::getType, GameResponse.ColumnResponse::setType)
                   .addMapping(Game.Column::getBoxes, GameResponse.ColumnResponse::setBoxes);

        modelMapper.createTypeMap(Game.Box.class, GameResponse.BoxResponse.class)
                   .addMapping(Game.Box::getType, GameResponse.BoxResponse::setType)
                   .addMapping(Game.Box::getValue, GameResponse.BoxResponse::setValue);

        modelMapper.createTypeMap(Game.Dice.class, GameResponse.DiceResponse.class)
                   .addMapping(Game.Dice::getIndex, GameResponse.DiceResponse::setIndex)
                   .addMapping(Game.Dice::getValue, GameResponse.DiceResponse::setValue);

        TypeMap<Log, LogResponse> logTypeMap = modelMapper.createTypeMap(Log.class, LogResponse.class);
        logTypeMap.addMapping(Log::getPlayer, LogResponse::setPlayer)
                  .addMapping(Log::getData, LogResponse::setData)
                  .addMapping(Log::getMessage, LogResponse::setMessage)
                  .addMapping(Log::getLevel, LogResponse::setLevel)
                  .addMapping(Log::getCreatedAt, LogResponse::setCreatedAt);
    }
}
