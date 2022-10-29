package com.example.demo.src.Folders;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Folders.model.GetFolderRes;
import com.example.demo.src.Folders.model.PostCreateFolderReq;
import com.example.demo.src.Folders.model.PostCreateFolderRes;
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


    @GetMapping("")
    public BaseResponse<List<GetFolderRes>> getFolders(){
        try{
            List<GetFolderRes> getFolderRes = folderProvider.getFolders();
            return new BaseResponse<>(getFolderRes);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    @PostMapping("/create")
    public BaseResponse<PostCreateFolderRes> postCreateFolders(@RequestBody Map<String, String> param){
        try{
            PostCreateFolderReq postCreateFolderReq = new PostCreateFolderReq(param.get("folderName"));

            if(postCreateFolderReq.getFolderName() == null){
                return new BaseResponse<>(FOLDERS_EMPTY_FOLDER_NAME);
            }

            if(postCreateFolderReq.getFolderName().length() > 20 && postCreateFolderReq.getFolderName().length() == 0){
                return new BaseResponse<>(FOLDERS_UNABLE_LENGTH_FOLDER_NAME);
            }

            if(postCreateFolderReq.getFolderName().matches("^[a-zA-Z0-9]*$")){
                return new BaseResponse<>(FOLDERS_UNABLE_WORD_FOLDER_NAME);
            }

            PostCreateFolderRes postCreateFolderRes = folderService.postCreateFolder(postCreateFolderReq);
            return new BaseResponse<>(postCreateFolderRes);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

}
