package com.tejko.yamb.domain.services.interfaces;

import java.util.List;

import com.tejko.yamb.api.dto.responses.ScoreResponse;
import com.tejko.yamb.api.dto.responses.GlobalScoreStats;
import com.tejko.yamb.domain.models.Score;

public interface ScoreService {
	
	public Score fetchById(Long id);

	public ScoreResponse getById(Long id);

	public List<ScoreResponse> getAll();

	public void deleteById(Long id);

	public GlobalScoreStats getGlobalStats();
    
}
