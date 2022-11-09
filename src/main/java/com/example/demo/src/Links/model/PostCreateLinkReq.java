package com.example.demo.src.Links.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostCreateLinkReq {
    private String linkUrl;
    private int folderIdx;
    private String linkAlias;
}
