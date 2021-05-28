package com.example.KLF.server.service;
import com.example.KLF.server.domain.Report;
import com.example.KLF.server.domain.User;
import com.example.KLF.server.mapper.KlfMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.List;

@Service
public class KlfServiceImpl implements KlfService {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    KlfMapper mapper;

    @Override
    public int importUserActivity(Integer activity_id, Integer user_id, Timestamp occurrence) {
        return mapper.createUserActivity(activity_id,user_id,occurrence);
    }

    @Override
    public int importUser(String name, String password, String email, String phone,Blob image) {
        return mapper.createUser(name,password,email,phone,image);
    }

    @Override
    public List<Report> getReport() {
        return mapper.getReport();
    }

    @Override
    public int updateUser(String name, String password, String email, String phone, Blob image, String preName, String prePassword) {
        return mapper.updateUser(name,password,email,phone,image,preName,prePassword);
    }

    @Override
    public User searchUserByName(String name) {
        return mapper.searchUserByName(name);
    }

    @Override
    public User searchUser(String name, String password) {
        return mapper.searchUser(name,password);
    }

}
