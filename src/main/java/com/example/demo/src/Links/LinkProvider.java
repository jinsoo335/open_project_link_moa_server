package com.example.demo.src.Links;

import com.example.demo.config.BaseException;
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

    public List<GetLinkRes> getLinks(int folderIdx) throws BaseException {
        try{
            List<GetLinkRes> getLinkRes = linkDao.getLinks(folderIdx);
            return getLinkRes;

        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    /**
     * 해당 사용자가 가진 폴더 중 linkAlias과 일치하는 폴더가 있는지 체크
     * -> 중복체크
     * @param userIdx
     * @param linkAlias
     * @return 존재하면 1, 없으면 0
     * @throws BaseException
     */
    public int checkLinkAlias(String linkAlias) throws BaseException {
        try{
            int res = linkDao.checkLinkAlias(linkAlias);
            return res;
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkLink(int folderIdx) throws BaseException {
        try{
            int res = linkDao.checkLink(folderIdx);
            return res;
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
