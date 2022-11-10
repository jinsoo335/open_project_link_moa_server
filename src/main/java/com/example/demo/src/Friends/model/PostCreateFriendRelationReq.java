package com.example.demo.src.Friends.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostCreateFriendRelationReq {
    private int firstUserIdx;
    private int secondUserIdx;

}
