package com.tejko.yamb.models.payload;

import java.time.LocalDateTime;

public class DateTimeInterval {

    private LocalDateTime from;
    private LocalDateTime to;

    public DateTimeInterval(LocalDateTime from, LocalDateTime to) {
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
