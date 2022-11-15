package com.example.demo.src.Alerts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostCreateAlertReq {
    private String alertText;
    private int alertType;
    private int receiveUserIdx;
    private int folderIdx;
    private int linkIdx;
}
