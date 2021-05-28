package com.example.KLF.server.service;

import com.example.KLF.server.domain.Report;
import com.example.KLF.server.domain.User;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.List;

public interface KlfService {


    int importUser(String name, String password, String email, String phone,Blob image);

    User searchUserByName(String name);

    User searchUser(String name, String password);

    int importUserActivity(Integer activity_id, Integer user_id, Timestamp occurrence);

    int updateUser(String name, String password, String email, String phone,Blob image,String preName,String prePassword);

    List<Report> getReport();

}
