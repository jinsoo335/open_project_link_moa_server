package com.example.demo.src.Folders;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Folders.model.GetFolderRes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/folders")
public class FolderController {

    private final FolderProvider folderProvider;
    private final FolderService folderService;

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

}
