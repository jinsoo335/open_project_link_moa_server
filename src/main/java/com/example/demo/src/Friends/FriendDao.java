package com.example.demo.src.Friends;

import com.example.demo.src.Friends.model.DeleteFriendRes;
import com.example.demo.src.Friends.model.GetFriendsRes;
import com.example.demo.src.Friends.model.PostCreateFriendRelationRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;


@Repository
public class FriendDao {

    private JdbcTemplate jdbcTemplate;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setJdbc(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 친구 관계 생성
     * @Param PostCreateFriendRelationReq
     * @return PostCreateFriendRelationRes
     *
     */
    public PostCreateFriendRelationRes postCreateFriendRelation(int userIdx,int sendUserIdx){
        String createFriendRelationQuery = "insert into Friends (firstUserIdx,secondUserIdx)VALUES (?,?);";
        Object[] createFriendRelationParams = new Object[]{
                userIdx,
                sendUserIdx
        };

        this.jdbcTemplate.update(createFriendRelationQuery,createFriendRelationParams);

//        String createFriendRelationResQuery = "select last_insert_id()";
//        int friendRelationIdx=this.jdbcTemplate.queryForObject(createFriendRelationResQuery,int.class);
//

        return new PostCreateFriendRelationRes(sendUserIdx);
    }


    /**
     * 친구 리스트 가져오기
     * @Param userIdx
     * @Return List<GetFriendsRes>
     *
     */
    public List<GetFriendsRes> getFriends(int userIdx){
        String getFriendsQuery = "select distinct userIdx,nickname,profileImageUrl\n" +
                "from Users,Friends\n" +
                "where (Users.userIdx=Friends.firstUserIdx or Users.userIdx=Friends.secondUserIdx) and (firstUserIdx=? or secondUserIdx=?) and userIdx!=?\n" +
                "order by userIdx ASC";
        Object[] getFriendsParams = new Object[]{
                userIdx,
                userIdx,
                userIdx

        };

        return this.jdbcTemplate.query(getFriendsQuery,
                (rs,rowNum)->new GetFriendsRes(
                        rs.getInt("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("profileImageUrl")
                ),getFriendsParams);
    }














    /**
     * 실제로 있는 친구와 친구를 하는지 확인.
     * 친구 메시지가 왔지만, 그 사이 그 send한 친구가 삭제되면 친구관계가 성립될 수 없음.
     * userIdx와 firstUserIdx, sendUserIdx를 비교하여 확인
     * return 존재 시 1, 없으면 0
     */
    public int checkFriend(int sendUserIdx){
        String checkFriendQuery = "select exists(select userIdx from Users where userIdx=?);\n";

        Object[] checkFriendParamsSend = {
                sendUserIdx
        };
        return this.jdbcTemplate.queryForObject(checkFriendQuery,int.class,checkFriendParamsSend);


    }

    /**
     * 이미 친구인지 확인
     * return 존재시 1, 없으면 0
     */
    public int checkFriendAlready(int userIdx,int sendUserIdx){
        String checkFriendAlreadyQueryInFirst = "select exists(\n" +
                "    select firstUserIdx\n" +
                "    from Friends\n" +
                "    where firstUserIdx=? and secondUserIdx=?\n" +
                "           )";
        Object[] checkFriendAlreadyParamsInFirst ={
                sendUserIdx,
                userIdx
        };


        String checkFriendAlreadyQueryInSecond = "select exists(\n" +
                "    select secondUserIdx\n" +
                "    from Friends\n" +
                "    where secondUserIdx=? and firstUserIdx=?\n" +
                "           )";
        Object[] checkFriendAlreadyParamsInSecond ={
                sendUserIdx,
                userIdx
        };



        if(this.jdbcTemplate.queryForObject(checkFriendAlreadyQueryInFirst,int.class,checkFriendAlreadyParamsInFirst)==1
        || this.jdbcTemplate.queryForObject(checkFriendAlreadyQueryInSecond,int.class,checkFriendAlreadyParamsInSecond)==1){
            return 1;
        }
        else
            return 0;
    }

    /**
     * 친구 삭제 api
     * @Param userIdx,deleteFriendIdx
     * @Return deletedFriendIdx
     */
    public DeleteFriendRes deleteFriend(int userIdx,int deleteFriendIdx){
        String deleteFriendQuery = "delete from Friends\n" +
                "where ( firstUserIdx=? and secondUserIdx = ? ) or ( firstUserIdx=? and secondUserIdx = ? )";
        Object[] deleteFriendParams = {
          userIdx,
          deleteFriendIdx,
          deleteFriendIdx,
          userIdx
        };
        this.jdbcTemplate.update(deleteFriendQuery,deleteFriendParams);

        return new DeleteFriendRes(deleteFriendIdx);
    }

}
