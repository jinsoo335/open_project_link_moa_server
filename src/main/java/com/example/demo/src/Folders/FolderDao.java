package com.example.demo.src.Folders;

import com.example.demo.src.Folders.model.GetFolderRes;
import com.example.demo.src.Folders.model.PostCreateFolderReq;
import com.example.demo.src.Folders.model.PostCreateFolderRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class FolderDao {

    private JdbcTemplate jdbcTemplate;
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    public void setJdbc(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

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
}
