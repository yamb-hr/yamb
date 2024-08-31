package com.tejko.yamb.domain.services.interfaces;

import java.util.List;

import com.tejko.yamb.domain.enums.BoxType;
import com.tejko.yamb.domain.enums.ColumnType;
import com.tejko.yamb.domain.models.Game;

public interface GameService {

    public Game getById(String id);

    public List<Game> getAll();

    public Game getOrCreate(Long playerId);

    public Game rollById(String id, int[] diceToRoll);

    public Game announceById(String id, BoxType boxType);

    public Game fillById(String id, ColumnType columnType, BoxType boxType);

    public Game restartById(String id);

    public Game completeById(String id);

    public Game finishById(String id);

}
