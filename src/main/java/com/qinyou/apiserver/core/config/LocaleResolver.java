package com.qinyou.apiserver.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Slf4j
public class LocaleResolver extends AcceptHeaderLocaleResolver {
    private Locale myLocal;

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        log.debug("Get Request Locale: {}", request.getLocale());
        return myLocal == null ? request.getLocale() : myLocal;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        myLocal = locale;
    }
}
