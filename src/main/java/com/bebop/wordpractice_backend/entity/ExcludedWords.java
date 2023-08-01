package com.bebop.wordpractice_backend.entity;

import java.util.List;

public class ExcludedWords {

    private int userId;
    private int dictId;
    private List<Integer> wordIndexes;

    private List<Integer> wrongWordIndexes;

    public ExcludedWords() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDictId() {
        return dictId;
    }

    public void setDictId(int dictId) {
        this.dictId = dictId;
    }

    public List<Integer> getWordIndexes() {
        return wordIndexes;
    }

    public void setWordIndexes(List<Integer> wordIndexes) {
        this.wordIndexes = wordIndexes;
    }

    public List<Integer> getWrongWordIndexes() {
        return wrongWordIndexes;
    }

    public void setWrongWordIndexes(List<Integer> wrongWordIndexes) {
        this.wrongWordIndexes = wrongWordIndexes;
    }

}
