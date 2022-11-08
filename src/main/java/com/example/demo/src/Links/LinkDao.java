package com.example.demo.src.Links;


import com.example.demo.src.Links.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class LinkDao {

    private JdbcTemplate jdbcTemplate;
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    public void setJdbc(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetLinkRes> getLinks(int folderIdx) {
        String getLinkQuery = "select linkUrl, linkIdx, linkAlias from Links where folderIdx = ?;";
        int getLinkParams = folderIdx;

        return this.jdbcTemplate.query(getLinkQuery,
                (rs, rowNum) -> new GetLinkRes(
                        rs.getString("linkUrl"),
                        rs.getInt("linkIdx"),
                        rs.getString("linkAlias")
                ),
                getLinkParams);


    }

    public PostCreateLinkRes postCreateLink(PostCreateLinkReq postCreateLinkReq) {
        String createQuery = "insert into Links (linkUrl,folderIdx,linkAlias) VALUES (?,?,?);";
        Object[] createParams = new Object[]{
                postCreateLinkReq.getLinkUrl(),
                postCreateLinkReq.getFolderIdx(),
                postCreateLinkReq.getLinkAlias(),

        };

        this.jdbcTemplate.update(createQuery, createParams);


        String createResQuery = "select last_insert_id();";
        int linkIdx = this.jdbcTemplate.queryForObject(createResQuery, int.class);
        return new PostCreateLinkRes(linkIdx);


    }
    /**
     * 링크 이름 수정
     * @param patchLinkReq
     * @return 업데이트 한 link의 linkIdx
     */
    public PatchLinkRes modifyLink(PatchLinkReq patchLinkReq) {
        String modifyLinkQuery = "update Links\n" +
                "set linkUrl= ?,linkAlias = ?, updatedAt = current_timestamp\n" +
                "where linkIdx = ?;";
        Object[] modifyLinkParams = {
                patchLinkReq.getUpdateLinkUrl(),
                patchLinkReq.getUpdateLinkAlias(),
                patchLinkReq.getLinkIdx()


        };

        this.jdbcTemplate.update(modifyLinkQuery, modifyLinkParams);


        return new PatchLinkRes(patchLinkReq.getLinkIdx());
    }

    /**
     * 해당 링크가 존재하는지 확인
     * linkIdx 값과 linkIdx 값을 가지고 확인
     * @param linkIdx
     * @return 존재하면 1, 없으면 0
     */
    public int checkLink(int linkIdx) {
        String checkLinkQuery = "select exists(select linkIdx from Links where linkIdx = ?);\n";
        Object[] checkLinkParams= {
                linkIdx
        };

        return this.jdbcTemplate.queryForObject(checkLinkQuery, int.class, checkLinkParams);
    }

    /**
     * 해당 링크가 존재하는지 확인
     * linkName과 linkIdx 값으로 확인
     * @param linkAlias
     * @return 존재하면 1, 없으면 0
     */
    public int checkLinkAlias(String linkAlias) {
        String checkLinkQuery = "select exists(select linkIdx from Links where linkAlias = ?);\n";
        Object[] checkLinkParams= {
                linkAlias
        };

        return this.jdbcTemplate.queryForObject(checkLinkQuery, int.class, checkLinkParams);
    }

    /**
     * 링크 삭제 api
     * @param linkIdx
     * @return DeleteLinkRes
     */
    public DeleteLinkRes deleteLink(int linkIdx) {
        String deleteFolderQuery = "delete from Links\n" +
                "where linkIdx = ?;";
        int deleteFolderParam = linkIdx;

        this.jdbcTemplate.update(deleteFolderQuery, deleteFolderParam);

        return new DeleteLinkRes(linkIdx);
    }
}
