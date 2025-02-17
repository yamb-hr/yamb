package com.tejko.yamb.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.tejko.yamb.api.dto.requests.PlayerPreferencesRequest;
import com.tejko.yamb.api.dto.responses.ClashDetailResponse;
import com.tejko.yamb.api.dto.responses.ClashResponse;
import com.tejko.yamb.api.dto.responses.GameDetailResponse;
import com.tejko.yamb.api.dto.responses.GameResponse;
import com.tejko.yamb.api.dto.responses.GlobalPlayerStatsResponse;
import com.tejko.yamb.api.dto.responses.GlobalScoreStatsResponse;
import com.tejko.yamb.api.dto.responses.ImageResponse;
import com.tejko.yamb.api.dto.responses.LogDetailResponse;
import com.tejko.yamb.api.dto.responses.LogResponse;
import com.tejko.yamb.api.dto.responses.NotificationResponse;
import com.tejko.yamb.api.dto.responses.PlayerDetailResponse;
import com.tejko.yamb.api.dto.responses.PlayerPreferencesResponse;
import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.api.dto.responses.PlayerStatsResponse;
import com.tejko.yamb.api.dto.responses.RelationshipResponse;
import com.tejko.yamb.api.dto.responses.ScoreResponse;
import com.tejko.yamb.api.dto.responses.TicketResponse;
import com.tejko.yamb.domain.models.Clash;
import com.tejko.yamb.domain.models.Clash.ClashPlayer;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.domain.models.GlobalPlayerStats;
import com.tejko.yamb.domain.models.GlobalScoreStats;
import com.tejko.yamb.domain.models.Image;
import com.tejko.yamb.domain.models.Log;
import com.tejko.yamb.domain.models.Notification;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.PlayerPreferences;
import com.tejko.yamb.domain.models.PlayerRelationship;
import com.tejko.yamb.domain.models.PlayerStats;
import com.tejko.yamb.domain.models.Role;
import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.domain.models.Ticket;

@Configuration
public class ModelMapperConfig {

