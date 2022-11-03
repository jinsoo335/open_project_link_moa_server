package com.example.demo.src.Folders.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchFolderReq {
    private String UpdateFolderName;
    private int folderIdx;
}
