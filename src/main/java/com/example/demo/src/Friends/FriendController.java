package com.example.demo.src.Friends;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Friends.model.DeleteFriendRes;
import com.example.demo.src.Friends.model.GetFriendsRes;
import com.example.demo.src.Friends.model.PostCreateFriendRelationRes;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FriendProvider friendProvider;
    private final FriendService friendService;

    @Autowired
    public FriendController(FriendProvider friendProvider,FriendService friendService){
        this.friendProvider = friendProvider;
        this.friendService = friendService;
    }

    /**
     * 친구관계를 생성하는 API
     * @return postCreateFriendRelationRes
     */
    @PostMapping("/create/{sendUserIdx}")
    public BaseResponse<PostCreateFriendRelationRes> postCreateFriendRelation(@PathVariable ("sendUserIdx") int sendUserIdx) {


        try{
            PostCreateFriendRelationRes postCreateFriendRelationRes = friendService.postCreateFriendRelation(sendUserIdx);


            return new BaseResponse<>(postCreateFriendRelationRes);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 친구 목록 가져오는 API
     */
    @GetMapping("")
    public BaseResponse<List<GetFriendsRes>>getFriends(){
        try{
            List<GetFriendsRes> getFriendsRes = friendProvider.getFriends();
            return new BaseResponse<>(getFriendsRes);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 친구 삭제 API
     */
    @DeleteMapping("/delete/{deleteFriendIdx}")
    public BaseResponse<String> deleteFriend(@PathVariable("deleteFriendIdx") int deleteFriendIdx){
        try{
            DeleteFriendRes deleteFriendRes = friendService.deleteFriend(deleteFriendIdx);
            return new BaseResponse<>("userIdx가 "+deleteFriendIdx + "번인 친구를 삭제했습니다.");
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

}