    @Bean
    @Primary
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(AccessLevel.PRIVATE)
            .setAmbiguityIgnored(true)
            .setSkipNullEnabled(true)
            .setImplicitMappingEnabled(false);
        configureMappings(modelMapper);
        return modelMapper;
    }

    private void configureMappings(ModelMapper modelMapper) {
        
        // player
        modelMapper.createTypeMap(Player.class, PlayerResponse.class)
            .addMapping(Player::getExternalId, PlayerResponse::setId)
            .addMapping(Player::getCreatedAt, PlayerResponse::setCreatedAt)
            .addMapping(Player::getUpdatedAt, PlayerResponse::setUpdatedAt)
            .addMapping(Player::getUsername, PlayerResponse::setName)
            .addMapping(Player::getAvatar, PlayerResponse::setAvatar);

        modelMapper.createTypeMap(Player.class, PlayerDetailResponse.class)
            .addMapping(Player::getExternalId, PlayerDetailResponse::setId)
            .addMapping(Player::getCreatedAt, PlayerDetailResponse::setCreatedAt)
            .addMapping(Player::getUpdatedAt, PlayerDetailResponse::setUpdatedAt)
            .addMapping(Player::getUsername, PlayerDetailResponse::setName)
            .addMapping(Player::getAvatar, PlayerDetailResponse::setAvatar)
            .addMapping(Player::getEmail, PlayerDetailResponse::setEmail)
            .addMapping(Player::isEmailVerified, PlayerDetailResponse::setEmailVerified)
            .addMapping(Player::isAdmin, PlayerDetailResponse::setAdmin)
            .addMapping(Player::isGuest, PlayerDetailResponse::setGuest);

        // image
        modelMapper.createTypeMap(Image.class, ImageResponse.class)
            .addMapping(Image::getExternalId, ImageResponse::setId)
            .addMapping(Image::getName, ImageResponse::setName)
            .addMapping(Image::getUrl, ImageResponse::setUrl);
        
        // role
        modelMapper.createTypeMap(Role.class, String.class)
            .setConverter(ctx -> ctx.getSource().getLabel());

        // score
        modelMapper.createTypeMap(Score.class, ScoreResponse.class)
            .addMapping(Score::getExternalId, ScoreResponse::setId)
            .addMapping(Score::getCreatedAt, ScoreResponse::setCreatedAt)
            .addMapping(Score::getValue, ScoreResponse::setValue)
            .addMapping(Score::getPlayer, ScoreResponse::setPlayer);

        // log
        modelMapper.createTypeMap(Log.class, LogResponse.class)
            .addMapping(Log::getExternalId, LogResponse::setId)
            .addMapping(Log::getCreatedAt, LogResponse::setCreatedAt)
            .addMapping(Log::getMessage, LogResponse::setMessage)
            .addMapping(Log::getLevel, LogResponse::setLevel)
            .addMapping(Log::getPlayer, LogResponse::setPlayer);

        modelMapper.createTypeMap(Log.class, LogDetailResponse.class)
            .addMapping(Log::getExternalId, LogDetailResponse::setId)
            .addMapping(Log::getCreatedAt, LogDetailResponse::setCreatedAt)
            .addMapping(Log::getData, LogDetailResponse::setData)
            .addMapping(Log::getMessage, LogDetailResponse::setMessage)
            .addMapping(Log::getLevel, LogDetailResponse::setLevel)
            .addMapping(Log::getPlayer, LogDetailResponse::setPlayer);

        //game
        modelMapper.createTypeMap(Game.class, GameResponse.class)
            .addMapping(Game::getExternalId, GameResponse::setId)
            .addMapping(Game::getCreatedAt, GameResponse::setCreatedAt)
            .addMapping(Game::getUpdatedAt, GameResponse::setUpdatedAt)
            .addMapping(Game::getStatus, GameResponse::setStatus)
            .addMapping(Game::getTotalSum, GameResponse::setTotalSum)
            .addMapping(Game::getType, GameResponse::setType)
            .addMapping(Game::getProgress, GameResponse::setProgress);

        modelMapper.createTypeMap(Game.class, GameDetailResponse.class)
            .addMapping(Game::getExternalId, GameDetailResponse::setId)
            .addMapping(Game::getCreatedAt, GameDetailResponse::setCreatedAt)
            .addMapping(Game::getUpdatedAt, GameDetailResponse::setUpdatedAt)
            .addMapping(Game::getSheet, GameDetailResponse::setSheet)
            .addMapping(Game::getDices, GameDetailResponse::setDices)
            .addMapping(Game::getRollCount, GameDetailResponse::setRollCount)
            .addMapping(Game::getAnnouncement, GameDetailResponse::setAnnouncement)
            .addMapping(Game::getStatus, GameDetailResponse::setStatus)
            .addMapping(Game::getTotalSum, GameDetailResponse::setTotalSum)
            .addMapping(Game::getLatestDiceRolled, GameDetailResponse::setLatestDiceRolled)
            .addMapping(Game::getLastAction, GameDetailResponse::setLastAction)
            .addMapping(Game::getPreviousRollCount, GameDetailResponse::setPreviousRollCount)
            .addMapping(Game::getLatestColumnFilled, GameDetailResponse::setLatestColumnFilled)
            .addMapping(Game::getLatestBoxFilled, GameDetailResponse::setLatestBoxFilled)
            .addMapping(Game::getType, GameDetailResponse::setType)
            .addMapping(Game::getProgress, GameDetailResponse::setProgress);

        modelMapper.createTypeMap(Game.Sheet.class, GameDetailResponse.Sheet.class)
            .addMapping(Game.Sheet::getColumns, GameDetailResponse.Sheet::setColumns);

        modelMapper.createTypeMap(Game.Column.class, GameDetailResponse.Column.class)
            .addMapping(Game.Column::getType, GameDetailResponse.Column::setType)
            .addMapping(Game.Column::getBoxes, GameDetailResponse.Column::setBoxes);

        modelMapper.createTypeMap(Game.Box.class, GameDetailResponse.Box.class)
            .addMapping(Game.Box::getType, GameDetailResponse.Box::setType)
            .addMapping(Game.Box::getValue, GameDetailResponse.Box::setValue);

        modelMapper.createTypeMap(Game.Dice.class, GameDetailResponse.Dice.class)
            .addMapping(Game.Dice::getIndex, GameDetailResponse.Dice::setIndex)   
            .addMapping(Game.Dice::getValue, GameDetailResponse.Dice::setValue);
                
        // clash
        modelMapper.createTypeMap(Clash.class, ClashDetailResponse.class)
            .addMapping(Clash::getExternalId, ClashDetailResponse::setId)
            .addMapping(Clash::getCreatedAt, ClashDetailResponse::setCreatedAt)
            .addMapping(Clash::getUpdatedAt, ClashDetailResponse::setUpdatedAt)
            .addMapping(Clash::getName, ClashDetailResponse::setName)
            .addMapping(Clash::getType, ClashDetailResponse::setType)
            .addMapping(Clash::getStatus, ClashDetailResponse::setStatus)
            .addMapping(Clash::getTurn, ClashDetailResponse::setTurn);

        modelMapper.createTypeMap(Clash.class, ClashResponse.class)
            .addMapping(Clash::getExternalId, ClashResponse::setId)
            .addMapping(Clash::getCreatedAt, ClashResponse::setCreatedAt)
            .addMapping(Clash::getUpdatedAt, ClashResponse::setUpdatedAt)
            .addMapping(Clash::getName, ClashResponse::setName)
            .addMapping(Clash::getType, ClashResponse::setType)
            .addMapping(Clash::getStatus, ClashResponse::setStatus);

        modelMapper.createTypeMap(ClashPlayer.class, ClashDetailResponse.ClashPlayer.class)
            .addMapping(ClashPlayer::getId, ClashDetailResponse.ClashPlayer::setId)
            .addMapping(ClashPlayer::getGameId, ClashDetailResponse.ClashPlayer::setGameId)
            .addMapping(ClashPlayer::getStatus, ClashDetailResponse.ClashPlayer::setStatus)
            .addMapping(ClashPlayer::getScore, ClashDetailResponse.ClashPlayer::setScore);

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

        modelMapper.createTypeMap(PlayerPreferencesRequest.class, PlayerPreferences.class)
            .addMapping(PlayerPreferencesRequest::getLanguage, PlayerPreferences::setLanguage)
            .addMapping(PlayerPreferencesRequest::getTheme, PlayerPreferences::setTheme);

        modelMapper.createTypeMap(PlayerStats.class, PlayerStatsResponse.class)
            .addMapping(PlayerStats::getLastActivity, PlayerStatsResponse::setLastActivity)
            .addMapping(PlayerStats::getAverageScore, PlayerStatsResponse::setAverageScore)
            .addMapping(PlayerStats::getHighScore, PlayerStatsResponse::setHighScore)
            .addMapping(PlayerStats::getScoreCount, PlayerStatsResponse::setScoreCount);

        // ticket
        modelMapper.createTypeMap(Ticket.class, TicketResponse.class)
            .addMapping(Ticket::getExternalId, TicketResponse::setId)
            .addMapping(Ticket::getCreatedAt, TicketResponse::setCreatedAt)
            .addMapping(Ticket::getUpdatedAt, TicketResponse::setUpdatedAt)
            .addMapping(Ticket::getTitle, TicketResponse::setTitle)
            .addMapping(Ticket::getDescription, TicketResponse::setDescription)
            .addMapping(Ticket::getStatus, TicketResponse::setStatus)
            .addMapping(Ticket::getEmailAddress, TicketResponse::setEmailAddresses)
            .addMappings(mapper -> mapper.map(Ticket::getPlayer, TicketResponse::setPlayer));

        // notification
        modelMapper.createTypeMap(Notification.class, NotificationResponse.class)
            .addMapping(Notification::getExternalId, NotificationResponse::setId)
            .addMapping(Notification::getCreatedAt, NotificationResponse::setCreatedAt)
            .addMapping(Notification::getContent, NotificationResponse::setContent)
            .addMapping(Notification::getLink, NotificationResponse::setLink)
            .addMapping(Notification::getType, NotificationResponse::setType);

        // relationship
        modelMapper.createTypeMap(PlayerRelationship.class, RelationshipResponse.class)
            .addMapping(PlayerRelationship::getExternalId, RelationshipResponse::setId)
            .addMapping(PlayerRelationship::getType, RelationshipResponse::setType)
            .addMapping(PlayerRelationship::isActive, RelationshipResponse::setActive);

    }

}
