package com.example.demo.src.Folders;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.security.JwtTool;
import com.example.demo.src.Folders.model.PostCreateFolderReq;
import com.example.demo.src.Folders.model.PostCreateFolderRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderService {

    private final FolderDao folderDao;

    @Autowired
    public FolderService(FolderDao folderDao) {
        this.folderDao = folderDao;
    }

    public PostCreateFolderRes postCreateFolder(PostCreateFolderReq postCreateFolderReq) throws BaseException {
        try{
            int userIdx = JwtTool.getUserIdx();

            PostCreateFolderRes postCreateFolderRes = folderDao.postCreateFolder(userIdx, postCreateFolderReq);
            return postCreateFolderRes;

        } catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
