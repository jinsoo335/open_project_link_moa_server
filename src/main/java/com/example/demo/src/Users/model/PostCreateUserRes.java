package com.example.demo.src.Users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostCreateUserRes {
    private int userIdx;
    private String jwtToken;
}
