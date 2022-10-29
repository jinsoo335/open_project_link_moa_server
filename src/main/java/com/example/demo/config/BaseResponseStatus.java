package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),
    USERS_EXIST_USER_ID(false, 2011, "이미 존재하는 유저 아이디 입니다."),
    USERS_INVALID_USER_PASSWORD(false, 2012, "비밀번호가 일치하지 않습니다."),
    USERS_UNABLE_LENGTH_USER_ID(false, 2013, "아이디 글자수가 맞지 않습니다."),
    USERS_UNABLE_LENGTH_USER_PASSWORD(false, 2014, "비밀번호 글자수가 맞지 않습니다."),
    USERS_SPECIAL_CHAR_USER_ID(false, 2015, "아이디에 특수문자를 사용할 수 없습니다."),

    // Folders
    FOLDERS_EMPTY_FOLDER_NAME(false, 2050, "폴더 생성 시 이름이 필요합니다."),
    FOLDERS_UNABLE_LENGTH_FOLDER_NAME(false, 2051, "폴더 이름의 길이를 맞춰주세요."),
    FOLDERS_EXIST_FOLDER_NAME(false,2052, "같은 이름의 폴더가 이미 있습니다."),
    FOLDERS_UNABLE_WORD_FOLDER_NAME(false, 2053, "사용할 수 없는 폴더 이름입니다."),
    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다.");




    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
