package com.example.demo.src.Links;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Folders.FolderProvider;
import com.example.demo.src.Folders.FolderService;
import com.example.demo.src.Folders.model.GetFolderRes;
import com.example.demo.src.Folders.model.PostCreateFolderReq;
import com.example.demo.src.Folders.model.PostCreateFolderRes;
import com.example.demo.src.Links.model.GetLinkRes;
import com.example.demo.src.Links.model.PostCreateLinkReq;
import com.example.demo.src.Links.model.PostCreateLinkRes;
import com.example.demo.src.Users.model.PostCreateUserReq;
import com.example.demo.src.Users.model.PostCreateUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.example.demo.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/links")
public class LinkController {
    private final LinkProvider linkProvider;
    private final LinkService linkService;

    @Autowired
    public LinkController(LinkProvider linkProvider, LinkService linkService) {
        this.linkProvider = linkProvider;
        this.linkService = linkService;
    }


    @GetMapping("")
    public BaseResponse<List<GetLinkRes>> getLinks(){
        try{
            List<GetLinkRes> getLinkRes = linkProvider.getLinks();
            return new BaseResponse<>(getLinkRes);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    @PostMapping("/create")
    public BaseResponse<PostCreateLinkRes> postCreateLinks(@RequestBody PostCreateLinkReq postCreateLinkReq){
        //링크 별칭 길이 . 0일 경우와 , 50이상인 경우 걸러냄.
        if (postCreateLinkReq.getLinkAlias().length() > 50 || postCreateLinkReq.getLinkAlias().length() < 1) {
            if (postCreateLinkReq.getLinkAlias().length()==0)
            {
                return new BaseResponse<>(LINKS_EMPTY_LINK_NAME);
            }
            return new BaseResponse<>(LINKS_UNABLE_LENGTH_LINK_NAME);
        }
        // 링크 별칭 특수문자 확인
        if(!postCreateLinkReq.getLinkAlias().matches("^[a-zA-Z0-9가-힣]*$")){
            return new BaseResponse<>(LINKS_SPECIAL_CHAR_LINK);
        }
        try{
            PostCreateLinkRes postCreateLinkRes = linkService.postCreateLink(postCreateLinkReq);
            return new BaseResponse<>(postCreateLinkRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
