package com.example.demo.src.Alerts;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Alerts.model.GetAlertRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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


    /**
     * 사용자의 알림 목록을 가져오는 api
     * @return BaseResponse<List<GetAlertRes>>
     */
    @GetMapping("")
    public BaseResponse<List<GetAlertRes>> getAlert(){
        try{
            List<GetAlertRes> getAlertResList = alertProvider.getAlert();
            return new BaseResponse<>(getAlertResList);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }






}
