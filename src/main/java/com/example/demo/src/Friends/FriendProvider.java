package com.example.demo.src.Friends;


import com.example.demo.config.BaseException;
import com.example.demo.security.JwtTool;
import com.example.demo.src.Friends.model.GetFriendsRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class FriendProvider {
    private final FriendDao friendDao;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public FriendProvider(FriendDao friendDao) {
        this.friendDao = friendDao;
    }

    public int checkFriend(int sendUserIdx)throws BaseException {
        try{
            int res = friendDao.checkFriend(sendUserIdx);
            return res;
        }catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkFriendAlready(int userIdx,int sendUserIdx)throws BaseException{
        try{
            int res = friendDao.checkFriendAlready(userIdx,sendUserIdx);
            return res;
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public List<GetFriendsRes> getFriends() throws BaseException{
        int userIdx = JwtTool.getUserIdx();
        try{
            List<GetFriendsRes> getFriendsRes = friendDao.getFriends(userIdx);
            return getFriendsRes;
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
