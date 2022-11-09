package com.example.demo.src.Links.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetLinkRes {
    private String linkUrl;
    private int linkIdx;
    private String linkAlias;
}
