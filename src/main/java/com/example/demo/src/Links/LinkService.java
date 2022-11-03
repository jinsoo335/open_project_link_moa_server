package com.example.demo.src.Links;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.security.JwtTool;
import com.example.demo.src.Links.model.PostCreateLinkReq;
import com.example.demo.src.Links.model.PostCreateLinkRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
