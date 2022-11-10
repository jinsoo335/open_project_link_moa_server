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
        int userIdx = JwtTool.getUserIdx();

        // 해당 유저에게 폴더가 존재하는지 확인
        if(linkProvider.checkFolderUser(userIdx,postCreateLinkReq.getFolderIdx()) == 0){
            throw new BaseException(FOLDERS_NOT_HAVE_USERS);
        }
        try{

            PostCreateLinkRes postCreateLinkRes = linkDao.postCreateLink(userIdx,postCreateLinkReq);


            return postCreateLinkRes;



        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public PatchLinkRes modifyLink(PatchLinkReq patchLinkReq) throws BaseException{
        int userIdx = JwtTool.getUserIdx();

        // 수정하고자 하는 이가 해당 링크의 주인인지에 대해 확인
        if(linkProvider.checkLinkUser(userIdx,patchLinkReq.getLinkIdx())==0){

            throw new BaseException(LINKS_NOT_HAVE_USERS);
        }


        // 해당 링크가 존재하는지 확인
        if(linkProvider.checkLink(patchLinkReq.getLinkIdx()) == 0){
            throw new BaseException(LINKS_NOT_EXIST_LINK);
        }


        try{
            PatchLinkRes patchLinkRes = linkDao.modifyLink(patchLinkReq);
            return patchLinkRes;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }



    }
    public DeleteLinkRes deleteLink(int linkIdx) throws BaseException {
        int userIdx = JwtTool.getUserIdx();

        // 수정하고자 하는 이가 해당 링크의 주인인지에 대해 확인
        if(linkProvider.checkLinkUser(userIdx,linkIdx)==0){

            throw new BaseException(LINKS_NOT_HAVE_USERS);
        }

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
