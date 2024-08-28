package com.tejko.yamb.exceptions.custom;

public class GameStateException extends IllegalStateException {

    private final String messageKey;
    private final Object[] args;

    public GameStateException(String messageKey, Object... args) {
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
