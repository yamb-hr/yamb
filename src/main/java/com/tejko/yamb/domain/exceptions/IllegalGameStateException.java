package com.tejko.yamb.domain.exceptions;

public class IllegalGameStateException extends IllegalStateException {

    private final String messageKey;
    private final Object[] args;

    public IllegalGameStateException(String messageKey, Object... args) {
        super(messageKey);
        this.messageKey = messageKey;
        this.args = args;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object[] getArgs() {
        return args;
    }
    
}
