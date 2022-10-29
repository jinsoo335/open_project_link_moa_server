package com.example.demo.src.Users;

import com.example.demo.config.BaseException;
import com.example.demo.security.JwtTool;
import com.example.demo.src.Users.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class UserService {

    private final UserProvider userProvider;

    private final UserDao userDao;

    @Autowired
    public UserService(UserProvider userProvider, UserDao userDao) {
        this.userProvider = userProvider;
        this.userDao = userDao;
    }


    public PostCreateUserRes createUser(PostCreateUserReq postCreateUserReq) throws BaseException {
        int res;
        try{
            res = userProvider.checkUserId(postCreateUserReq.getId());
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

        if(res == 1){
            throw new BaseException(USERS_EXIST_USER_ID);
        }


        try{
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String securityPw = encoder.encode(postCreateUserReq.getPassword());

            postCreateUserReq.setPassword(securityPw);

            int userIdx = userDao.createUser(postCreateUserReq);
            String jwtToken = JwtTool.crateJwtToken(userIdx);

            return new PostCreateUserRes(userIdx, jwtToken);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes loginUser(PostLoginReq postLoginReq) throws BaseException {
        if(userProvider.checkUserId(postLoginReq.getId()) == 0){
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        if(userProvider.checkUserPassword(postLoginReq.getId(), postLoginReq.getPassword()) == 0){
            throw new BaseException(USERS_INVALID_USER_PASSWORD);
        }


        try{
            Users users = userDao.loginUser(postLoginReq);
            String jwtToken = JwtTool.crateJwtToken(users.getUserIdx());

            return new PostLoginRes(users, jwtToken);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
