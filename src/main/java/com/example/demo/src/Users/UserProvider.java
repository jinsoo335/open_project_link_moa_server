package com.example.demo.src.Users;

import com.example.demo.config.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.USERS_EXIST_USER_ID;

@Service
public class UserProvider {
    private final UserDao userDao;

    @Autowired
    public UserProvider(UserDao userDao) {
        this.userDao = userDao;
    }


    public int checkUserId(String id) throws BaseException{
        try{
            // 가입하려는 id를 db에 검색해 있는지 확인하는 함수 - 있으면 1
            int res = userDao.checkUserId(id);

            return res;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserPassword(String id, String password) throws BaseException{
        try{
            // int res = userDao.checkUserPassword(id, password);
            String pwd = userDao.getUserPassword(id);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if(encoder.matches(password, pwd)){
                return 1;
            }

            return 0;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
