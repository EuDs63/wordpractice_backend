package com.bebop.wordpractice_backend.service;

import com.bebop.wordpractice_backend.entity.ExerciseRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ExerciseRecordService {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    JdbcTemplate jdbcTemplate;

    RowMapper<ExerciseRecord> exerciseRecordRowMapper = new BeanPropertyRowMapper<>(ExerciseRecord.class);

    /**
     * 插入一条新的练习记录
     * @param dictId
     * @param createdAt
     * @param questionCount
     * @param correctCount
     * @param wrongWordIndexes
     * @param userId
     * @return
     */
    public Boolean InsertExerciceRecode(int dictId,int userId,long createdAt,int timeUsed,int questionCount,int correctCount,String wrongWordIndexes,int type){
        try{
            jdbcTemplate.update("INSERT INTO exercise (dictId,userId,createdAt,timeUsed,questionCount,correctCount,wrongWordIndexes,type) VALUES(?,?,?,?,?,?,?,?)",dictId,userId,createdAt,timeUsed,questionCount,correctCount,wrongWordIndexes,type);
            logger.info("Insert exercise record of {} successfully",userId);
            return true;
        }catch (Exception e){
            logger.error(e.toString());
            return false;
        }
    }

    /**
     * 根据用户id查询并返回练习记录
     * @param userId
     * @return ExerciseRecord[]
     */
    public List<Map<String, Object>> getExerciseRecordByUserId(int userId) {
        try {
            String sql = "SELECT * FROM exercise WHERE userId = ?";
            List<Map<String, Object>> exerciseRecords = jdbcTemplate.queryForList(sql, userId);
            logger.info("Get exercise record of {} successfully", userId);
            return exerciseRecords;
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 返回所有的练习记录
     */
    public List<Map<String, Object>> getAllExerciseRecord() {
        try {
            String sql = "SELECT * FROM exercise";
            List<Map<String, Object>> exerciseRecords = jdbcTemplate.queryForList(sql);
            logger.info("Get all exercise record successfully");
            return exerciseRecords;
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }
    }
}
