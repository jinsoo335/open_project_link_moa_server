package com.example.demo.src.Users;

import com.example.demo.config.BaseException;
import com.example.demo.src.Users.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class UserProvider {
    private final UserDao userDao;

    @Autowired
    public UserProvider(UserDao userDao) {
        this.userDao = userDao;
    }


    public int checkUserIdx(int userIdx) throws BaseException{
        try{
            int res = userDao.checkUserIdx(userIdx);
            return res;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public int checkUserId(String id) throws BaseException{
        try{
            // 가입하려는 id를 db에 검색해 있는지 확인하는 함수 - 있으면 1, 없으면 0
            int res = userDao.checkUserId(id);

            return res;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 비밀번호 체크용 함수이나 현재는 BCryptPasswordEncoder를 사용해서 쓰지 않는 함수
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


    public int checkUserNickname(String nickname) throws BaseException{
        try{
            int res = userDao.checkUserNickname(nickname);
            return res;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }



    public List<GetUserRes> getUserNickname(String search) throws BaseException {

        // 찾고자 하는 닉네임이 없는 경우
        if(checkUserNickname(search) == 0){
            throw new BaseException(USERS_NOT_EXIST_USER);
        }

        try{
            List<GetUserRes> getUserRes = userDao.getUserNickname(search);
            return getUserRes;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetUserRes getUserId(String search) throws BaseException {
        if(checkUserId(search) == 0){
            throw new BaseException(USERS_NOT_EXIST_USER);
        }

        try{
            GetUserRes getUserRes = userDao.getUserId(search);
            return getUserRes;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
