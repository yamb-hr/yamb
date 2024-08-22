package com.tejko.yamb.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tejko.yamb.api.payload.responses.PlayerStatsResponse;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.domain.repositories.GameRepository;
import com.tejko.yamb.domain.repositories.PlayerRepository;
import com.tejko.yamb.domain.repositories.ScoreRepository;
import com.tejko.yamb.interfaces.services.WebSocketService;
import com.tejko.yamb.services.PlayerServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceImplUnitTest {

    @InjectMocks
    private PlayerServiceImpl playerService;

    @Mock
    private GameRepository gameRepo;

    @Mock
    private PlayerRepository playerRepo;

    @Mock
    private ScoreRepository scoreRepo;

    @Mock
    private WebSocketService webSocketService;

    private UUID externalId;
    private Player player;
    private String username;

    @BeforeEach
    public void setUp() {
        externalId = UUID.randomUUID();
        username = "username";
        player = Player.getInstance(username, "password", false);
    }

    @Test
    public void testGetByExternalId_Success() {
        when(playerRepo.findByExternalId(externalId)).thenReturn(Optional.of(player));

        Player result = playerService.getByExternalId(externalId);

        assertEquals(player, result);
    }

    @Test
    public void testGetByExternalId_NotFound() {
        when(playerRepo.findByExternalId(externalId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            playerService.getByExternalId(externalId);
        });
    }

    @Test
    public void testGetAll_Success() {
        List<Player> players = Arrays.asList(player);
        Page<Player> page = new PageImpl<>(players);
        Pageable pageable = PageRequest.of(0, 10, Direction.ASC, "username");
        when(playerRepo.findAll(pageable)).thenReturn(page);

        List<Player> result = playerService.getAll(0, 10, "username", "ASC");

        assertEquals(players, result);
    }

    @Test
    public void testGetScoresByPlayerId_Success() {
        Score score = Score.getInstance(player, 1000);
        when(playerRepo.findByExternalId(externalId)).thenReturn(Optional.of(player));
        when(scoreRepo.findAllByPlayerIdOrderByCreatedAtDesc(player.getId())).thenReturn(Arrays.asList(score));

        List<Score> result = playerService.getScoresByPlayerId(externalId);

        assertEquals(1, result.size());
        assertEquals(score, result.get(0));
    }

    @Test
    public void testGetScoresByPlayerId_PlayerNotFound() {
        when(playerRepo.findByExternalId(externalId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            playerService.getScoresByPlayerId(externalId);
        });
    }

    @Test
    public void testLoadUserByUsername_Success() {
        when(playerRepo.findByUsername(username)).thenReturn(Optional.of(player));

        Player result = playerService.loadUserByUsername(username);

        assertEquals(player.getUsername(), result.getUsername());
    }

    @Test
    public void testLoadUserByUsername_NotFound() {
        when(playerRepo.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            playerService.loadUserByUsername(username);
        });
    }

    @Test
    public void testDeleteByExternalId_Success() {
        doNothing().when(playerRepo).deleteByExternalId(externalId);

        playerService.deleteByExternalId(externalId);

        verify(playerRepo, times(1)).deleteByExternalId(externalId);
    }

    @Test
    public void testGetPrincipalByExternalId_Success() {
        String principal = "testPrincipal";
        when(webSocketService.getPrincipalByExternalId(externalId)).thenReturn(principal);

        String result = playerService.getPrincipalByExternalId(externalId);

        assertEquals(principal, result);
    }

    @Test
    public void testGetPlayerStats_Success() {
        when(playerRepo.findByExternalId(externalId)).thenReturn(Optional.of(player));
        LocalDateTime lastActivity = LocalDateTime.of(2024, 8, 22, 16, 11, 38);
        when(playerRepo.findLastActivityByPlayerId(player.getId())).thenReturn(lastActivity);
        when(scoreRepo.findAverageValueByPlayerId(player.getId())).thenReturn(1000.0);
        when(scoreRepo.findTopValueByPlayerId(player.getId())).thenReturn(1400);
        when(scoreRepo.countByPlayerId(player.getId())).thenReturn(2000L);

        PlayerStatsResponse result = playerService.getPlayerStats(externalId);

        assertEquals(lastActivity, result.lastActivity);
        assertEquals(1000.0, result.averageScore);
        assertEquals(1400, result.topScore);
        assertEquals(2000L, result.gamesPlayed);
    }

    @Test
    public void testGetPlayerStats_PlayerNotFound() {
        when(playerRepo.findByExternalId(externalId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            playerService.getPlayerStats(externalId);
        });
    }
}
