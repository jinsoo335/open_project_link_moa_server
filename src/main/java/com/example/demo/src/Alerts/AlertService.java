package com.example.demo.src.Alerts;

import com.example.demo.config.BaseException;
import com.example.demo.security.JwtTool;
import com.example.demo.src.Alerts.model.PostCreateAlertReq;
import com.example.demo.src.Alerts.model.PostCreateAlertRes;
import com.example.demo.src.Folders.FolderProvider;
import com.example.demo.src.Links.LinkProvider;
import com.example.demo.src.Users.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class AlertService {

    private final AlertDao alertDao;
    private final AlertProvider alertProvider;

    private final LinkProvider linkProvider;
    private final FolderProvider folderProvider;
    private final UserProvider userProvider;

    @Autowired
    public AlertService(AlertDao alertDao, AlertProvider alertProvider, LinkProvider linkProvider, FolderProvider folderProvider, UserProvider userProvider) {
        this.alertDao = alertDao;
        this.alertProvider = alertProvider;
        this.linkProvider = linkProvider;
        this.folderProvider = folderProvider;
        this.userProvider = userProvider;
    }

    public String deleteAlert(int alertIdx) throws BaseException{
        int userIdx = JwtTool.getUserIdx();

        if(alertProvider.checkExistAlert(userIdx, alertIdx) == 0){
            throw new BaseException(ALERTS_NOT_EXIST_ALERT);
        }

        try{
            alertDao.deleteAlert(userIdx, alertIdx);
            return alertIdx + "번 알림을 삭제했습니다.";
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public PostCreateAlertRes createAlert(PostCreateAlertReq postCreateAlertReq) throws BaseException {
        int sendUserIdx = JwtTool.getUserIdx();


        // 유저 정보가 일치하는지 확인
        if(userProvider.checkUserIdx(postCreateAlertReq.getReceiveUserIdx()) == 0 ||
                userProvider.checkUserIdx(sendUserIdx) == 0){
            throw new BaseException(USERS_NOT_EXIST_USER);
        }

        // 폴더 보낼 경우 자신의 폴더가 맞는지 확인
        if(postCreateAlertReq.getAlertType() == 1
                && folderProvider.checkFolder(sendUserIdx, postCreateAlertReq.getFolderIdx()) == 0){
            throw new BaseException(FOLDERS_NOT_HAVE_USERS);
        }

        // 링크 보낼 경우 자신의 링크가 맞는지 확인
        if(postCreateAlertReq.getAlertType() == 2
                && linkProvider.checkLinkUser(sendUserIdx, postCreateAlertReq.getLinkIdx()) == 0){
            throw new BaseException(LINKS_NOT_HAVE_USERS);
        }


        try{
            PostCreateAlertRes postCreateAlertRes = alertDao.createAlert(sendUserIdx, postCreateAlertReq);
            return postCreateAlertRes;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
