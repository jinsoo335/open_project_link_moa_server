package com.example.demo.src.Friends;

import com.example.demo.config.BaseException;
import com.example.demo.security.JwtTool;
import com.example.demo.src.Friends.model.DeleteFriendRes;
import com.example.demo.src.Friends.model.PostCreateFriendRelationRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class FriendService {
    private final FriendDao friendDao;
    private final FriendProvider friendProvider;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public FriendService(FriendDao friendDao, FriendProvider friendProvider)
    {
        this.friendDao = friendDao;
        this.friendProvider = friendProvider;
    }

    public PostCreateFriendRelationRes postCreateFriendRelation(int sendUserIdx)throws BaseException{
        int userIdx= JwtTool.getUserIdx();

        if(friendProvider.checkFriend(sendUserIdx)==0)
            throw new BaseException(FRIEND_NOT_IN_USERS);

        if(userIdx==sendUserIdx)
            throw new BaseException(FRIEND_NOT_MY_SELF);

        if(friendProvider.checkFriendAlready(userIdx,sendUserIdx)==1)
            throw new BaseException(FRIEND_EXIST_ALREADY);

        try{

            PostCreateFriendRelationRes postCreateFriendRelationRes = friendDao.postCreateFriendRelation(userIdx,sendUserIdx);
            return postCreateFriendRelationRes;
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public DeleteFriendRes deleteFriend(int deleteFriendIdx) throws BaseException{
        int userIdx = JwtTool.getUserIdx();


        if(friendProvider.checkFriend(deleteFriendIdx)==0)
            throw new BaseException(FRIEND_NOT_IN_USERS_DELETE);

        if(userIdx==deleteFriendIdx)
            throw new BaseException(FRIEND_NOT_MY_SELF_DELETE);

        if(friendProvider.checkFriendAlready(userIdx,deleteFriendIdx)==0)
            throw new BaseException(FRIEND_NOT_EXIST);



        DeleteFriendRes deleteFriendRes;

        try{
            deleteFriendRes = friendDao.deleteFriend(userIdx,deleteFriendIdx);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
        return deleteFriendRes;
    }
}
