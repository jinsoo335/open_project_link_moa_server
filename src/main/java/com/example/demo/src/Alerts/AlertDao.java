package com.example.demo.src.Alerts;

import com.example.demo.src.Alerts.model.GetAlertRes;
import com.example.demo.src.Alerts.model.PostCreateAlertReq;
import com.example.demo.src.Alerts.model.PostCreateAlertRes;
import com.example.demo.src.Users.model.PatchUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class AlertDao {

    private JdbcTemplate jdbcTemplate;
    @Autowired
    public void setJdbc(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetAlertRes> getAlert(int userIdx) {
        String getAlertQuery = "select\n" +
                "    alertIdx,\n" +
                "    alertText,\n" +
                "    alertType,\n" +
                "    sendUserIdx,\n" +
                "    receiveUserIdx,\n" +
                "    ifnull(folderIdx, 0) as folderIdx,\n" +
                "    ifnull(linkIdx, 0) as linkIdx,\n" +
                "    (select U.nickname from Users U where Alerts.sendUserIdx = U.userIdx) as nickname,\n" +
                "    (select U.profileImageUrl from Users U where Alerts.sendUserIdx = U.userIdx) as profileImageUrl,\n" +
                "    case\n" +
                "        when Alerts.folderIdx is null then null\n" +
                "        ELSE (select F.folderName from Folders F where Alerts.folderIdx = F.folderIdx)\n" +
                "    END as folderName,\n" +
                "    case\n" +
                "        when Alerts.folderIdx is null then null\n" +
                "        when Alerts.linkIdx is null then null\n" +
                "        ELSE (select L.linkAlias from Links L where Alerts.linkIdx = L.linkIdx)\n" +
                "    END as linkName\n" +
                "from Alerts\n" +
                "where receiveUserIdx = ?\n" +
                "order by createdAt desc;";
        int getAlertParam = userIdx;

        return this.jdbcTemplate.query(getAlertQuery,
                (rs, rowNum) -> new GetAlertRes(
                        rs.getInt("alertIdx"),
                        rs.getString("alertText"),
                        rs.getInt("alertType"),
                        rs.getInt("sendUserIdx"),
                        rs.getInt("receiveUserIdx"),
                        rs.getInt("folderIdx"),
                        rs.getInt("linkIdx"),
                        rs.getString("nickname"),
                        rs.getString("profileImageUrl"),
                        rs.getString("folderName"),
                        rs.getString("linkName")
                ), getAlertParam);
    }

    public int checkAlert(int userIdx, int alertIdx) {
        String checkAlertQuery = "select exists(select alertIdx from Alerts where receiveUserIdx = ? and alertIdx = ?);";
        Object[] checkAlertParam = new Object[]{
                userIdx,
                alertIdx
        };

        return this.jdbcTemplate.queryForObject(checkAlertQuery, int.class, checkAlertParam);
    }

    public void deleteAlert(int userIdx, int alertIdx) {
        String deleteAlertQuery = "delete from Alerts where alertIdx = ? and receiveUserIdx = ?;";
        Object[] deleteAlertParams = new Object[]{
                alertIdx,
                userIdx
        };

        this.jdbcTemplate.update(deleteAlertQuery, deleteAlertParams);

    }

    public PostCreateAlertRes createAlert(int sendUserIdx, PostCreateAlertReq postCreateAlertReq) {
        String createAlertQuery = "insert into Alerts (receiveUserIdx, folderIdx, alertText, sendUserIdx, alertType, linkIdx) \n" +
                "VALUES (?,?,?,?,?,?)";

        Object folderIdx = null;
        Object linkIdx = null;

        if(postCreateAlertReq.getFolderIdx() != 0){
            folderIdx = postCreateAlertReq.getFolderIdx();
        }

        if(postCreateAlertReq.getLinkIdx() != 0){
            linkIdx = postCreateAlertReq.getLinkIdx();
        }


        Object[] createAlertParams = new Object[]{
                postCreateAlertReq.getReceiveUserIdx(),
                folderIdx,
                postCreateAlertReq.getAlertText(),
                sendUserIdx,
                postCreateAlertReq.getAlertType(),
                linkIdx
        };

        this.jdbcTemplate.update(createAlertQuery, createAlertParams);

        String lastUpdateUserQuery = "select alertIdx from Alerts order by createdAt desc limit 1;";
        return new PostCreateAlertRes(this.jdbcTemplate.queryForObject(lastUpdateUserQuery, int.class));
    }
}
