package com.example.demo.src.Folders;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.security.JwtTool;
import com.example.demo.src.Folders.model.GetFolderRes;

import com.example.demo.src.Folders.model.PostCreateFolderReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class FolderProvider {

    private final FolderDao folderDao;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public FolderProvider(FolderDao folderDao) {
        this.folderDao = folderDao;
    }

    public List<GetFolderRes> getFolders() throws BaseException{
        try{
            int userIdx = JwtTool.getUserIdx();
            List<GetFolderRes> getFolderRes = folderDao.getFolders(userIdx);
            return getFolderRes;

        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public int checkFolderName(int userIdx, String folderName) throws BaseException {
        try{
            int res = folderDao.checkFolderName(userIdx, folderName);
            return res;
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkFolder(int userIdx, int folderIdx) throws BaseException {
        try{
            int res = folderDao.checkFolder(userIdx, folderIdx);
            return res;
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
