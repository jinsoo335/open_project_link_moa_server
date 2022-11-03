package com.example.demo.src.Folders;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.security.JwtTool;
import com.example.demo.src.Folders.model.PatchFolderReq;
import com.example.demo.src.Folders.model.PatchFolderRes;
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

        if(folderProvider.checkFolderName(userIdx, postCreateFolderReq.getFolderName()) == 1){
            throw new BaseException(FOLDERS_EXIST_FOLDER_NAME);
        }

        try{
            PostCreateFolderRes postCreateFolderRes = folderDao.postCreateFolder(userIdx, postCreateFolderReq);
            return postCreateFolderRes;

        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PatchFolderRes modifyFolderName(PatchFolderReq patchFolderReq) throws BaseException{
        int userIdx = JwtTool.getUserIdx();

        // 해당 폴더가 존재하는지 확인
        if(folderProvider.checkFolder(userIdx, patchFolderReq.getFolderIdx()) == 0){
            throw new BaseException(FOLDERS_NOT_EXIST_FOLDER);
        }

        // 다른 폴더 중에서 변경하고자 하는 이름을 가진 폴더가 있는지 확인
        if(folderProvider.checkFolderName(userIdx, patchFolderReq.getUpdateFolderName()) == 1){
            throw new BaseException(FOLDERS_EXIST_FOLDER_NAME);
        }

        try{
            PatchFolderRes patchFolderRes = folderDao.modifyFolderName(patchFolderReq);
            return patchFolderRes;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }



    }
}
