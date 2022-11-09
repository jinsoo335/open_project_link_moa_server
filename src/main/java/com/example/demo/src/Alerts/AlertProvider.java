package com.example.demo.src.Alerts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlertProvider {

    private final AlertDao alertDao;

    @Autowired
    public AlertProvider(AlertDao alertDao) {
        this.alertDao = alertDao;
    }
}
