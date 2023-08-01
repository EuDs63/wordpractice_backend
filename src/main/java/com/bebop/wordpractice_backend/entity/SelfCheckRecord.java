package com.bebop.wordpractice_backend.entity;

import java.util.List;

/**
 * Record的子类，包含信息：
 * 用户id、字典、时间戳、
 * 自检id(第几次练习)、所用时间、
 * 认识单词索引List<Integer>，模糊单词索引List<Integer>，不认识单词索引List<Integer>；
 */
public class SelfCheckRecord extends Record{
    //自检id(第几次练习)
    private Long selfcheckrecordId;

    //所用时间,单位为秒
    private int timeUsed;

    //认识单词索引,指的是认识的单词在字典中的索引
    private List<Integer> knownWordIndexes;
    //模糊单词索引,指的是模糊的单词在字典中的索引
    private List<Integer> vagueWordIndexes;
    //不认识单词索引,指的是不认识的单词在字典中的索引
    private List<Integer> unknownWordIndexes;

    //构造函数
    public SelfCheckRecord() {

    }
    //getter and setter
    public int getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(int timeUsed) {
        this.timeUsed = timeUsed;
    }
    public Long getSelfcheckrecordId() {
        return selfcheckrecordId;
    }

    public void setSelfcheckrecordId(Long selfcheckrecordId) {
        this.selfcheckrecordId = selfcheckrecordId;
    }

    public List<Integer> getKnownWordIndexes() {
        return knownWordIndexes;
    }

    public void setKnownWordIndexes(List<Integer> knownWordIndexes) {
        this.knownWordIndexes = knownWordIndexes;
    }

    public List<Integer> getVagueWordIndexes() {
        return vagueWordIndexes;
    }

    public void setVagueWordIndexes(List<Integer> vagueWordIndexes) {
        this.vagueWordIndexes = vagueWordIndexes;
    }

    public List<Integer> getUnknownWordIndexes() {
        return unknownWordIndexes;
    }

    public void setUnknownWordIndexes(List<Integer> unknownWordIndexes) {
        this.unknownWordIndexes = unknownWordIndexes;
    }
}
