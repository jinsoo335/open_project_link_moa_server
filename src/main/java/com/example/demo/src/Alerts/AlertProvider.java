package com.example.demo.src.Alerts;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.security.JwtTool;
import com.example.demo.src.Alerts.model.GetAlertRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class AlertProvider {

    private final AlertDao alertDao;

    @Autowired
    public AlertProvider(AlertDao alertDao) {
        this.alertDao = alertDao;
    }

    public List<GetAlertRes> getAlert() throws BaseException {
        int userIdx = JwtTool.getUserIdx();

        try{
            List<GetAlertRes> getAlertResList = alertDao.getAlert(userIdx);
            return getAlertResList;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
