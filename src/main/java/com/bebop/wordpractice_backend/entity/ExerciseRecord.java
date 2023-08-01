package com.bebop.wordpractice_backend.entity;

import java.util.List;

/**
 * Record的子类，包含信息：
 * 用户id、字典、时间戳、
 * 练习id(第几次练习)、练习所用时间、题目数量，正确题目计数，错误单词索引，练习类型
 */
public class ExerciseRecord extends Record {
    //练习id(第几次练习)
    private Long exerciseId;

    //练习所用时间,单位为秒
    private int timeUsed;
    //题目数量
    private Integer questionCount;
    //正确题目计数
    private Integer correctCount;
    //错误单词索引,指的是错题的单词在字典中的索引
    private List<Integer> wrongWordIndexes;
    //练习类型
    private int type;
    //构造函数
    public ExerciseRecord() {

    }
    //getter and setter
    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public Integer getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(Integer correctCount) {
        this.correctCount = correctCount;
    }

    public int getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(int timeUsed) {
        this.timeUsed = timeUsed;
    }
    public List<Integer> getWrongWordIndexes() {
        return wrongWordIndexes;
    }

    public void setWrongWordIndexes(List<Integer> wrongWordIndexes) {
        this.wrongWordIndexes = wrongWordIndexes;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getType(){
        return type;
    }

    public void setType(int type){
        this.type = type;
    }
}