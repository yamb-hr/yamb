package com.tejko.yamb.config;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.tejko.yamb.api.dto.requests.PlayerPreferencesRequest;
import com.tejko.yamb.api.dto.requests.TicketRequest;
import com.tejko.yamb.api.dto.responses.AuthResponse;
import com.tejko.yamb.api.dto.responses.ClashPlayerResponse;
import com.tejko.yamb.api.dto.responses.ClashResponse;
import com.tejko.yamb.api.dto.responses.GameResponse;
import com.tejko.yamb.api.dto.responses.GlobalPlayerStatsResponse;
import com.tejko.yamb.api.dto.responses.GlobalScoreStatsResponse;
import com.tejko.yamb.api.dto.responses.ImageResponse;
import com.tejko.yamb.api.dto.responses.LogResponse;
import com.tejko.yamb.api.dto.responses.NotificationResponse;
import com.tejko.yamb.api.dto.responses.PlayerPreferencesResponse;
import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.api.dto.responses.PlayerStatsResponse;
import com.tejko.yamb.api.dto.responses.ScoreResponse;
import com.tejko.yamb.api.dto.responses.TicketResponse;
import com.tejko.yamb.business.interfaces.PlayerService;
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
import com.tejko.yamb.domain.models.PlayerStats;
import com.tejko.yamb.domain.models.PlayerWithToken;
import com.tejko.yamb.domain.models.Role;
import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.domain.models.Ticket;

@Configuration
public class ModelMapperConfig {

    private final PlayerService playerService;

    @Autowired
    public ModelMapperConfig(PlayerService playerService) {
        this.playerService = playerService;
    }

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

        Converter<Player, Boolean> isAdminConverter = ctx -> {
            Player player = ctx.getSource();
            if (player.getRoles() == null) {
                return false;
            }
            return player.getRoles().stream().anyMatch(role -> "ADMIN".equals(role.getLabel()));
        };
        
        // player
        modelMapper.createTypeMap(Player.class, PlayerResponse.class)
            .addMapping(Player::getExternalId, PlayerResponse::setId)
            .addMapping(Player::getCreatedAt, PlayerResponse::setCreatedAt)
            .addMapping(Player::getUpdatedAt, PlayerResponse::setUpdatedAt)
            .addMapping(Player::getUsername, PlayerResponse::setName)
            .addMapping(Player::getAvatar, PlayerResponse::setAvatar)
            .addMappings(mapper -> mapper.using(isAdminConverter).map(src -> src, PlayerResponse::setAdmin));

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
            .addMapping(Log::getData, LogResponse::setData)
            .addMapping(Log::getMessage, LogResponse::setMessage)
            .addMapping(Log::getLevel, LogResponse::setLevel)
            .addMapping(Log::getPlayer, LogResponse::setPlayer);

        // game
        Converter<UUID, PlayerResponse> playerConverter = context -> {
            UUID playerExternalId = context.getSource();
            Optional<Player> player = playerService.findByExternalId(playerExternalId);
            return player.isPresent() ? modelMapper.map(player.get(), PlayerResponse.class) : null;
        };

        modelMapper.createTypeMap(Game.class, GameResponse.class)
            .addMapping(Game::getExternalId, GameResponse::setId)
            .addMapping(Game::getCreatedAt, GameResponse::setCreatedAt)
            .addMapping(Game::getUpdatedAt, GameResponse::setUpdatedAt)
            .addMapping(Game::getSheet, GameResponse::setSheet)
            .addMapping(Game::getDices, GameResponse::setDices)
            .addMapping(Game::getRollCount, GameResponse::setRollCount)
            .addMapping(Game::getAnnouncement, GameResponse::setAnnouncement)
            .addMapping(Game::getStatus, GameResponse::setStatus)
            .addMapping(Game::getTotalSum, GameResponse::setTotalSum)
            .addMapping(Game::getLatestDiceRolled, GameResponse::setLatestDiceRolled)
            .addMapping(Game::getPreviousRollCount, GameResponse::setPreviousRollCount)
            .addMapping(Game::getLatestColumnFilled, GameResponse::setLatestColumnFilled)
            .addMapping(Game::getLatestBoxFilled, GameResponse::setLatestBoxFilled)
            .addMappings(mapper -> mapper.using(playerConverter).map(Game::getPlayerId, GameResponse::setPlayer));

