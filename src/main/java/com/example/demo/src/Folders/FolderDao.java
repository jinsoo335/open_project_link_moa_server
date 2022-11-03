package com.example.demo.src.Folders;

import com.example.demo.config.BaseException;
import com.example.demo.src.Folders.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.FOLDERS_NOT_EXIST_FOLDER;

@Repository
public class FolderDao {

    private JdbcTemplate jdbcTemplate;
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    public void setJdbc(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 폴더 리스트 가져오기
     * @param userIdx
     * @return List<GetFolderRes>
     */
    public List<GetFolderRes> getFolders(int userIdx) {
        String getFolderQuery = "select folderIdx, folderName from Folders where ownerUserIdx = ?;";
        int getFolderParams = userIdx;

        return this.jdbcTemplate.query(getFolderQuery,
                (rs, rowNum) -> new GetFolderRes(
                        rs.getInt("folderIdx"),
                        rs.getString("folderName")
                ),
                getFolderParams);
    }

    /**
     * 폴더 생성
     * @param userIdx
     * @param postCreateFolderReq
     * @return 생성한 folder의 folderIdx
     */
    public PostCreateFolderRes postCreateFolder(int userIdx, PostCreateFolderReq postCreateFolderReq) {
        String createQuery = "insert into Folders (folderName, ownerUserIdx) VALUES (?,?);";
        Object[] createParams = new Object[]{
                postCreateFolderReq.getFolderName(),
                userIdx
        };

        this.jdbcTemplate.update(createQuery, createParams);


        String createResQuery = "select last_insert_id();";
        int folderIdx = this.jdbcTemplate.queryForObject(createResQuery, int.class);
        return new PostCreateFolderRes(folderIdx);


    }

    /**
     * 해당 폴더가 존재하는지 확인
     * folderIdx 값과 userIdx 값을 가지고 확인
     * @param userIdx
     * @param folderIdx
     * @return 존재하면 1, 없으면 0
     */
    public int checkFolder(int userIdx, int folderIdx) {
        String checkFolderQuery = "select exists(select folderIdx from Folders where ownerUserIdx = ? and folderIdx = ?);\n";
        Object[] checkFolderParams= {
                userIdx,
                folderIdx
        };

        return this.jdbcTemplate.queryForObject(checkFolderQuery, int.class, checkFolderParams);
    }

    /**
     * 해당 폴더가 존재하는지 확인
     * folderName과 folderIdx 값으로 확인
     * @param userIdx
     * @param folderName
     * @return 존재하면 1, 없으면 0
     */
    public int checkFolderName(int userIdx, String folderName) {
        String checkFolderQuery = "select exists(select folderIdx from Folders where ownerUserIdx = ? and folderName = ?);\n";
        Object[] checkFolderParams= {
                userIdx,
                folderName
        };

        return this.jdbcTemplate.queryForObject(checkFolderQuery, int.class, checkFolderParams);
    }


    /**
     * 폴더 이름 수정
     * @param patchFolderReq
     * @return 업데이트 한 folder의 folderIdx
     */
    public PatchFolderRes modifyFolderName(PatchFolderReq patchFolderReq) {
        String modifyFolderNameQuery = "update Folders\n" +
                "set folderName = ?, updatedAt = current_timestamp\n" +
                "where folderIdx = ?;";
        Object[] modifyFolderNameParams = {
                patchFolderReq.getUpdateFolderName(),
                patchFolderReq.getFolderIdx()
        };

        this.jdbcTemplate.update(modifyFolderNameQuery, modifyFolderNameParams);


        String lastUpdateFolderQuery = "select folderIdx\n" +
                "from Folders\n" +
                "order by updatedAt desc limit 1;";

        int lastUpdateFolderIdx = this.jdbcTemplate.queryForObject(lastUpdateFolderQuery, int.class);
        return new PatchFolderRes(lastUpdateFolderIdx);
    }

    public DeleteFolderRes deleteFolder(int folderIdx) {
        String deleteFolderQuery = "delete from Folders\n" +
                "where folderIdx = ?;";
        int deleteFolderParam = folderIdx;

        this.jdbcTemplate.update(deleteFolderQuery, deleteFolderParam);

        return new DeleteFolderRes(folderIdx);
    }
}
