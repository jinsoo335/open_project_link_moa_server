package com.example.demo.src.Links;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Links.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    //링크 불러오기 API
    @GetMapping("")
    public BaseResponse<List<GetLinkRes>> getLinks(@RequestParam int folderIdx){
        try{
            List<GetLinkRes> getLinkRes = linkProvider.getLinks(folderIdx);
            return new BaseResponse<>(getLinkRes);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    //링크 생성 API
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
        if(!postCreateLinkReq.getLinkAlias().matches("^([ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9,. ])*$")){
            return new BaseResponse<>(LINKS_SPECIAL_CHAR_LINK);
        }
        try{
            PostCreateLinkRes postCreateLinkRes = linkService.postCreateLink(postCreateLinkReq);
            return new BaseResponse<>(postCreateLinkRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


     //링크 수정(Url, Alias) API
    @PatchMapping("/modify")
    public BaseResponse<PatchLinkRes> modifyLink(@RequestBody PatchLinkReq patchLinkReq){
        try{// 링크 별칭 글자수 validation
            if (patchLinkReq.getUpdateLinkAlias().length() > 50 || patchLinkReq.getUpdateLinkAlias().length() < 1) {
                if (patchLinkReq.getUpdateLinkAlias().length()==0)
                {
                    return new BaseResponse<>(LINKS_EMPTY_LINK_NAME);
                }
                return new BaseResponse<>(LINKS_UNABLE_LENGTH_LINK_NAME);
            }
            // 링크 별칭 특수문자 확인
            if(!patchLinkReq.getUpdateLinkAlias().matches("^([ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9,. ])*$")){
                return new BaseResponse<>(LINKS_SPECIAL_CHAR_LINK);
            }

            PatchLinkRes patchLinkRes = linkService.modifyLink(patchLinkReq);
            return new BaseResponse<>(patchLinkRes);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    //링크 삭제 API
    @DeleteMapping("/delete/{linkIdx}")
    public BaseResponse<String> deleteLink(@PathVariable ("linkIdx") int linkIdx){
        try{
            DeleteLinkRes deleteLinkRes = linkService.deleteLink(linkIdx);
            return new BaseResponse<>(linkIdx + "번 링크를 삭제했습니다.");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
