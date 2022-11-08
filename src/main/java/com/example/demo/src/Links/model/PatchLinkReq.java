package com.example.demo.src.Links.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchLinkReq {
    private String updateLinkUrl;
    private int linkIdx;
    private String updateLinkAlias;
}
