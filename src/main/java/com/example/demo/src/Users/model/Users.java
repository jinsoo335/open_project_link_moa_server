package com.example.demo.src.Users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Users {
    private int userIdx;
    private String id;
    private String nickname;
    private String profileImageUrl;
}
