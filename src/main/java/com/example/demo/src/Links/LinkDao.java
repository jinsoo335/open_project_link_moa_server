package com.example.demo.src.Links;


import com.example.demo.src.Links.model.GetLinkRes;
import com.example.demo.src.Links.model.PostCreateLinkReq;
import com.example.demo.src.Links.model.PostCreateLinkRes;

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

    public List<GetLinkRes> getLinks(int userIdx) {
        String getFolderQuery = "select linkIdx, linkName from Links where ownerUserIdx = ?;";
        int getLinkParams = userIdx;

        return this.jdbcTemplate.query(getFolderQuery,
                (rs, rowNum) -> new GetLinkRes(
                        rs.getInt("linkIdx"),
                        rs.getString("linkName")
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
}
