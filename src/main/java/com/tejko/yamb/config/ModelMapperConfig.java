package com.tejko.yamb.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.tejko.yamb.api.dto.responses.AuthResponse;
import com.tejko.yamb.api.dto.responses.GameResponse;
import com.tejko.yamb.api.dto.responses.GlobalPlayerStatsResponse;
import com.tejko.yamb.api.dto.responses.GlobalScoreStatsResponse;
import com.tejko.yamb.api.dto.responses.LogResponse;
import com.tejko.yamb.api.dto.responses.PlayerPreferencesResponse;
import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.api.dto.responses.PlayerStatsResponse;
import com.tejko.yamb.api.dto.responses.RoleResponse;
import com.tejko.yamb.api.dto.responses.ScoreResponse;
import com.tejko.yamb.api.dto.responses.ShortGameResponse;
import com.tejko.yamb.api.dto.responses.ShortPlayerResponse;
import com.tejko.yamb.api.dto.responses.ShortScoreResponse;
import com.tejko.yamb.domain.models.AnonymousPlayer;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.domain.models.GlobalPlayerStats;
import com.tejko.yamb.domain.models.GlobalScoreStats;
import com.tejko.yamb.domain.models.Log;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.PlayerPreferences;
import com.tejko.yamb.domain.models.PlayerStats;
import com.tejko.yamb.domain.models.PlayerWithToken;
import com.tejko.yamb.domain.models.RegisteredPlayer;
import com.tejko.yamb.domain.models.Role;
import com.tejko.yamb.domain.models.Score;

@Configuration
public class ModelMapperConfig {

