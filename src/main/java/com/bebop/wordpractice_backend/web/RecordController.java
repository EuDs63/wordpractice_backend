package com.bebop.wordpractice_backend.web;

import com.bebop.wordpractice_backend.entity.ExerciseRecord;
import com.bebop.wordpractice_backend.entity.ResultCode;
import com.bebop.wordpractice_backend.entity.SelfCheckRecord;
import com.bebop.wordpractice_backend.service.ExcludedWordService;
import com.bebop.wordpractice_backend.service.ExerciseRecordService;
import com.bebop.wordpractice_backend.service.SelfCheckRecordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/record")
public class RecordController {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ExerciseRecordService exerciseRecordService;

    @Autowired
    SelfCheckRecordService wordRecordService;

    @Autowired
    ExcludedWordService excludedWordService;

    /**
     * 添加一条练习记录
     * @param exerciseRecord
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping("/addExerciseRecord")
    @CrossOrigin(origins = "*")
    public ResultCode addRecord(@RequestBody ExerciseRecord exerciseRecord) throws JsonProcessingException {
        int dictId = exerciseRecord.getDictId();
        int userId = exerciseRecord.getUserId();
        long createdAt = exerciseRecord.getCreatedAt();
        int timeUsed = exerciseRecord.getTimeUsed();
        int questionCount = exerciseRecord.getQuestionCount();
        int correctCount = exerciseRecord.getCorrectCount();
        List<Integer> wrongWordIndexes = exerciseRecord.getWrongWordIndexes();

        //更新excluded_words表
        excludedWordService.updateExcludedWordsByExercise(exerciseRecord);

        // 将 List<Integer> 转换为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String wrongWordIndexesJson = objectMapper.writeValueAsString(wrongWordIndexes);
        int type = exerciseRecord.getType();

        if (exerciseRecordService.InsertExerciceRecode(dictId,userId,createdAt,timeUsed,questionCount,correctCount,wrongWordIndexesJson,type))
        {
            logger.info("成功添加了一条 userId: {} dictId: {} 单词练习记录", userId, dictId);
            return ResultCode.SUCCESS;
        } else {
            logger.info("handle add-record request failed");
            return ResultCode.FAILED;
        }
    }

    /**
     * 添加一条单词自检记录
     */
    @RequestMapping("/addSelfCheckRecord")
    @CrossOrigin(origins = "*")
    public ResultCode addSelfCheckRecord(@RequestBody SelfCheckRecord selfCheckRecord) throws JsonProcessingException {
        int dictId = selfCheckRecord.getDictId();
        int userId = selfCheckRecord.getUserId();
        long createdAt = selfCheckRecord.getCreatedAt();
        int timeUsed = selfCheckRecord.getTimeUsed();

        List<Integer> knownWordIndexes = selfCheckRecord.getKnownWordIndexes();
        List<Integer> vagueWordIndexes = selfCheckRecord.getVagueWordIndexes();
        List<Integer> unknownWordIndexes = selfCheckRecord.getUnknownWordIndexes();

        //更新excluded_words表
        excludedWordService.updateExcludedWordsBySelfTest(selfCheckRecord);

        //将 List<Integer> 转换为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String knownWordIndexesJson = objectMapper.writeValueAsString(knownWordIndexes);
        String vagueWordIndexesJson = objectMapper.writeValueAsString(vagueWordIndexes);
        String unknownWordIndexesJson = objectMapper.writeValueAsString(unknownWordIndexes);

        //插入
        if (wordRecordService.InsertSelfCheckRecord(dictId, userId, createdAt, timeUsed, knownWordIndexesJson, vagueWordIndexesJson, unknownWordIndexesJson)) {
            logger.info("handle add-record request successfully");
            return ResultCode.SUCCESS;
        } else {
            logger.info("handle add-record request failed");
            return ResultCode.FAILED;
        }

    }

    /**
     * 根据用户id获取练习记录
     */
    @GetMapping("/getExerciseRecord")
    @CrossOrigin(origins = "*")
    public List<Map<String, Object>> getExerciseRecord(@RequestParam int userId) {
        logger.info("try to handle get-exercise-record request of {}", userId);
        return exerciseRecordService.getExerciseRecordByUserId(userId);
    }

    /**
     * 返回所有的练习记录
     */
    @GetMapping("/getAllExerciseRecord")
    @CrossOrigin(origins = "*")
    public List<Map<String, Object>> getAllExerciseRecord() {
        logger.info("try to handle get-all-exercise-record request");
        return exerciseRecordService.getAllExerciseRecord();
    }

    /**
     * 根据用户id获取单词自检记录
     */
    @GetMapping("/getSelfCheckRecord")
    @CrossOrigin(origins = "*")
    public List<Map<String, Object>> getSelfCheckRecord(@RequestParam int userId) {
        logger.info("try to handle get-self-check-record request of {}", userId);
        return wordRecordService.getSelfCheckRecordByUserId(userId);
    }

    /**
     * 返回所有的单词自检记录
     */
    @GetMapping("/getAllSelfCheckRecord")
    @CrossOrigin(origins = "*")
    public List<Map<String, Object>> getAllSelfCheckRecord() {
        logger.info("try to handle get-all-self-check-record request");
        return wordRecordService.getAllSelfCheckRecord();
    }
}
