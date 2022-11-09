package com.example.demo.src.Alerts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    private final AlertProvider alertProvider;
    private final AlertService alertService;

    @Autowired
    public AlertController(AlertProvider alertProvider, AlertService alertService) {
        this.alertProvider = alertProvider;
        this.alertService = alertService;
    }






}
