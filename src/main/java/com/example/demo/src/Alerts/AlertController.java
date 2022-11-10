package com.example.demo.src.Alerts;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Alerts.model.GetAlertRes;
import com.example.demo.src.Alerts.model.PostCreateAlertReq;
import com.example.demo.src.Alerts.model.PostCreateAlertRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

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

    /**
     * 알림 삭제 API
     * @param alertIdx
     * @return BaseResponse<String>
     */
    @DeleteMapping("/delete/{alertIdx}")
    public BaseResponse<String> deleteAlert(@PathVariable int alertIdx){
        try{
            String result = alertService.deleteAlert(alertIdx);
            return new BaseResponse<>(result);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    @PostMapping("/create")
    public BaseResponse<PostCreateAlertRes> createAlert(@RequestBody PostCreateAlertReq postCreateAlertReq){
        try{
            if(postCreateAlertReq.getAlertText() == null || postCreateAlertReq.getAlertText().length() == 0){
                return new BaseResponse<>(ALERTS_EMPTY_ALERT_TEXT);
            }

            if(postCreateAlertReq.getReceiveUserIdx() == 0){
                return new BaseResponse<>(ALERTS_EMPTY_RECEIVE_USERIDX);
            }

            if(postCreateAlertReq.getAlertType() == 1 && postCreateAlertReq.getFolderIdx() == 0){
                return new BaseResponse<>(ALERTS_EMPTY_FOLDERIDX);
            }

            if(postCreateAlertReq.getAlertType() == 2 && postCreateAlertReq.getLinkIdx() == 0){
                return new BaseResponse<>(ALERTS_EMPTY_LINKIDX);
            }

            PostCreateAlertRes postCreateAlertRes = alertService.createAlert(postCreateAlertReq);
            return new BaseResponse<>(postCreateAlertRes);

        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }







}
