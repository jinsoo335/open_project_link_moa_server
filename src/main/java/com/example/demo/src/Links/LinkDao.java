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

    public PostCreateLinkRes postCreateLink(int userIdx,PostCreateLinkReq postCreateLinkReq) {
        String createQuery = "insert into Links (linkUrl,folderIdx,linkAlias) VALUES (?,?,?);";
        Object[] createParams = new Object[]{
                postCreateLinkReq.getLinkUrl(),
                postCreateLinkReq.getFolderIdx(),
                postCreateLinkReq.getLinkAlias(),

        };

        this.jdbcTemplate.update(createQuery, createParams);

        // folderIdx - userIdx 이용 체크


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
     * 해당 폴더가 유저의 것인지 확인
     * linkIdx 값과 folderIdx 값을 가지고 확인
     * @param userIdx,linkIdx
     * @return 존재하면 1, 없으면 0
     */
    public int checkFolderUser(int userIdx,int folderIdx) {
        String checkLinkQuery = "select exists(select folderIdx from Folders where folderIdx =? and ownerUserIdx = ?)" ;
        Object[] checkLinkParams= {
                folderIdx,
                userIdx

        };

        return this.jdbcTemplate.queryForObject(checkLinkQuery, int.class, checkLinkParams);
    }

    /**
     * 해당 링크별칭이 존재하는지 확인
     * 다만, 같은 폴더 일경우 까지..
     * linkName과 linkIdx 값으로 확인
     * @param linkAlias
     * @return 존재하면 1, 없으면 0
     */
    public int checkLinkAlias(int userIdx,int folderIdx, String linkAlias) {
        String checkLinkQuery = "select exists(select  linkIdx from Links,Users,Folders\n" +
                "                                where (Links.folderIdx = Folders.folderIdx and Folders.ownerUserIdx = Users.userIdx) and (ownerUserIdx=? and linkAlias=? and Folders.folderIdx=?)  )";
        Object[] checkLinkParams= {
                userIdx,
                linkAlias,
                folderIdx

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

    /**
     * 해당 링크가 유저의 것인지 확인
     * linkIdx 값과 linkIdx 값을 가지고 확인
     * @param userIdx,linkIdx
     * @return 존재하면 1, 없으면 0
     */
    public int checkLinkUser(int userIdx,int linkIdx) {
        String checkLinkQuery = "select exists(\n" +
                "select Links.linkIdx\n" +
                "from Links,Folders\n" +
                "where Links.folderIdx=Folders.folderIdx and Links.linkIdx = ? and Folders.ownerUserIdx = ?)" ;
        Object[] checkLinkParams= {
                linkIdx,
                userIdx

        };

        return this.jdbcTemplate.queryForObject(checkLinkQuery, int.class, checkLinkParams);
    }

    /**
     * 링크의 이름 받아오는 함수
     * 링크 사본 생성하는 데 이용됨.
     */
    public String getLinkAlias(int linkIdx){
        String getLinkAliasQuery = "select linkAlias\n" +
                "from Links\n" +
                "where linkIdx = ?";
        int getLinkAliasParam = linkIdx;

        return this.jdbcTemplate.queryForObject(getLinkAliasQuery, String.class,getLinkAliasParam);
    }

    /**
     * 링크의 url 받아오는 함수
     * 링크 사본 생성하는 데 이용됨.
     */
    public String getLinkUrl(int linkIdx){
        String getLinkAliasQuery = "select linkUrl\n" +
                "from Links\n" +
                "where linkIdx = ?";
        int getLinkAliasParam = linkIdx;

        return this.jdbcTemplate.queryForObject(getLinkAliasQuery, String.class,getLinkAliasParam);
    }



    /**
     * 링크 복사API
     * 전달받은 링크의 IDX로 복사할 링크를 찾아
     * 전달받은 receeiveUserIdx에 해당하는 사용자에 복사 링크를 추가
     *
     */
    public PostCopyLinkRes copyLink(PostCopyLinkReq postCopyLinkReq, String linkAlias,String linkUrl){

        //링크 이름 복사 부분
        String copyLinkQuery = "insert into Links (linkUrl,folderIdx,linkAlias) VALUES (?,?,?)";
        Object[] copyLinkParams={
                linkUrl,
                postCopyLinkReq.getFolderIdx(),
                linkAlias
        };
        this.jdbcTemplate.update(copyLinkQuery,copyLinkParams);

        // 생성한 링크 (copy된)의 idx값 가져오기
        String lastCreateLinkQuery = "select last_insert_id()";
        int linkIdx  = this.jdbcTemplate.queryForObject(lastCreateLinkQuery,int.class);



        return new PostCopyLinkRes(linkIdx);
    }

}
