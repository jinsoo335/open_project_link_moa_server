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
        // 해당 링크의 이름이 해당 폴더에 이미 있는지 확인
        if(linkProvider.checkLinkAlias(userIdx,postCreateLinkReq.getFolderIdx(),postCreateLinkReq.getLinkAlias())==1){
            throw new BaseException(LINKS_EXIST_LINK_ALIAS);
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

    public PostCopyLinkRes copyLink(PostCopyLinkReq postCopyLinkReq)throws BaseException{
        int userIdx = JwtTool.getUserIdx();

        // 해당 링크가 존재하는지 확인
        if(linkProvider.checkLink(postCopyLinkReq.getLinkIdx()) == 0){
            throw new BaseException(LINKS_NOT_EXIST_LINK);
        }



        //링크 Alias,Url가져오기
        String linkAlias = linkProvider.getLinkAlias(postCopyLinkReq.getLinkIdx());//링크 idx에 맞는 alias가져옴
        String linkUrl = linkProvider.getLinkUrl(postCopyLinkReq.getLinkIdx());//링크 idx에 맞는 alias가져옴


        //복사하고자 하는 링크와 동일한 이름의 링크가 존재하는 지 확인
        // 있는 경우엔 (1),(2) 붙이기
        int i = 1;
        StringBuffer sb = new StringBuffer(linkAlias);

        while(true){
            if(linkProvider.checkLinkAlias(userIdx, postCopyLinkReq.getFolderIdx(),sb.toString())==0){
                linkAlias = sb.toString();
                break;
            }
            if(i > 1){
                int digit = String.valueOf(i).length();
                sb.replace(sb.length() - 1 - digit,sb.length() -digit, String.valueOf(i));

            }
            else{
                sb.append("(1)");
            }
            i++;

        }

        try{
            PostCopyLinkRes postCopyLinkRes = linkDao.copyLink(postCopyLinkReq,linkAlias,linkUrl);
            return postCopyLinkRes;
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
