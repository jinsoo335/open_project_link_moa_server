package com.example.demo.src.Links.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostCopyLinkReq {

    private int folderIdx;
    private int linkIdx;
    private int sendUserIdx;
}
