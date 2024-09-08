package com.tejko.yamb.business.interfaces;

import java.util.List;

import com.tejko.yamb.domain.models.GlobalScoreStats;
import com.tejko.yamb.domain.models.entities.Score;

public interface ScoreService {

	public Score getById(Long id);

	public List<Score> getAll();

	public void deleteById(Long id);

	public GlobalScoreStats getGlobalStats();
    
}
