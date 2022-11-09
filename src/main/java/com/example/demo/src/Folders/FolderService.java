package com.example.demo.src.Folders;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.security.JwtTool;
import com.example.demo.src.Folders.model.*;
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

    public DeleteFolderRes deleteFolder(int folderIdx) throws BaseException {
        int userIdx = JwtTool.getUserIdx();

        // 해당 폴더가 존재하는지 확인
        if(folderProvider.checkFolder(userIdx, folderIdx) == 0){
            throw new BaseException(FOLDERS_NOT_EXIST_FOLDER);
        }

        DeleteFolderRes deleteFolderRes;

        try{
            deleteFolderRes = folderDao.deleteFolder(folderIdx);
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }

        // 삭제 후, 해당 폴더가 존재하는지 확인
        if(folderProvider.checkFolder(userIdx, deleteFolderRes.getFolderIdx()) == 1){
            throw new BaseException(FOLDERS_DELETE_FAILED);
        }

        return deleteFolderRes;

    }

    public PostCopyFolderRes copyFolder(PostCopyFolderReq postCopyFolderReq) throws BaseException{
        int userIdx = JwtTool.getUserIdx();

        // 해당 폴더가 존재하는지 확인
        if(folderProvider.checkFolder(postCopyFolderReq.getSendUserIdx(), postCopyFolderReq.getFolderIdx()) == 0){
            throw new BaseException(FOLDERS_NOT_EXIST_FOLDER);
        }

        // 복사하고자 하는 폴더와 동일한 이름의 폴더가 존재하는지 확인
        // 있다면 뒤에 ...(1) ...(2) 이런식으로 숫자 붙여주기
        String folderName = folderProvider.getFolderName(postCopyFolderReq.getFolderIdx());
        int i = 1;
        StringBuffer sb = new StringBuffer(folderName);
        while (true){
            if(folderProvider.checkFolderName(userIdx, sb.toString()) == 0){
                folderName = sb.toString();
                break;
            }
            if(folderName.length() > 20){
                throw new BaseException(FOLDERS_DUPLICATE_FOLDER_NAME);
            }
            if(i > 1){
                int digit = String.valueOf(i).length();
                sb.replace(sb.length() - 1 - digit, sb.length() - digit, String.valueOf(i));
            }else{
                sb.append("(1)");
            }
            i++;
        }

        try{
            PostCopyFolderRes postCopyFolderRes = folderDao.copyFolder(postCopyFolderReq, folderName, userIdx);
            return postCopyFolderRes;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
