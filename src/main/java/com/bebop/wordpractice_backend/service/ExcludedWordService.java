package com.bebop.wordpractice_backend.service;

import com.bebop.wordpractice_backend.entity.ExcludedWords;
import com.bebop.wordpractice_backend.entity.ExerciseRecord;
import com.bebop.wordpractice_backend.entity.SelfCheckRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ExcludedWordService {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    JdbcTemplate jdbcTemplate;

    RowMapper<ExcludedWords> excludedWordsRowMapper = new BeanPropertyRowMapper<>(ExcludedWords.class);

    /**
     * 根据用户id和词典id找到对应的ExcludedWords
     */
    public List<Integer> getExcludedWordsByUserIdAndDictId(int userId, int dictId) {
        try {
            String sql = "SELECT wordIndexes FROM excludedwords WHERE userId = ? AND dictId = ?";
            String wordIndexes = jdbcTemplate.queryForObject(sql, String.class, userId, dictId);
            logger.info("Get excluded words of user: {}, dict: {} successfully", userId, dictId);
            if (wordIndexes == null) {
                List<Integer> excludedWords = new ArrayList<>();
                excludedWords.add(-1);
                return excludedWords;
            }else
            {
                // 将String类型的wordIndexes转换为List<Integer>
                ObjectMapper objectMapper = new ObjectMapper();
                List<Integer> excludedWords = objectMapper.readValue(wordIndexes, new TypeReference<List<Integer>>() {});
                return excludedWords;
            }
        } catch (Exception e) {
            logger.warn("数据库中尚未有与userId: {} dictId: {} 对应的wordIndexes;默认返回-1", userId, dictId);
            List<Integer> excludedWords = new ArrayList<>();
            excludedWords.add(-1);
            return excludedWords;
        }
    }

    /**
     * 根据用户id和词典id找到对应的wrongWordIndexes
     */
    public List<Integer> getWrongWordIndexesByUserIdAndDictId(int userId, int dictId) {
        try {
            String sql = "SELECT wrongWordIndexes FROM excludedwords WHERE userId = ? AND dictId = ?";
            String wrongWordIndexes = jdbcTemplate.queryForObject(sql, String.class, userId, dictId);
            logger.info("Get wrong word indexes of user: {}, dict: {} successfully", userId, dictId);

            // 将String类型的wrongWordIndexes转换为List<Integer>
            ObjectMapper objectMapper = new ObjectMapper();
            List<Integer> wrongWordIndexesList = objectMapper.readValue(wrongWordIndexes, new TypeReference<List<Integer>>() {});
            return wrongWordIndexesList;

        } catch (Exception e) {
            logger.warn("数据库中尚未有与userId: {} dictId: {} 对应的wrongWordIndexes;默认返回-1", userId, dictId);
            List<Integer> wrongWordIndexesList = new ArrayList<>();
            wrongWordIndexesList.add(-1);
            return wrongWordIndexesList;
        }
    }

    /**
     * 根据单词自检记录更新excludedwords
     * 先根据userId和dictId获得对应的数组unnecessary
     * 然后检索数组，若其中的序号出现在vagueWordIndexes或unknownWordIndexes中，则将其从数组中删除
     * 将knownWordIndexes中的序号加入数组
     */
    public void updateExcludedWordsBySelfTest(SelfCheckRecord wordRecord) throws JsonProcessingException {
        int userId = wordRecord.getUserId();
        int dictId = wordRecord.getDictId();
        List<Integer> knownWordIndexes = wordRecord.getKnownWordIndexes();
        List<Integer> vagueWordIndexes = wordRecord.getVagueWordIndexes();
        List<Integer> unknownWordIndexes = wordRecord.getUnknownWordIndexes();

        // 获取当前的unnecessary数组
        List<Integer> unnecessary = getExcludedWordsByUserIdAndDictId(userId, dictId);

        // 从unnecessary中移除vagueWordIndexes和unknownWordIndexes中的序号
        unnecessary.removeAll(vagueWordIndexes);
        unnecessary.removeAll(unknownWordIndexes);

        // 将knownWordIndexes中的序号加入unnecessary数组
        unnecessary.addAll(knownWordIndexes);

        //将 List<Integer> 转换为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String unnecessaryJson = objectMapper.writeValueAsString(unnecessary);

//        // 更新ExcludedWords表中的unnecessary字段
//        String updateSql = "UPDATE excludedwords SET wordIndexes = ? WHERE userId = ? AND dictId = ?";
//
//        jdbcTemplate.update(updateSql, unnecessaryJson, userId, dictId);

        // 先执行更新语句
        String updateSql = "UPDATE excludedwords SET wordIndexes = ? WHERE userId = ? AND dictId = ?";
        boolean isUpdated = false;
        try {
            isUpdated = jdbcTemplate.update(updateSql, unnecessaryJson, userId, dictId) > 0;
            if (isUpdated) {
                logger.info("根据单词自检记录更新excludedwords of user {}, dict {} 成功", userId, dictId);
            }
        } catch (DataAccessException e) {
            // 若更新失败，则执行插入语句
            logger.warn("Failed to update excludedwords. Performing insert instead.");
        }

        // 若更新未成功，则执行插入语句
        if (!isUpdated) {
            String insertSql = "INSERT INTO excludedwords (userId, dictId, wordIndexes) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertSql, userId, dictId, unnecessaryJson);
            logger.info("根据单词自检记录更新excludedwords of user {}, dict {} 成功", userId, dictId);
        }
    }

    /**
     * 根据单词练习记录更新ExcludedWords
     * 先根据userId和dictId获得对应的数组unnecessary, wrongWordList
     * 若unnecessary不为空，则检索数组，若其中的序号出现在wrongWordIndexes中，则将其从数组中删除
     * 若unnecessary为空，则添加-1
     * 若wrongWordList不为空，则将wrongWordIndexes中的序号加入wrongWordList数组
     * 若wrongWordList为空，不对其进行操作
     */
    public void updateExcludedWordsByExercise(ExerciseRecord exerciseRecord) throws JsonProcessingException {
        // 处理传过来的exerciseRecord，获取userId, dictId, wrongWordIndexes
        int userId = exerciseRecord.getUserId();
        int dictId = exerciseRecord.getDictId();
        List<Integer> wrongWordIndexes = exerciseRecord.getWrongWordIndexes();

        // 从数据库中获取当前的unnecessary数组
        List<Integer> unnecessary = getExcludedWordsByUserIdAndDictId(userId, dictId);

        //从数据库中获取当前的wrongWordIndexes数组
        List<Integer> wrongWordList = getWrongWordIndexesByUserIdAndDictId(userId, dictId);

        // unnecessary不为空，则从unnecessary中移除wrongWordIndexes中的序号
        if(unnecessary != null)
        {
            unnecessary.removeAll(wrongWordIndexes);
        }else {
            unnecessary = new ArrayList<>();
            unnecessary.add(-1);
        }

        if(wrongWordList != null)
        {
            // 将wrongWordIndexes中的序号加入wrongWordList数组
            wrongWordList.addAll(wrongWordIndexes);
        }else {
            wrongWordList = new ArrayList<>();
            wrongWordList.add(-1);
        }

        //将 List<Integer> 转换为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String unnecessaryJson = objectMapper.writeValueAsString(unnecessary);
        String wrongWordListJson = objectMapper.writeValueAsString(wrongWordList);

//        // 更新ExcludedWords表中的unnecessary字段
//        String updateSql = "UPDATE excludedwords SET wordIndexes = ? WHERE userId = ? AND dictId = ?";
//        jdbcTemplate.update(updateSql, unnecessaryJson, userId, dictId);
//
//        // 更新ExcludedWords表中的wrongWordIndexes字段
//        String updateSql2 = "UPDATE excludedwords SET wrongWordIndexes = ? WHERE userId = ? AND dictId = ?";
//        jdbcTemplate.update(updateSql2, wrongWordListJson, userId, dictId);

//        String sql = "INSERT INTO excludedwords (userId, dictId, wordIndexes, wrongWordIndexes) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE wordIndexes = VALUES(wordIndexes), wrongWordIndexes = VALUES(wrongWordIndexes)";
//        jdbcTemplate.update(sql, userId, dictId, unnecessaryJson, wrongWordListJson);
        // 先执行更新语句
        String updateSql = "UPDATE excludedwords SET wordIndexes = ?, wrongWordIndexes = ? WHERE userId = ? AND dictId = ?";
        boolean isUpdated = false;
        try {
            isUpdated = jdbcTemplate.update(updateSql, unnecessaryJson, wrongWordListJson, userId, dictId) > 0;
            if (isUpdated) {
                logger.info("Updated excludedwords record of user {}, dict {} successfully", userId, dictId);
            }
        } catch (DataAccessException e) {
            // 若更新失败，则执行插入语句
            logger.warn("Failed to update excludedwords record. Performing insert instead.");
        }

        // 若更新未成功，则执行插入语句
        if (!isUpdated) {
            String insertSql = "INSERT INTO excludedwords (userId, dictId, wordIndexes, wrongWordIndexes) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(insertSql, userId, dictId, unnecessaryJson, wrongWordListJson);
            logger.info("Inserted excludedwords record of user {}, dict {} successfully", userId, dictId);
        }

    }
}
