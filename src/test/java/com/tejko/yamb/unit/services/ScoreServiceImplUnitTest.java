package com.tejko.yamb.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import com.tejko.yamb.api.payload.requests.DateRangeRequest;
import com.tejko.yamb.api.payload.responses.ScoreResponse;
import com.tejko.yamb.api.payload.responses.ScoreboardResponse;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.domain.repositories.ScoreRepository;
import com.tejko.yamb.services.ScoreServiceImpl;
import com.tejko.yamb.util.PayloadMapper;

@ExtendWith(MockitoExtension.class)
public class ScoreServiceImplUnitTest {

    @InjectMocks
    private ScoreServiceImpl scoreService;

    @Mock
    private ScoreRepository scoreRepo;

    @Mock
    private PayloadMapper mapper;

    private UUID externalId;
    private Player player;
    private int value;
    private Score score;

    @BeforeEach
    public void init() {
        externalId = UUID.randomUUID();
        player = Player.getInstance("username", "password", true);
        value = 1000;
        score = Score.getInstance(player, value);
    }

    @Test
    public void testGetByExternalId_Success() {
        when(scoreRepo.findByExternalId(externalId)).thenReturn(Optional.of(score));

        Score result = scoreService.getByExternalId(externalId);

        assertEquals(score, result);
    }

    @Test
    public void testGetByExternalId_NotFound() {
        when(scoreRepo.findByExternalId(externalId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            scoreService.getByExternalId(externalId);
        });
    }

    @Test
    public void testGetAll() {
        List<Score> scores = Arrays.asList(score);
        when(scoreRepo.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(scores));

        List<Score> result = scoreService.getAll(0, 10, "value", "ASC");

        assertEquals(scores, result);
    }

    @Test
    public void testGetByInterval() {
        LocalDateTime from = LocalDateTime.now().minusDays(7);
        LocalDateTime to = LocalDateTime.now();
        DateRangeRequest interval = new DateRangeRequest(from, to);

        List<Score> scores = Arrays.asList(score);

        when(scoreRepo.findByCreatedAtBetween(from, to)).thenReturn(scores);

        List<Score> result = scoreService.getByInterval(interval);

        assertEquals(scores, result);
    }

    @Test
    public void testDeleteByExternalId() {
        scoreService.deleteByExternalId(externalId);

        verify(scoreRepo).deleteByExternalId(externalId);
    }

    @Test
    public void testGetScoreboard() {
        List<Score> scores = Arrays.asList(score);
        when(scoreRepo.findTop30ByCreatedAtBetweenOrderByValueDesc(any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(scores);
        when(scoreRepo.findTop30ByOrderByValueDesc()).thenReturn(scores);
        when(scoreRepo.count()).thenReturn(100L);
        when(scoreRepo.findAverageValue()).thenReturn(1000.0);
        when(mapper.toDTO(any(Score.class))).thenReturn(new ScoreResponse());

        ScoreboardResponse result = scoreService.getScoreboard();

        assertEquals(1, result.topToday.size());
        assertEquals(1, result.topThisWeek.size());
        assertEquals(1, result.topThisMonth.size());
        assertEquals(1, result.topThisYear.size());
        assertEquals(1, result.topAllTime.size());
        assertEquals(100L, result.gamesPlayed);
        assertEquals(1000.0, result.averageScore);
    }
}