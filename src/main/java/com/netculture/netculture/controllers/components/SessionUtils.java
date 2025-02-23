package com.netculture.netculture.controllers.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionUtils {
    @Autowired
    private HttpSession session;

    public void removeMsg() {
        session.removeAttribute("msg");
    }
}