    @Bean
    @Primary
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(AccessLevel.PRIVATE);
        configureMappings(modelMapper);
        return modelMapper;
    }

    private void configureMappings(ModelMapper modelMapper) {

        Converter<Player, Boolean> isRegisteredConverter = new Converter<Player, Boolean>() {
            @Override
            public Boolean convert(MappingContext<Player, Boolean> context) {
                Player source = context.getSource();
                return source instanceof RegisteredPlayer;
            }
        };

        // player

        modelMapper.createTypeMap(Player.class, PlayerResponse.class)
            .addMapping(Player::getId, PlayerResponse::setId)
            .addMapping(Player::getCreatedAt, PlayerResponse::setCreatedAt)
            .addMapping(Player::getUpdatedAt, PlayerResponse::setUpdatedAt)
            .addMapping(Player::getUsername, PlayerResponse::setName)
            .addMappings(mapper -> mapper.using(isRegisteredConverter).map(src -> src, PlayerResponse::setRegistered));

        modelMapper.createTypeMap(RegisteredPlayer.class, PlayerResponse.class)
            .addMapping(Player::getId, PlayerResponse::setId)
            .addMapping(Player::getCreatedAt, PlayerResponse::setCreatedAt)
            .addMapping(Player::getUpdatedAt, PlayerResponse::setUpdatedAt)
            .addMapping(Player::getUsername, PlayerResponse::setName)
            .addMappings(mapper -> mapper.map(src -> true, PlayerResponse::setRegistered));

        modelMapper.createTypeMap(AnonymousPlayer.class, PlayerResponse.class)
            .addMapping(AnonymousPlayer::getId, PlayerResponse::setId)
            .addMapping(AnonymousPlayer::getCreatedAt, PlayerResponse::setCreatedAt)
            .addMapping(AnonymousPlayer::getUpdatedAt, PlayerResponse::setUpdatedAt)
            .addMapping(AnonymousPlayer::getUsername, PlayerResponse::setName)
            .addMappings(mapper -> mapper.map(src -> false, PlayerResponse::setRegistered));

        // short player

        modelMapper.createTypeMap(Player.class, ShortPlayerResponse.class)
            .addMapping(Player::getId, ShortPlayerResponse::setId)
            .addMapping(Player::getUsername, ShortPlayerResponse::setName);

        modelMapper.createTypeMap(RegisteredPlayer.class, ShortPlayerResponse.class)
            .addMapping(Player::getId, ShortPlayerResponse::setId)
            .addMapping(Player::getUsername, ShortPlayerResponse::setName);

        modelMapper.createTypeMap(AnonymousPlayer.class, ShortPlayerResponse.class)
            .addMapping(AnonymousPlayer::getId, ShortPlayerResponse::setId)
            .addMapping(AnonymousPlayer::getUsername, ShortPlayerResponse::setName);

        // role

        modelMapper.createTypeMap(Role.class, RoleResponse.class)
            .addMapping(Role::getLabel, RoleResponse::setName)
            .addMapping(Role::getDescription, RoleResponse::setDescription);

        // score

        modelMapper.createTypeMap(Score.class, ScoreResponse.class)
            .addMapping(Score::getId, ScoreResponse::setId)
            .addMapping(Score::getCreatedAt, ScoreResponse::setCreatedAt)
            .addMapping(Score::getValue, ScoreResponse::setValue)
            .addMapping(Score::getPlayer, ScoreResponse::setPlayer);

        modelMapper.createTypeMap(Score.class, ShortScoreResponse.class)
            .addMapping(Score::getId, ShortScoreResponse::setId)
            .addMapping(Score::getCreatedAt, ShortScoreResponse::setCreatedAt)
            .addMapping(Score::getValue, ShortScoreResponse::setValue);

        // log

        modelMapper.createTypeMap(Log.class, LogResponse.class)
            .addMapping(Log::getId, LogResponse::setId)
            .addMapping(Log::getCreatedAt, LogResponse::setCreatedAt)
            .addMapping(Log::getData, LogResponse::setData)
            .addMapping(Log::getMessage, LogResponse::setMessage)
            .addMapping(Log::getLevel, LogResponse::setLevel)
            .addMapping(Log::getPlayer, LogResponse::setPlayer);

        // game

        modelMapper.createTypeMap(Game.class, GameResponse.class)
            .addMapping(Game::getId, GameResponse::setId)
            .addMapping(Game::getCreatedAt, GameResponse::setCreatedAt)
            .addMapping(Game::getUpdatedAt, GameResponse::setUpdatedAt)
            .addMapping(Game::getSheet, GameResponse::setSheet)
            .addMapping(Game::getDices, GameResponse::setDices)
            .addMapping(Game::getRollCount, GameResponse::setRollCount)
            .addMapping(Game::getAnnouncement, GameResponse::setAnnouncement)
            .addMapping(Game::getStatus, GameResponse::setStatus)
            .addMapping(Game::getTotalSum, GameResponse::setTotalSum);

        modelMapper.createTypeMap(Game.class, ShortGameResponse.class)
            .addMapping(Game::getId, ShortGameResponse::setId)
            .addMapping(Game::getCreatedAt, ShortGameResponse::setCreatedAt)
            .addMapping(Game::getUpdatedAt, ShortGameResponse::setUpdatedAt)
            .addMapping(Game::getTotalSum, ShortGameResponse::setTotalSum)
            .addMapping(Game::getStatus, ShortGameResponse::setStatus);

        // stats

        modelMapper.createTypeMap(GlobalPlayerStats.class, GlobalPlayerStatsResponse.class)
            .addMapping(GlobalPlayerStats::getPlayerCount, GlobalPlayerStatsResponse::setPlayerCount)
            .addMapping(GlobalPlayerStats::getMostScoresByAnyPlayer, GlobalPlayerStatsResponse::setMostScoresByAnyPlayer)
            .addMapping(GlobalPlayerStats::getPlayerWithMostScores, GlobalPlayerStatsResponse::setPlayerWithMostScores)
            .addMapping(GlobalPlayerStats::getHighestAverageScoreByAnyPlayer, GlobalPlayerStatsResponse::setHighestAverageScoreByAnyPlayer)
            .addMapping(GlobalPlayerStats::getPlayerWithHighestAverageScore, GlobalPlayerStatsResponse::setPlayerWithHighestAverageScore)
            .addMapping(GlobalPlayerStats::getHighScore, GlobalPlayerStatsResponse::setHighScore)
            .addMapping(GlobalPlayerStats::getNewestPlayer, GlobalPlayerStatsResponse::setNewestPlayer)
            .addMapping(GlobalPlayerStats::getOldestPlayer, GlobalPlayerStatsResponse::setOldestPlayer);

        modelMapper.createTypeMap(GlobalScoreStats.class, GlobalScoreStatsResponse.class)
            .addMapping(GlobalScoreStats::getScoreCount, GlobalScoreStatsResponse::setScoreCount)
            .addMapping(GlobalScoreStats::getAverageScore, GlobalScoreStatsResponse::setAverageScore)
            .addMapping(GlobalScoreStats::getHighScore, GlobalScoreStatsResponse::setHighScore)
            .addMapping(GlobalScoreStats::getTopToday, GlobalScoreStatsResponse::setTopToday)
            .addMapping(GlobalScoreStats::getTopThisWeek, GlobalScoreStatsResponse::setTopThisWeek)
            .addMapping(GlobalScoreStats::getTopThisMonth, GlobalScoreStatsResponse::setTopThisMonth)
            .addMapping(GlobalScoreStats::getTopThisYear, GlobalScoreStatsResponse::setTopThisYear)
            .addMapping(GlobalScoreStats::getTopAllTime, GlobalScoreStatsResponse::setTopAllTime);

        modelMapper.createTypeMap(PlayerPreferences.class, PlayerPreferencesResponse.class)
            .addMapping(PlayerPreferences::getLanguage, PlayerPreferencesResponse::setLanguage)
            .addMapping(PlayerPreferences::getTheme, PlayerPreferencesResponse::setTheme);

        modelMapper.createTypeMap(PlayerStats.class, PlayerStatsResponse.class)
            .addMapping(PlayerStats::getLastActivity, PlayerStatsResponse::setLastActivity)
            .addMapping(PlayerStats::getAverageScore, PlayerStatsResponse::setAverageScore)
            .addMapping(PlayerStats::getHighScore, PlayerStatsResponse::setHighScore)
            .addMapping(PlayerStats::getScoreCount, PlayerStatsResponse::setScoreCount);

        modelMapper.createTypeMap(PlayerWithToken.class, AuthResponse.class)
            .addMapping(PlayerWithToken::getToken, AuthResponse::setToken)
            .addMapping(PlayerWithToken::getPlayer, AuthResponse::setPlayer);
    }
}
