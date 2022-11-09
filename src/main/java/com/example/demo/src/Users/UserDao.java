package com.example.demo.src.Users;

import com.example.demo.src.Users.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbc(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createUser(PostCreateUserReq postCreateUserReq) {
        String createQuery = "insert into Users (id, password, nickname, profileimageurl) values (?,?,?,?);";
        Object[] createParams = new Object[]{
                postCreateUserReq.getId(),
                postCreateUserReq.getPassword(),
                postCreateUserReq.getNickname(),
                postCreateUserReq.getProfileImageUrl()
        };

        this.jdbcTemplate.update(createQuery, createParams);


        String createResQuery = "select last_insert_id();";
        int userIdx = this.jdbcTemplate.queryForObject(createResQuery, int.class);
        return userIdx;

    }

    public int checkUserId(String id) {
        String checkIdQuery = "select exists(select Id from Users where Id = ?);";
        String checkIdParam = id;


        return this.jdbcTemplate.queryForObject(checkIdQuery, int.class, checkIdParam);
    }

    public int checkUserPassword(String id, String password) {
        String checkPasswordQuery = "select exists(select userIdx from Users where Id= ? and password = ?);";
        Object[] checkPasswordParams = new Object[]{id, password};

        return this.jdbcTemplate.queryForObject(checkPasswordQuery, int.class, checkPasswordParams);
    }

    public String getUserPassword(String id){
        String getPasswordQuery = "select password from Users where id = ?;";
        return this.jdbcTemplate.queryForObject(getPasswordQuery, String.class, id);
    }


    public Users loginUser(PostLoginReq postLoginReq) {
        String userLoginQuery = "select userIdx, id, nickname, profileImageUrl\n" +
                "from Users\n" +
                "where Id = ?;";
        Object[] userLoginParams = new Object[]{postLoginReq.getId()};

        return this.jdbcTemplate.queryForObject(userLoginQuery,
                (rs, rowNum) -> new Users(
                        rs.getInt("userIdx"),
                        rs.getString("id"),
                        rs.getString("nickname"),
                        rs.getString("profileImageUrl")
                ), userLoginParams);
    }

    public int checkUserNickname(String nickname) {
        String checkUserNicknameQuery = "select exists(select userIdx from Users where nickname like ?);";
        String checkUserNicknameParam = nickname;

        return this.jdbcTemplate.queryForObject(checkUserNicknameQuery, int.class, checkUserNicknameParam);
    }

    public List<GetUserRes> getUserNickname(String search) {
        String getUserQuery = "select userIdx, Id, nickname, profileImageUrl\n" +
                "from Users\n" +
                "where nickname = ?;";
        String getUserParams = search;

        return this.jdbcTemplate.query(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("id"),
                        rs.getString("nickname"),
                        rs.getString("profileImageUrl")
                ), getUserParams);
    }

    public GetUserRes getUserId(String search) {
        String getUserQuery = "select userIdx, Id, nickname, profileImageUrl\n" +
                "from Users\n" +
                "where Id = ?;";
        String getUserParams = search;


        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("id"),
                        rs.getString("nickname"),
                        rs.getString("profileImageUrl")
                ), getUserParams);
    }

    public int checkUserIdx(int userIdx) {
        String checkUserQuery = "select exists(select userIdx from Users where userIdx = ?);";
        int checkUserParam = userIdx;

        return this.jdbcTemplate.queryForObject(checkUserQuery, int.class, checkUserParam);
    }

    public PatchUserRes modifyUser(int userIdx, PatchUserReq patchUserReq) {
        String modifyUserQuery = "update Users\n" +
                "set nickname = ?, profileImageUrl = ?, updatedAt = current_timestamp\n" +
                "where userIdx = ?;";
        Object[] modifyUserParams = {
                patchUserReq.getNickname(),
                patchUserReq.getProfileImageUrl(),
                userIdx
        };

        this.jdbcTemplate.update(modifyUserQuery, modifyUserParams);

        String lastUpdateUserQuery = "select userIdx from Users order by updatedAt desc limit 1;";
        return new PatchUserRes(this.jdbcTemplate.queryForObject(lastUpdateUserQuery, int.class));
    }

    public List<GetUserRes> getUser(String nameInfo) {
        String getUserQuery = "select userIdx, id, nickname, profileImageUrl\n" +
                "from Users\n" +
                "where nickname like ? or id like ?;";

        Object[] getUserParam = {
                nameInfo,
                nameInfo
        };

        return this.jdbcTemplate.query(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("id"),
                        rs.getString("nickname"),
                        rs.getString("profileImageUrl")
                ), getUserParam);
    }
}
