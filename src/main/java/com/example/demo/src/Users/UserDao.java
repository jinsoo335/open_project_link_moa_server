package com.example.demo.src.Users;

import com.example.demo.src.Users.model.PostCreateUserReq;
import com.example.demo.src.Users.model.PostCreateUserRes;
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

        jdbcTemplate.update(createQuery, createParams);


        String createResQuery = "select last_insert_id();";
        int userIdx = jdbcTemplate.queryForObject(createResQuery, int.class);
        return new PostCreateUserRes(userIdx);

    }
}
