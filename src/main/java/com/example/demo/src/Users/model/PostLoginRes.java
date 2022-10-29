package com.example.demo.src.Users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostLoginRes {
    private int userIdx;
    private String id;
    private String nickname;
    private String profileImageUrl;
    private String jwtToken;

    public PostLoginRes(Users users, String jwtToken) {
        this.userIdx = users.getUserIdx();
        this.id = users.getId();
        this.nickname = users.getNickname();
        this.profileImageUrl = users.getProfileImageUrl();
        this.jwtToken = jwtToken;
    }
}
