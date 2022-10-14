package com.example.demo.src.Users;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Users.model.PostCreateUserReq;
import com.example.demo.src.Users.model.PostCreateUserRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserProvider userProvider;
    private final UserService userService;

    @Autowired
    public UserController(UserProvider userProvider, UserService userService) {
        this.userProvider = userProvider;
        this.userService = userService;
    }


    /**
     *  회원 가입 API
     *  [Post] /users
     * @return BaseResponse<PostCreateUserRes>
     */
    @RequestMapping("/create")
    public BaseResponse<PostCreateUserRes> createUser(@RequestBody PostCreateUserReq postCreateUserReq){

        try{
            PostCreateUserRes postCreateUserRes = userService.createUser(postCreateUserReq);
            return new BaseResponse<>(postCreateUserRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }


}
