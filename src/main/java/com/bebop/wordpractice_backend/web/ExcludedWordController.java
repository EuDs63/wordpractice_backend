package com.bebop.wordpractice_backend.web;

import com.bebop.wordpractice_backend.service.ExcludedWordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/word")
public class ExcludedWordController {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ExcludedWordService excludedWordService;

    /**
     * 根据用户id和词典id找到对应的ExcludedWords
     */
    @GetMapping("/getExcludedWords")
    @CrossOrigin(origins = "*")
    public List<Integer> getExcludedWords(@RequestParam int  userId,@RequestParam int dictId) {
        logger.info("try to get excluded words of user: {}, dict: {}", userId, dictId);
        return excludedWordService.getExcludedWordsByUserIdAndDictId(userId, dictId);
    }

    /**
     * 根据用户id和词典id找到对应的wrongWordIndexes
     */
    @GetMapping("/getWrongWords")
    @CrossOrigin(origins = "*")
    public List<Integer> getWrongWords(@RequestParam int  userId,@RequestParam int dictId) {
        logger.info("try to get wrong words of user: {}, dict: {}", userId, dictId);
        return excludedWordService.getWrongWordIndexesByUserIdAndDictId(userId, dictId);
    }

}
