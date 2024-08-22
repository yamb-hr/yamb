package com.tejko.yamb.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

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

import com.tejko.yamb.domain.enums.LogLevel;
import com.tejko.yamb.domain.models.Log;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.repositories.LogRepository;
import com.tejko.yamb.services.LogServiceImpl;

@ExtendWith(MockitoExtension.class)
public class LogServiceImplUnitTest {

    @Mock
    private LogRepository logRepo;

    @InjectMocks
    private LogServiceImpl logService;

    private UUID externalId;
    private Player player;
    private Log log;

    @BeforeEach
    public void setUp() {
        externalId = UUID.randomUUID();
        player = Player.getInstance("username", "password", true);
        log = Log.getInstance(player, "message", LogLevel.INFO, "data");
    }

    @Test
    public void testGetByExternalId_Success() {
        when(logRepo.findByExternalId(externalId)).thenReturn(Optional.of(log));

        Log result = logService.getByExternalId(externalId);

        assertEquals(log, result);
    }

    @Test
    public void testGetByExternalId_NotFound() {
        when(logRepo.findByExternalId(externalId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            logService.getByExternalId(externalId);
        });
    }

    @Test
    public void testGetAll_Success() {
        List<Log> logs = Arrays.asList(log);
        Page<Log> page = new PageImpl<>(logs);
        Pageable pageable = PageRequest.of(0, 10, Direction.ASC, "createdAt");
        when(logRepo.findAll(pageable)).thenReturn(page);

        List<Log> result = logService.getAll(0, 10, "createdAt", "ASC");

        assertEquals(logs, result);
    }

    @Test
    public void testCreate_Success() {
        when(logRepo.save(log)).thenReturn(log);

        Log result = logService.create(log);

        assertEquals(log, result);
        verify(logRepo, times(1)).save(log);
    }

    @Test
    public void testDeleteAll_Success() {
        doNothing().when(logRepo).deleteAll();

        logService.deleteAll();

        verify(logRepo, times(1)).deleteAll();
    }

    @Test
    public void testDeleteByExternalId_Success() {
        doNothing().when(logRepo).deleteByExternalId(externalId);

        logService.deleteByExternalId(externalId);

        verify(logRepo, times(1)).deleteByExternalId(externalId);
    }

}
