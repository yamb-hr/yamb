package com.tejko.yamb.business.interfaces;

import com.tejko.yamb.domain.enums.BoxType;
import com.tejko.yamb.domain.enums.ColumnType;
import com.tejko.yamb.domain.models.Game;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameService {

    public Game getById(String id);

    public Page<Game> getAll(Pageable pageable);

    public Game getOrCreate();

    public Game rollById(String id, int[] diceToRoll);

    public Game announceById(String id, BoxType boxType);

    public Game fillById(String id, ColumnType columnType, BoxType boxType);

    public Game restartById(String id);

    public Game completeById(String id);

    public Game archiveById(String id);

}
