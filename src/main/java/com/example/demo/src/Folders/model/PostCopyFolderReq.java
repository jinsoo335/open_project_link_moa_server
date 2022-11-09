package com.example.demo.src.Folders.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostCopyFolderReq {
    private int folderIdx;
    private int sendUserIdx;
}
