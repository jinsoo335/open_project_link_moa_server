package com.example.demo.security;

import com.example.demo.config.BaseException;
//import com.example.demo.config.secret.Secret;     yml으로 jwt secret key 값 이동
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.example.demo.config.BaseResponseStatus.EMPTY_JWT;
import static com.example.demo.config.BaseResponseStatus.INVALID_JWT;

@Service
public class JwtTool {


    private static String JWT_SECRET_KEY;

    @Value("${jwt.JWT_SECRET_KEY}")
    private void setJwtSecretKey(String temp){
        JWT_SECRET_KEY = temp;
    }

    private static final int JWT_EXPIRAION = 1000 *60 * 60 * 24 * 365;


//    public static String crateJwtToken(int userIdx){
//        Date now = new Date();
//        Date expireDate = new Date(now.getTime() + JWT_EXPIRAION);
//
//        return Jwts.builder()
//                .setExpiration(expireDate)                                       // 만료 시간 생성
//                .setHeaderParam("typ", "jwt")                        // jwt header에 들어갈 typ : jwt 명시
//                .setIssuedAt(now)                                               // 발급 시간 - 현재 시간 기준으로 생성
//                .claim("userIdx", userIdx)                                // 사용자 id를 가지고 jwt token 생성
//                .signWith(SignatureAlgorithm.HS512, Secret.JWT_SECRET_KEY)      // 알고리즘, secret 값 설정
//                .compact();
//    }

    public static String crateJwtToken(int userIdx){
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + JWT_EXPIRAION);

        return Jwts.builder()
                .setExpiration(expireDate)                                       // 만료 시간 생성
                .setHeaderParam("typ", "jwt")                        // jwt header에 들어갈 typ : jwt 명시
                .setIssuedAt(now)                                               // 발급 시간 - 현재 시간 기준으로 생성
                .claim("userIdx", userIdx)                                // 사용자 id를 가지고 jwt token 생성
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_KEY)      // 알고리즘, secret 값 설정
                .compact();
    }


    /**
     * request header에서 jwt 부분만 추출하는 함수
     * header에 접근하는 코드가 controller 부분에서만 사용돠지 않을 수도 있기에 별도의 함수로 만듬
     * @return String jwt
     */
    public static String getJwtByHeader(){
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return req.getHeader("LF-ACCESS-TOKEN");
    }

    /**
     * jwt 안에 들어있는 userIdx 값 추출 함수
     * @return int userIdx
     */

    public static int getUserIdx() throws BaseException {
        String jwtToken = getJwtByHeader();
        if(jwtToken == null || jwtToken.length() == 0){
            throw new BaseException(EMPTY_JWT);
        }

        Claims claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET_KEY)
                    .parseClaimsJws(jwtToken)
                    .getBody();
        } catch (Exception e){
            throw new BaseException(INVALID_JWT);
        }


        return claims.get("userIdx", Integer.class);
    }

}
