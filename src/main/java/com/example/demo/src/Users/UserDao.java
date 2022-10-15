package com.example.demo.src.Users;

import com.example.demo.src.Users.model.PostCreateUserReq;
import com.example.demo.src.Users.model.PostCreateUserRes;
import com.example.demo.src.Users.model.PostLoginReq;
import com.example.demo.src.Users.model.PostLoginRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbc(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public PostCreateUserRes createUser(PostCreateUserReq postCreateUserReq) {
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
        return new PostCreateUserRes(userIdx);

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

    public PostLoginRes loginUser(PostLoginReq postLoginReq) {
        String userLoginQuery = "select userIdx, id, nickname, profileImageUrl\n" +
                "from Users\n" +
                "where Id = ?;";
        Object[] userLoginParams = new Object[]{postLoginReq.getId()};

        return this.jdbcTemplate.queryForObject(userLoginQuery,
                (rs, rowNum) -> new PostLoginRes(
                        rs.getInt("userIdx"),
                        rs.getString("id"),
                        rs.getString("nickname"),
                        rs.getString("profileImageUrl")
                ), userLoginParams);
    }
}
