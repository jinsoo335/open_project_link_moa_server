//package com.example.demo.src.Friends;
//
//import com.example.demo.config.BaseResponse;
//import com.example.demo.src.Friends.model.PostCreateFriendRelationReq;
//import com.example.demo.src.Friends.model.PostCreateFriendRelationRes;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.sql.DataSource;
//
//
//@Repository
//public class FriendDao {
//
//    private JdbcTemplate jdbcTemplate;
//    final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    public void setJdbc(DataSource dataSource){
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//    }
//
//    /**
//     * 친구 관계 생성
//     * @Param PostCreateFriendRelationReq
//     * @return PostCreateFriendRelationRes
//     *
//     */
//    public PostCreateFriendRelationRes postCreateFriendRelationRes(PostCreateFriendRelationReq postCreateFriendRelationReq){
//        String createQuery = ""
//    }
//}
