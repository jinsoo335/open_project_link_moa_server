package com.example.demo.src.Users;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Users.model.PostCreateUserReq;
import com.example.demo.src.Users.model.PostCreateUserRes;
import org.springframework.beans.factory.annotation.Autowired;
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
        try{
            PostCreateUserRes postCreateUserRes = userDao.createUser(postCreateUserReq);
            return postCreateUserRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
