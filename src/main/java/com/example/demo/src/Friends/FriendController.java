//package com.example.demo.src.Friends;
//
//import com.example.demo.config.BaseException;
//import com.example.demo.config.BaseResponse;
//import com.example.demo.src.Friends.model.PostCreateFriendRelationReq;
//import com.example.demo.src.Friends.model.PostCreateFriendRelationRes;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/friend")
//public class FriendController {
//    private final FriendProvider friendProvider;
//    private final FriendService friendService;
//
//    @Autowired
//    public FriendController(FriendProvider friendProvider,FriendService friendService){
//        this.friendProvider = friendProvider;
//        this.friendService = friendService;
//    }
//
//    /**
//     * 친구관계를 생성하는 API
//     * @param postCreateFriendRelationReq
//     * @return postCreateFriendRelationRes
//     */
//    @PostMapping("/create")
//    public BaseResponse<PostCreateFriendRelationRes> postCreateFriendRelation(@RequestBody PostCreateFriendRelationReq postCreateFriendRelationReq){
//        try{
//            PostCreateFriendRelationRes postCreateFriendRelationRes = friendService.createFriendRelation(postCreateFriendRelationReq);
//            return new BaseResponse<>(postCreateFriendRelationRes);
//        }catch (BaseException e){
//            return new BaseResponse<>(e.getStatus());
//        }
//    }
//
//}
