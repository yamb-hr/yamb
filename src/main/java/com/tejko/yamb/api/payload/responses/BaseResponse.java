package com.tejko.yamb.api.payload.responses;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class BaseResponse {

    public UUID id;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

}