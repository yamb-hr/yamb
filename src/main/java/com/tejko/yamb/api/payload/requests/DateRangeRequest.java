package com.tejko.yamb.api.payload.requests;

import java.time.LocalDateTime;

public class DateRangeRequest {

    private LocalDateTime from;
    private LocalDateTime to;

    public DateRangeRequest(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return from;
    }
    
    public LocalDateTime getTo() {
        return to;
    }

}
