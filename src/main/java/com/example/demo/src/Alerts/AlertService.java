package com.example.demo.src.Alerts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlertService {

    private final AlertDao alertDao;
    private final AlertProvider alertProvider;

    @Autowired
    public AlertService(AlertDao alertDao, AlertProvider alertProvider) {
        this.alertDao = alertDao;
        this.alertProvider = alertProvider;
    }
}
