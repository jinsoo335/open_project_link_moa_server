package com.example.demo.src.Friends.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFriendsRes {
    private int userIdx;
    private String id;
    private String nickname;
    private String profileImageUrl;
}
