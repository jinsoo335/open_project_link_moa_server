package com.example.demo.src.Users;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Users.model.*;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/users")
public class UserController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserProvider userProvider;
    private final UserService userService;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.dir}")
    private String dir;

    @Autowired
    public UserController(UserProvider userProvider, UserService userService, AmazonS3 amazonS3) {
        this.userProvider = userProvider;
        this.userService = userService;
        this.amazonS3 = amazonS3;
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




    /**
     * 닉네임으로 사용자 검색 함수
     * @param search
     * @return List<GetUserRes>
     */
    @GetMapping("/nickname")
    public BaseResponse<List<GetUserRes>> getUserNickname(@RequestParam(required = false) String search){
        try{
            List<GetUserRes> getUserRes = userProvider.getUserNickname(search);
            return new BaseResponse<>(getUserRes);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    /**
     * id로 사용자를 검색하는 함수
     * @param search
     * @return GetUserRes
     */
    @GetMapping("/id")
    public BaseResponse<GetUserRes> getUserId(@RequestParam(required = false) String search){
        try{
            GetUserRes getUserRes = userProvider.getUserId(search);
            return new BaseResponse<>(getUserRes);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    /**
     * 사용자 프로필 수정
     * @param patchUserReq
     * @return BaseResponse<PatchUserRes>
     */
    @PatchMapping("/modify")
    public BaseResponse<PatchUserRes> modifyUser(@RequestBody PatchUserReq patchUserReq){
        try{
            if(!patchUserReq.getNickname().matches("^([ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9,. ])*$")){
                return new BaseResponse<>(USERS_UNABLE_USER_NICKNAME);
            }

            PatchUserRes patchUserRes = userService.modifyUser(patchUserReq);
            return new BaseResponse<>(patchUserRes);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 검색어를 기준으로 사용자의 id나 닉네임에 일치하는 부분이 있으면 해당 유저 정보 반환
     * @param search
     * @return BaseResponse<List<GetUserRes>>
     */
    @GetMapping("")
    public BaseResponse<List<GetUserRes>> getUser(@RequestParam(required = false) String search){
        try{
            List<GetUserRes> getUserResList = userProvider.getUser(search);
            return new BaseResponse<>(getUserResList);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    /**
     * multipart를 이용해 이미지를 받아 s3로 업로드하고 해당 url을 db에 저장하는 함수
     * 1048576 bytes 이상의 이미지는 받지 못함
     * @param file
     * @return 성공 or 실패(String)
     */
    @PostMapping("/form")
    public BaseResponse<String> uploadForm(@RequestParam(value = "images", required = false) MultipartFile file){
        if(file.isEmpty()){
            return new BaseResponse<>(USERS_EMPTY_IMAGE);
        }

        try{
            String imageUrl = userService.s3Upload(file);

            int userIdx = userService.pushUserImage(imageUrl);

            return new BaseResponse<>(imageUrl);
        } catch (FileSizeLimitExceededException e){
            return new BaseResponse<>(FILE_SIZE_LIMIT_OVER);
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



    @GetMapping("/form/{userIdx}")
    public ResponseEntity<byte[]> downloadForm(@PathVariable("userIdx") int userIdx){
        try{
            if(userIdx <= 0){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            ResponseEntity<byte[]> res  = userService.downloadForm(userIdx);
            return res;
        } catch (BaseException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }






}
