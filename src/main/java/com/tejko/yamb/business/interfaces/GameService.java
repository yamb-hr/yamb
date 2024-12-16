package com.tejko.yamb.business.interfaces;

import com.tejko.yamb.domain.enums.BoxType;
import com.tejko.yamb.domain.enums.ColumnType;
import com.tejko.yamb.domain.models.Game;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameService {

    Game getByExternalId(UUID externalId);

    Page<Game> getAll(Pageable pageable);

    Game getOrCreate(UUID playerExternalId);

    Game rollByExternalId(UUID externalId, int[] diceToRoll);

    Game announceByExternalId(UUID externalId, BoxType boxType);

    Game fillByExternalId(UUID externalId, ColumnType columnType, BoxType boxType);

    Game restartByExternalId(UUID externalId);

    Game completeByExternalId(UUID externalId);

    Game archiveByExternalId(UUID externalId);

    void deleteByExternalId(UUID externalId);

    void deleteAll();

}
