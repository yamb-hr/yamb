package com.tejko.yamb.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tejko.yamb.api.payload.requests.ActionRequest;
import com.tejko.yamb.domain.enums.BoxType;
import com.tejko.yamb.domain.enums.ColumnType;
import com.tejko.yamb.domain.enums.GameStatus;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.repositories.GameRepository;
import com.tejko.yamb.domain.repositories.PlayerRepository;
import com.tejko.yamb.domain.repositories.ScoreRepository;
import com.tejko.yamb.services.GameServiceImpl;
import com.tejko.yamb.util.YambLogger;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplUnitTest {

    @InjectMocks
    private GameServiceImpl gameService;

    @Mock
    private GameRepository gameRepo;

    @Mock
    private PlayerRepository playerRepo;

    @Mock
    private ScoreRepository scoreRepo;

    @Mock
    private YambLogger logger;

    private UUID externalId;
    private Player player;
    private String username;
    private Game game;

    @BeforeEach
    public void init() {
        externalId = UUID.randomUUID();
        username = "username";
        player = Player.getInstance(username, "password", false);
        game = Game.getInstance(player);
    }

    @Test
    public void testGetByExternalId_Success() {
        when(gameRepo.findByExternalId(externalId)).thenReturn(Optional.of(game));

        Game result = gameService.getByExternalId(externalId);

        assertEquals(game, result);
    }

    @Test
    public void testGetByExternalId_NotFound() {
        when(gameRepo.findByExternalId(externalId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            gameService.getByExternalId(externalId);
        });
    }

    @Test
    public void testGetAll_Success() {
        List<Game> games = Arrays.asList(game);
        when(gameRepo.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(games));

        List<Game> result = gameService.getAll(0, 10, "status", "ASC");

        assertEquals(games, result);
    }

    @Test
    public void testPlay_ExistingGame() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(player, null));
        SecurityContextHolder.setContext(securityContext);

        when(playerRepo.findByUsername(username)).thenReturn(Optional.of(player));
        when(gameRepo.findByPlayerIdAndStatus(player.getId(), GameStatus.IN_PROGRESS)).thenReturn(Optional.of(game));

        Game result = gameService.play();

        assertEquals(game, result);
    }

    @Test
    public void testPlay_NewGame() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(player, null));
        SecurityContextHolder.setContext(securityContext);

        when(playerRepo.findByUsername("username")).thenReturn(Optional.of(player));
        when(gameRepo.findByPlayerIdAndStatus(player.getId(), GameStatus.IN_PROGRESS)).thenReturn(Optional.empty());
        when(gameRepo.save(any(Game.class))).thenReturn(game);

        Game result = gameService.play();

        assertEquals(game, result);
    }

    @Test
    public void testRollByExternalId_Success() {
        int[] diceToRoll = {1, 2, 3, 4, 5};
        ActionRequest actionRequest = new ActionRequest(diceToRoll, null, null);
        when(gameRepo.findByExternalId(externalId)).thenReturn(Optional.of(game));

        Game result = gameService.rollByExternalId(externalId, actionRequest);

        assertEquals(1, result.getRollCount());
    }

    @Test
    public void testAnnounceByExternalId_Success() {
        ActionRequest actionRequest = new ActionRequest(null, null, BoxType.ONES);
        int[] diceToRoll = {1, 2, 3, 4, 5};
        game.roll(diceToRoll);
        when(gameRepo.findByExternalId(externalId)).thenReturn(Optional.of(game));

        Game result = gameService.announceByExternalId(externalId, actionRequest);

        assertEquals(BoxType.ONES, result.getAnnouncement());
    }

    @Test
    public void testfillByExternalId_Success() {
        ActionRequest actionRequest = new ActionRequest(null, ColumnType.DOWNWARDS, BoxType.ONES);
        int[] diceToRoll = {1, 2, 3, 4, 5};
        game.roll(diceToRoll);
        when(gameRepo.findByExternalId(externalId)).thenReturn(Optional.of(game));

        Game result = gameService.fillByExternalId(externalId, actionRequest);
   
        assertNotNull(result.getSheet().getColumns().get(ColumnType.DOWNWARDS.ordinal()).getBoxes().get(BoxType.ONES.ordinal()).getValue());
    }

    @Test
    public void testRestartByExternalId_Success() {
        when(gameRepo.findByExternalId(externalId)).thenReturn(Optional.of(game));

        Game result = gameService.restartByExternalId(externalId);
    
        assertEquals(0, result.getRollCount());
    }

    @Test
    public void testDeleteByExternalId_Success() {
        gameService.deleteByExternalId(externalId);

        verify(gameRepo).deleteByExternalId(externalId);
    }
}
