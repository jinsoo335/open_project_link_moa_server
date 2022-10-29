package com.example.demo.src.Folders;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.security.JwtTool;
import com.example.demo.src.Folders.model.PostCreateFolderReq;
import com.example.demo.src.Folders.model.PostCreateFolderRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class FolderService {

    private final FolderDao folderDao;
    private final FolderProvider folderProvider;


    @Autowired
    public FolderService(FolderDao folderDao, FolderProvider folderProvider) {
        this.folderDao = folderDao;
        this.folderProvider = folderProvider;
    }

    public PostCreateFolderRes postCreateFolder(PostCreateFolderReq postCreateFolderReq) throws BaseException {
        int userIdx = JwtTool.getUserIdx();

        if(folderProvider.checkFolder(userIdx, postCreateFolderReq) == 1){
            throw new BaseException(FOLDERS_EXIST_FOLDER_NAME);
        }

        try{
            PostCreateFolderRes postCreateFolderRes = folderDao.postCreateFolder(userIdx, postCreateFolderReq);
            return postCreateFolderRes;

        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
