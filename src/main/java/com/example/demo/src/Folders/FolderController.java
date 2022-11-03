package com.example.demo.src.Folders;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Folders.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/folders")
public class FolderController {

    private final FolderProvider folderProvider;
    private final FolderService folderService;

    @Autowired
    public FolderController(FolderProvider folderProvider, FolderService folderService) {
        this.folderProvider = folderProvider;
        this.folderService = folderService;
    }


    /**
     * 사용자의 폴더들을 가져오는 API
     * @return List<GetFolderRes>
     */
    @GetMapping("")
    public BaseResponse<List<GetFolderRes>> getFolders(){
        try{
            List<GetFolderRes> getFolderRes = folderProvider.getFolders();
            return new BaseResponse<>(getFolderRes);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    /**
     * 폴더를 추가하는 API
     * 폴더 이름 중복 불가
     * @param param
     * @return PostCreateFolderRes
     */
    @PostMapping("/create")
    public BaseResponse<PostCreateFolderRes> postCreateFolders(@RequestBody Map<String, String> param){
        try{
            PostCreateFolderReq postCreateFolderReq = new PostCreateFolderReq(param.get("folderName"));

            if(postCreateFolderReq.getFolderName() == null){
                return new BaseResponse<>(FOLDERS_EMPTY_FOLDER_NAME);
            }

            if(postCreateFolderReq.getFolderName().length() > 20 || postCreateFolderReq.getFolderName().length() == 0){
                return new BaseResponse<>(FOLDERS_UNABLE_LENGTH_FOLDER_NAME);
            }

            if(!postCreateFolderReq.getFolderName().matches("\"^([ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9,. ])*$\"")){
                return new BaseResponse<>(FOLDERS_UNABLE_WORD_FOLDER_NAME);
            }

            PostCreateFolderRes postCreateFolderRes = folderService.postCreateFolder(postCreateFolderReq);
            return new BaseResponse<>(postCreateFolderRes);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    /**
     * 폴더 이름 수정 API
     * @param patchFolderReq
     * @return PatchFolderRes
     */
    @PatchMapping("/modify")
    public BaseResponse<PatchFolderRes> modifyFolderName(@RequestBody PatchFolderReq patchFolderReq){
        try{
            if(patchFolderReq.getUpdateFolderName().length() > 20 || patchFolderReq.getUpdateFolderName().length() == 0){
                return new BaseResponse<>(FOLDERS_UNABLE_LENGTH_FOLDER_NAME);
            }
            if(!patchFolderReq.getUpdateFolderName().matches("^([ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9,. ])*$")){
                return new BaseResponse<>(FOLDERS_UNABLE_WORD_FOLDER_NAME);
            }

            PatchFolderRes patchFolderRes = folderService.modifyFolderName(patchFolderReq);
            return new BaseResponse<>(patchFolderRes);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 폴더 삭제 API
     * @param folderIdx
     * @return folderIdx + "번 폴더를 삭제했습니다."
     */
    @DeleteMapping("/delete/{folderIdx}")
    public BaseResponse<String> deleteFolder(@PathVariable ("folderIdx") int folderIdx){
        try{
            DeleteFolderRes deleteFolderRes = folderService.deleteFolder(folderIdx);
            return new BaseResponse<>(folderIdx + "번 폴더를 삭제했습니다.");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    /**
     * 폴더 복사 api
     * 폴더 번호와 받는 사람 번호를 가지고 폴더를 복사한다.
     * LinkFolder에서는 알림을 보내고 이를 수락할 때 폴더가 복사된다.
     * 받는 사람이 수락 버튼을 눌렀을 때 호출될 api
     * @param postCopyFolderReq
     * @return
     */
    @PostMapping("/copy")
    public BaseResponse<PostCopyFolderRes> copyFolder(@RequestBody PostCopyFolderReq postCopyFolderReq){
        try{
            PostCopyFolderRes postCopyFolderRes = folderService.copyFolder(postCopyFolderReq);
            return new BaseResponse<>(postCopyFolderRes);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }



}
