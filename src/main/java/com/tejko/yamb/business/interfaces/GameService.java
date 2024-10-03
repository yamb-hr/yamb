package com.tejko.yamb.business.interfaces;

import com.tejko.yamb.domain.enums.BoxType;
import com.tejko.yamb.domain.enums.ColumnType;
import com.tejko.yamb.domain.models.Game;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameService {

    public Game getByExternalId(UUID externalId);

    public Page<Game> getAll(Pageable pageable);

    public Game getOrCreate(UUID playerExternalId);

    public Game rollByExternalId(UUID externalId, int[] diceToRoll);

    public Game announceByExternalId(UUID externalId, BoxType boxType);

    public Game fillByExternalId(UUID externalId, ColumnType columnType, BoxType boxType);

    public Game restartByExternalId(UUID externalId);

    public Game completeByExternalId(UUID externalId);

    public Game archiveByExternalId(UUID externalId);

    public void deleteByExternalId(UUID externalId);

}
