package com.bebop.wordpractice_backend.entity;

/**
 * 父类，包含信息：用户id、字典、时间戳
 */
public class Record {
    //用户id
    private int userId;

    private int dictId;

    private Long createdAt;

    public Record()
    {

    }

    /*get and set*/
    public int getUserId() {
        return userId;
    }

    public void setUserId(int id){
         this.userId = id;
    }

    public int getDictId(){
        return dictId;
    }

    public void setDictId(int dictId){
        this.dictId = dictId;
    }

    public Long getCreatedAt(){
        return createdAt;
    }

    public void setCreatedAt(Long createdAt){
        this.createdAt = createdAt;
    }
}