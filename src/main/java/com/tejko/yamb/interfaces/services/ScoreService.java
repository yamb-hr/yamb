package com.tejko.yamb.interfaces.services;

import java.util.List;

import com.tejko.yamb.api.payload.responses.ScoreResponse;
import com.tejko.yamb.api.payload.responses.ScoreboardResponse;
import com.tejko.yamb.domain.models.Score;

public interface ScoreService {
	
	public Score fetchById(Long id);

	public ScoreResponse getById(Long id);

	public List<ScoreResponse> getAll();

	public void deleteById(Long id);

	public ScoreboardResponse getScoreboard();
    
}
