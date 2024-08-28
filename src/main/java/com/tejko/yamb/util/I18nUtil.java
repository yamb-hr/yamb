package com.tejko.yamb.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class I18nUtil {

    private final MessageSource messageSource;

    @Autowired
    public I18nUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String key, Object[] args) {
        try {
            return messageSource.getMessage(key, args, key, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return key;
        }
    }

    public String getMessage(String key) {
        return getMessage(key, null);
    }

}