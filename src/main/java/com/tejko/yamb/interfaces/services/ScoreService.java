package com.tejko.yamb.interfaces.services;

import java.util.List;

import com.tejko.yamb.api.payload.requests.DateRangeRequest;
import com.tejko.yamb.api.payload.responses.ScoreboardResponse;
import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.interfaces.BaseService;

public interface ScoreService extends BaseService<Score> {

    public List<Score> getByInterval(DateRangeRequest interval);

	public ScoreboardResponse getScoreboard();
    
}
