package com.tejko.yamb.business.interfaces;

import java.util.UUID;

import com.tejko.yamb.domain.models.GlobalScoreStats;
import com.tejko.yamb.domain.models.Score;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScoreService {

	Score getByExternalId(UUID externalId);

	Page<Score> getAll(Pageable pageable);

	void deleteByExternalId(UUID externalId);

	GlobalScoreStats getGlobalStats();
    
}
