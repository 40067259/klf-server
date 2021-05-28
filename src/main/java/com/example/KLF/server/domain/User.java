package com.example.KLF.server.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.security.SecureRandom;
import java.sql.Blob;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable {
    @XmlAttribute
    private static final int TOKEN_LENGTH = 20;
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String password;
    @XmlAttribute
    private String email;
    @XmlAttribute
    private String phone;
    @XmlAttribute
    public byte[] image;
    @XmlAttribute
    private Integer id;

    private String token;

    public User(String name, String password, String email,String phone,byte[] image){
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.image = image;
    }
    public User(Integer id,String name, String password, String email,String phone,byte[] image){
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.image = image;
    }
    public User(){}
    public User(String username, String password){
        this.name = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername(){
        return this.name;
    }

    public String getPassword(){
        return this.password;
    }

    public String getToken(){
        return this.token;
    }

    public void setUsername(String username){
        this.name = username;
    }


    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", id=" + id +
                '}';
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void generateToken(){
        String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        String NUMBER = "0123456789";
        String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;

        StringBuilder sb = new StringBuilder(TOKEN_LENGTH);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            // 0-62 (exclusive), random returns 0-61
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            sb.append(rndChar);
        }
        this.token = sb.toString();
    }

    public void destroyToken(){
        this.token = "";
    }

}