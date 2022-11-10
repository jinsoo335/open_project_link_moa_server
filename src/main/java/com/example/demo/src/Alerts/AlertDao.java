package com.example.demo.src.Alerts;

import com.example.demo.src.Alerts.model.GetAlertRes;
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
                "    ifnull(linkIdx, 0) as linkIdx\n" +
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
                        rs.getInt("linkIdx")
                ), getAlertParam);
    }

    public int checkAlert(int userIdx) {
        String checkAlertQuery = "select exists(select alertIdx from Alerts where receiveUserIdx = ?);";
        int checkAlertParam = userIdx;

        return this.jdbcTemplate.queryForObject(checkAlertQuery, int.class, checkAlertParam);
    }
}
