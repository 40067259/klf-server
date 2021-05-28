package com.example.KLF.server.mapper;

import com.example.KLF.server.domain.Report;
import com.example.KLF.server.domain.User;
import org.apache.ibatis.annotations.*;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface KlfMapper {

    @Insert("insert into user(name,password,email,phone,image)"+
            "values(#{name},#{password},#{email},#{phone},#{image})")
    public int createUser(String name, String password, String email,
                          String phone,Blob image);
    @Insert("insert into user_activity(activity_id,user_id,occurrence)"+
            "values(#{activity_id},#{user_id},#{occurrence})")
   public int createUserActivity(Integer activity_id, Integer user_id, Timestamp occurrence);

    @Select("select * from user where name = #{name} AND password = #{password}")
    public User searchUser(String name, String password);

    @Select("select * from user where name = #{name}")
    User searchUserByName(String name);

   @Select("SELECT user.name as user_name, activity.name as activity_name, COUNT(*) as amount," +
           "MIN(user_activity.occurrence) as first_occurrence, MAX(user_activity.occurrence) as " +
           "last_occurrence FROM user_activity INNER JOIN user ON user_activity.user_id=user.id " +
           "INNER JOIN activity ON user_activity.activity_id=activity.id GROUP BY " +
           "user_activity.activity_id, user_activity.user_id;")
   public List<Report> getReport();

   @Update("update user Set name=#{name},password=#{password},phone=#{phone}," +
         "email=#{email},image=#{image} where name=#{preName} AND password =#{prePassword}")
   public int updateUser(String name, String password, String email,
                       String phone,Blob image,String preName,String prePassword);
}
