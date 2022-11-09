package com.example.demo.src.Alerts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetAlertRes {
    private int alertIdx;
    private String alertText;
    private int alertType;
    private int sendUserIdx;
    private int receiveUserIdx;
    private int folderIdx;

}
