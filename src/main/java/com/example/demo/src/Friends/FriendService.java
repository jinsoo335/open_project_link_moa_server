//package com.example.demo.src.Friends;
//
//import com.example.demo.config.BaseException;
//import com.example.demo.src.Friends.model.PostCreateFriendRelationReq;
//import com.example.demo.src.Friends.model.PostCreateFriendRelationRes;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
//
//@Service
//public class FriendService {
//    private final FriendDao friendDao;
//    private final FriendProvider friendProvider;
//
//    @Autowired
//    public FriendService(FriendDao friendDao, FriendProvider friendProvider)
//    {
//        this.friendDao = friendDao;
//        this.friendProvider = friendProvider;
//    }
//
//    public PostCreateFriendRelationRes postCreateFriendRelation(PostCreateFriendRelationReq postCreateFriendRelationReq)throws BaseException{
//        try{
//            PostCreateFriendRelationRes postCreateFriendRelationRes = folderDao.postCreateFriendRelation(postCreateFriendRelationReq);
//            return postCreateFriendRelationRes;
//        }catch (Exception e){
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//}
