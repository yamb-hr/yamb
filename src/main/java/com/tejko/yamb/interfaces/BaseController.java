package com.tejko.yamb.interfaces;

import java.util.List;
import java.util.UUID;

import com.tejko.yamb.api.payload.responses.BaseResponse;

public interface BaseController<T extends BaseResponse> {

	public List<T> getAll(Integer page, Integer size, String sort, String direction);

	public T getByExternalId(UUID externalId);

	public void deleteByExternalId(UUID externalId);
    
}
