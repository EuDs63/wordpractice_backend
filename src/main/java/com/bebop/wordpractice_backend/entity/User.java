package com.bebop.wordpractice_backend.entity;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class User {

    private Long id;

    private String email;

    private String password;

    private String name;

    private long createdAt;
    /**
     * 用来记录用户所选的考试类型。1对应CET-4
     */
    private Integer level;

    /**
     * 用来记录用户是否为管理员
     */
    private Boolean isAdmin;

    public User() {
    }

    /*get and set*/
    public Long getId() {
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt){
        this.createdAt=createdAt;
    }

    public String getCreatedDateTime(){
        return Instant.ofEpochMilli(this.createdAt).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(Integer level)
    {
        this.level=level;
    }

    public Integer getLevel()
    {
        return this.level;
    }

    public void setIsAdmin(Boolean isAdmin)
    {
        this.isAdmin = isAdmin;
    }

    public Boolean getIsAdmin()
    {
        return this.isAdmin;
    }


    @Override
    public String toString() {
        return String.format("User[id=%s, email=%s, name=%s, password=%s, createdAt=%s, createdDateTime=%s]", getId(), getEmail(), getName(), getPassword(),
                getCreatedAt(), getCreatedDateTime());
    }

}
