package com.example.demo.src.Users;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.security.JwtTool;
import com.example.demo.src.Users.model.PostCreateUserReq;
import com.example.demo.src.Users.model.PostCreateUserRes;
import com.example.demo.src.Users.model.PostLoginReq;
import com.example.demo.src.Users.model.PostLoginRes;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/users")
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
     *  아이디 중복 불가
     *  [Post] /users
     * @return BaseResponse<PostCreateUserRes>
     */
    @RequestMapping("/create")
    public BaseResponse<PostCreateUserRes> createUser(@RequestBody PostCreateUserReq postCreateUserReq){
        if (postCreateUserReq.getId().length() > 20 || postCreateUserReq.getId().length() < 8) {
            return new BaseResponse<>(USERS_UNABLE_LENGTH_USER_ID);
        }

        if(postCreateUserReq.getPassword().length() > 20 || postCreateUserReq.getPassword().length() < 8){
            return new BaseResponse<>(USERS_UNABLE_LENGTH_USER_PASSWORD);
        }


        if(!postCreateUserReq.getId().matches("^[a-zA-Z0-9]*$")){
            return new BaseResponse<>(USERS_SPECIAL_CHAR_USER_ID);
        }

        try{
            PostCreateUserRes postCreateUserRes = userService.createUser(postCreateUserReq);
            return new BaseResponse<>(postCreateUserRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }


    /**
     *  로그인 API
     *  [Post] users/login
     * @return BaseResponse<PostLoginRes>
     */
    @RequestMapping("/login")
    public BaseResponse<PostLoginRes> loginUser(@RequestBody PostLoginReq postLoginReq){
        if (postLoginReq.getId().length() > 20 || postLoginReq.getId().length() < 8) {
            return new BaseResponse<>(USERS_UNABLE_LENGTH_USER_ID);
        }

        if(postLoginReq.getPassword().length() > 20 || postLoginReq.getPassword().length() < 8){
            return new BaseResponse<>(USERS_UNABLE_LENGTH_USER_PASSWORD);
        }

        if(!postLoginReq.getId().matches("^[a-zA-Z0-9]*$")){
            return new BaseResponse<>(USERS_SPECIAL_CHAR_USER_ID);
        }


        try{
            PostLoginRes postLoginRes = userService.loginUser(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