        modelMapper.createTypeMap(Game.Sheet.class, GameResponse.Sheet.class)
            .addMapping(Game.Sheet::getColumns, GameResponse.Sheet::setColumns);

        modelMapper.createTypeMap(Game.Column.class, GameResponse.Column.class)
            .addMapping(Game.Column::getType, GameResponse.Column::setType)
            .addMapping(Game.Column::getBoxes, GameResponse.Column::setBoxes);

        modelMapper.createTypeMap(Game.Box.class, GameResponse.Box.class)
            .addMapping(Game.Box::getType, GameResponse.Box::setType)
            .addMapping(Game.Box::getValue, GameResponse.Box::setValue);

        modelMapper.createTypeMap(Game.Dice.class, GameResponse.Dice.class)
            .addMapping(Game.Dice::getIndex, GameResponse.Dice::setIndex)   
            .addMapping(Game.Dice::getValue, GameResponse.Dice::setValue);
                
        // clash
        modelMapper.createTypeMap(Clash.class, ClashResponse.class)
            .addMapping(Clash::getExternalId, ClashResponse::setId)
            .addMapping(Clash::getCreatedAt, ClashResponse::setCreatedAt)
            .addMapping(Clash::getUpdatedAt, ClashResponse::setUpdatedAt)
            .addMapping(Clash::getName, ClashResponse::setName)
            .addMapping(Clash::getType, ClashResponse::setType)
            .addMapping(Clash::getStatus, ClashResponse::setStatus)
            .addMapping(Clash::getPlayers, ClashResponse::setPlayers)
            .addMapping(Clash::getTurn, ClashResponse::setTurn)
            .addMappings(mapper -> mapper.using(playerConverter).map(Clash::getOwnerId, ClashResponse::setOwner))
            .addMappings(mapper -> mapper.using(playerConverter).map(Clash::getWinnerId, ClashResponse::setWinner));

        Converter<UUID, String> playerNameConverter = context -> {
            UUID playerId = context.getSource();
            if (playerId == null) {
                return null;
            }
            Optional<Player> player = playerService.findByExternalId(playerId);
            return player.map(Player::getUsername).orElse(null);
        };

        modelMapper.createTypeMap(ClashPlayer.class, ClashPlayerResponse.class)
            .addMapping(ClashPlayer::getId, ClashPlayerResponse::setId)
            .addMapping(ClashPlayer::getGameId, ClashPlayerResponse::setGameId)
            .addMapping(ClashPlayer::getStatus, ClashPlayerResponse::setStatus)
            .addMappings(mapper -> mapper.using(playerNameConverter).map(ClashPlayer::getId, ClashPlayerResponse::setName));

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

        // auth
        modelMapper.createTypeMap(PlayerWithToken.class, AuthResponse.class)
            .addMapping(PlayerWithToken::getToken, AuthResponse::setToken)
            .addMapping(PlayerWithToken::getPlayer, AuthResponse::setPlayer);

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

        modelMapper.createTypeMap(TicketRequest.class, Ticket.class)
            .addMappings(mapper -> {
                mapper.using((Converter<UUID, Player>) context -> {
                    UUID playerId = context.getSource();
                    if (playerId == null) {
                        return null;
                    }
                    return playerService.findByExternalId(playerId).orElse(null);
                }).map(TicketRequest::getPlayerId, Ticket::setPlayer);

                mapper.using((Converter<Set<String>, Set<String>>) context -> {
                    Set<String> emails = context.getSource();
                    if (emails == null) {
                        return null;
                    }
                    return Set.copyOf(emails);
                }).map(TicketRequest::getEmailAddresses, Ticket::setEmailAddresses);
            })
            .addMapping(TicketRequest::getTitle, Ticket::setTitle)
            .addMapping(TicketRequest::getDescription, Ticket::setDescription);

        // notification
        modelMapper.createTypeMap(Notification.class, NotificationResponse.class)
            .addMapping(Notification::getExternalId, NotificationResponse::setId)
            .addMapping(Notification::getCreatedAt, NotificationResponse::setCreatedAt)
            .addMapping(Notification::getContent, NotificationResponse::setContent)
            .addMapping(Notification::getLink, NotificationResponse::setLink)
            .addMapping(Notification::getType, NotificationResponse::setType);

    }

}
