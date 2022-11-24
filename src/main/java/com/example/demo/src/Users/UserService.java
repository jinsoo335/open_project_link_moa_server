package com.example.demo.src.Users;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.security.JwtTool;
import com.example.demo.src.Users.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class UserService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());


    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.dir}")
    private String dir;

    private final UserProvider userProvider;

    private final UserDao userDao;

    @Autowired
    public UserService(AmazonS3 amazonS3, UserProvider userProvider, UserDao userDao) {
        this.amazonS3 = amazonS3;
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

    public PatchUserRes modifyUser(PatchUserReq patchUserReq) throws BaseException {
        int userIdx = JwtTool.getUserIdx();

        if(userProvider.checkUserIdx(userIdx) == 0){
            throw new BaseException(USERS_NOT_EXIST_USER);
        }

        try{
            PatchUserRes patchUserRes = userDao.modifyUser(userIdx, patchUserReq);
            return patchUserRes;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }

    }


    public String s3Upload(MultipartFile file) throws IOException {

        try{
            String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();


            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getInputStream().available());

            amazonS3.putObject(bucket + dir, filename, file.getInputStream(), objectMetadata);

            return amazonS3.getUrl(bucket + dir, filename).toString();
        }catch (Exception e){
            return "s3 업로드 실패";
        }

    }


    public int pushUserImage(String imageUrl) throws BaseException{
        int userIdx = JwtTool.getUserIdx();

        try{
            return userDao.pushUserImage(userIdx, imageUrl);
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }


    }

    public ResponseEntity<byte[]> downloadForm(int userIdx) throws BaseException {
        int res = userProvider.checkUserIdx(userIdx);

        if(res == 0){
            throw new BaseException(USERS_NOT_EXIST_USER);
        }

        try{
            String imgUrl = userProvider.getUserImageUrl(userIdx);

            logger.warn("~~~~~~~~~");

            String[] arr = imgUrl.split("/");
            String type = arr[arr.length - 1];
            String fileName = URLEncoder.encode(type, "UTF-8").replaceAll("\\+", "%20");

            logger.warn(arr[arr.length - 2] + "/" + arr[arr.length - 1]);



           // GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, imgUrl);

            //S3Object s3Object = amazonS3.getObject(getObjectRequest);

            S3Object s3Object = amazonS3.getObject(bucket, arr[arr.length - 2] + "/" + arr[arr.length - 1]);


            S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(objectInputStream);


            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(contentType(imgUrl));
            httpHeaders.setContentLength(bytes.length);
            httpHeaders.setContentDispositionFormData("attachment", fileName);



            return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);

        } catch (AmazonServiceException e) {
            throw new BaseException(S3_SERVER_ERROR);
        }
        catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }



    }


    private MediaType contentType(String filename){
        String[] arr = filename.split("\\.");
        String type = arr[arr.length - 1];

        switch (type){
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
                return MediaType.IMAGE_JPEG;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

}
