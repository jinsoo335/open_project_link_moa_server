package com.example.demo.src.Links;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.security.JwtTool;
import com.example.demo.src.Links.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class LinkService {
    private final LinkDao linkDao;
    private final LinkProvider linkProvider;

    @Autowired
    public LinkService(LinkDao linkDao,LinkProvider linkProvider) {

        this.linkDao = linkDao;
        this.linkProvider = linkProvider;
    }

    public PostCreateLinkRes postCreateLink(PostCreateLinkReq postCreateLinkReq) throws BaseException {
        try{
            int userIdx = JwtTool.getUserIdx();

            PostCreateLinkRes postCreateLinkRes = linkDao.postCreateLink(postCreateLinkReq);
            return postCreateLinkRes;

        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public PatchLinkRes modifyLink(PatchLinkReq patchLinkReq) throws BaseException{

        // 해당 링크가 존재하는지 확인
        if(linkProvider.checkLink(patchLinkReq.getLinkIdx()) == 0){
            throw new BaseException(LINKS_NOT_EXIST_LINK);
        }

        // 다른 링크 중에서 변경하고자 하는 이름을 가진 링크가 있는지 확인
        if(linkProvider.checkLinkAlias(patchLinkReq.getUpdateLinkAlias()) == 1){
            throw new BaseException(LINKS_EXIST_LINK_NAME);
        }

        try{
            PatchLinkRes patchLinkRes = linkDao.modifyLink(patchLinkReq);
            return patchLinkRes;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }



    }
    public DeleteLinkRes deleteLink(int linkIdx) throws BaseException {

        // 해당 링크가 존재하는지 확인
        if(linkProvider.checkLink(linkIdx) == 0){
            throw new BaseException(LINKS_NOT_EXIST_LINK);
        }

        DeleteLinkRes deleteFolderRes;

        try{
            deleteFolderRes = linkDao.deleteLink(linkIdx);
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }

        // 삭제 후, 해당 링크가 존재하는지 확인
        if(linkProvider.checkLink(deleteFolderRes.getLinkIdx()) == 1){
            throw new BaseException(LINKS_DELETE_FAILED);
        }

        return deleteFolderRes;

    }
}
