package com.example.demo.src.Links;

import com.example.demo.config.BaseException;
import com.example.demo.security.JwtTool;
import com.example.demo.src.Links.model.GetLinkRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class LinkProvider {
    private final LinkDao linkDao;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public LinkProvider(LinkDao linkDao) {
        this.linkDao = linkDao;
    }

    public List<GetLinkRes> getLinks() throws BaseException {
        try{
            int userIdx = JwtTool.getUserIdx();
            List<GetLinkRes> getLinkRes = linkDao.getLinks(userIdx);
            return getLinkRes;

        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
