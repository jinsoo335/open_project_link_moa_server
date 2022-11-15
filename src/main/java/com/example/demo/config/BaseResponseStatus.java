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
    USERS_NOT_EXIST_USER(false, 2016, "사용자가 존재하지 않습니다."),
    USERS_UNABLE_USER_NICKNAME(false, 2017, "사용할 수 없는 닉네임입니다."),


    // Folders
    FOLDERS_EMPTY_FOLDER_NAME(false, 2050, "폴더 생성 시 이름이 필요합니다."),
    FOLDERS_UNABLE_LENGTH_FOLDER_NAME(false, 2051, "폴더 이름의 길이를 맞춰주세요."),
    FOLDERS_EXIST_FOLDER_NAME(false,2052, "같은 이름의 폴더가 이미 있습니다."),
    FOLDERS_UNABLE_WORD_FOLDER_NAME(false, 2053, "사용할 수 없는 폴더 이름입니다."),
    FOLDERS_NOT_EXIST_FOLDER(false, 2054, "해당 폴더가 존재하지 않습니다."),
    FOLDERS_DELETE_FAILED(false, 2055, "폴더 삭제에 실패했습니다."),
    FOLDERS_DUPLICATE_FOLDER_NAME(false, 2056, "중복된 폴더 이름이 있습니다."),
    FOLDERS_NOT_HAVE_USERS(false,2057,"해당 유저의 폴더가 아닙니다."),


    //Links
    LINKS_EMPTY_LINK_NAME(false, 2100, "링크 생성 시 별칭이 필요합니다."),
    LINKS_UNABLE_LENGTH_LINK_NAME(false, 2101, "링크 별칭의 길이를 맞춰주세요."),
    LINKS_SPECIAL_CHAR_LINK(false, 2102, "링크 별칭에 특수문자를 사용할 수 없습니다."),
    LINKS_NOT_EXIST_LINK(false, 2103, "해당 링크가 존재하지 않습니다."),
    LINKS_DELETE_FAILED(false, 2104, "링크 삭제에 실패했습니다."),
    LINKS_NOT_HAVE_USERS(false,2105,"해당 유저의 링크가 아닙니다."),
    LINKS_NOT_EXIST_LINK_ALIAS(false,2106,"해당 링크별칭에 맞는 링크가 존재하지 않습니다."),

    LINKS_EXIST_LINK_ALIAS(false,2107, "같은 이름의 링크가 이미 있습니다."),


    //Friend
    FRIEND_NOT_MY_SELF(false,2200,"스스로와 친구가 될 수는 없습니다."),
    FRIEND_NOT_IN_USERS(false,2201,"친구 요청한 친구가 유저 목록에 없습니다."),
    FRIEND_EXIST_ALREADY(false,2202,"이미 친구목록에 있습니다."),
    FRIEND_NOT_EXIST(false,2202,"친구 목록에 없는 사람입니다."),
    FRIEND_NOT_MY_SELF_DELETE(false,2203,"본인을 친구목록에서 삭제할 수 없습니다."),
    FRIEND_NOT_IN_USERS_DELETE(false,2204,"친구목록에서 삭제할 친구가 유저 목록에 없습니다."),





    // Alerts
    ALERTS_NOT_EXIST_ALERT(false, 2150, "알림이 존재하지 않습니다."),
    ALERTS_EMPTY_RECEIVE_USERIDX(false, 2151, "알림에는 받는 사람 번호가 필요합니다."),
    ALERTS_EMPTY_FOLDERIDX(false, 2152, "폴더 공유 알림에는 폴더 번호가 필요합니다."),
    ALERTS_EMPTY_LINKIDX(false, 2153, "링크 공유 알림에는 링크 번호가 필요합니다."),
    ALERTS_EMPTY_ALERT_TEXT(false, 2154, "알림 내용이 필요합니다."),






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
