package com.tejko.yamb.api.payload.requests;

import java.time.LocalDateTime;

public class DateRangeRequest {

    private LocalDateTime from;
    private LocalDateTime to;

    public DateRangeRequest() {}

    public DateRangeRequest(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }

}
