package com.bebop.wordpractice_backend.service;

import com.bebop.wordpractice_backend.entity.SelfCheckRecord;
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
public class SelfCheckRecordService {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    JdbcTemplate jdbcTemplate;

    RowMapper<SelfCheckRecord> selfCheckRecordRowMapper = new BeanPropertyRowMapper<>(SelfCheckRecord.class);

    /**
     * 插入一条新的单词自检记录
     * @param dictId
     * @param userId
     * @param createdAt
     * @param timeUsed
     * @param knownWordIndexes
     * @param vagueWordIndexes
     * @param unknownWordIndexes
     * @return
     */
    public Boolean InsertSelfCheckRecord(int dictId, int userId, long createdAt, int timeUsed, String knownWordIndexes, String vagueWordIndexes, String unknownWordIndexes) {
        try {
            jdbcTemplate.update("INSERT INTO selfcheckrecord (dictId,userId,createdAt,timeUsed,knownWordIndexes,vagueWordIndexes,unknownWordIndexes) VALUES(?,?,?,?,?,?,?)", dictId, userId, createdAt, timeUsed, knownWordIndexes, vagueWordIndexes, unknownWordIndexes);
            logger.info("Insert selfcheckrecord of {} successfully", userId);
            return true;
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }
    }

    /**
     * 根据用户id查询并返回单词自检记录
     */
    public List<Map<String, Object>> getSelfCheckRecordByUserId(int userId) {
        try {
            String sql = "SELECT * FROM selfcheckrecord WHERE userId = ?";
            List<Map<String, Object>> selfCheckRecords = jdbcTemplate.queryForList(sql, userId);
            logger.info("Get selfcheck record of {} successfully", userId);
            return selfCheckRecords;
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 返回所有单词自检记录
     */
    public List<Map<String, Object>> getAllSelfCheckRecord() {
        try {
            String sql = "SELECT * FROM selfcheckrecord";
            List<Map<String, Object>> selfCheckRecords = jdbcTemplate.queryForList(sql);
            logger.info("Get all selfcheck record successfully");
            return selfCheckRecords;
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 对单词自检记录进行处理
     * 首先查excludedwords表，获取其中对应的用户、词典的unnecessary，并将其转为数组
     * 先检索unnecessary，若其中的序号出现在vagueWordIndexes或unknownWordIndexes中，则将其从中删除。
     * 将knownWordIndexes中的序号加入unnecessary中；
     */
}
