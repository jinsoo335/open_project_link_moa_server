package com.example.demo.src.Users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostCreateUserReq {
    private String id;
    private String password;
    private String nickname;
    private String profileImageUrl;
}
