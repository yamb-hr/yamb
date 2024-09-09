package com.tejko.yamb.exceptions.custom;

public class ResourceLockedException extends RuntimeException {

    private final String messageKey;
    private final Object[] args;

    public ResourceLockedException(String messageKey, Object... args) {
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
