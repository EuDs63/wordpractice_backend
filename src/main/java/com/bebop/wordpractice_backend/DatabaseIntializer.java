package com.bebop.wordpractice_backend;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseIntializer {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        jdbcTemplate.execute("DROP SCHEMA PUBLIC CASCADE;");
        //用户
        jdbcTemplate.execute("DROP TABLE IF EXISTS users");
        jdbcTemplate.update("CREATE TABLE users(" //
                + "id INT IDENTITY NOT NULL PRIMARY KEY, " //
                + "email VARCHAR(100) NOT NULL, " //
                + "password VARCHAR(100) , " //
                + "name VARCHAR(100) , " //
                + "createdAt BIGINT NOT NULL, " //
                + "level INT NOT NULL," //
                + "isAdmin BOOLEAN, " //
                + "UNIQUE (email))");
        jdbcTemplate.update("ALTER TABLE users ALTER COLUMN isAdmin SET DEFAULT FALSE");
        jdbcTemplate.update("INSERT INTO users (email,password,name,createdAt,level,isAdmin) VALUES('eric@example.com', 'highly-secure-password-fYjUw-','eric',0,1,TRUE)");
        //单词练习记录
        jdbcTemplate.execute("DROP TABLE IF EXISTS exercise");
        jdbcTemplate.update("CREATE TABLE exercise (" +
                "exerciseId INT IDENTITY NOT NULL PRIMARY KEY, " +
                "dictId INT NOT NULL, " +
                "userId INT NOT NULL, " +
                "createdAt BIGINT NOT NULL, " +
                "timeUsed INT, " +
                "questionCount INT, " +
                "correctCount INT, " +
                "wrongWordIndexes VARCHAR(256)," +
                "type INT NOT NULL, " +
                "FOREIGN KEY (userId) REFERENCES users(id)" +
                ")");
        jdbcTemplate.update("INSERT INTO exercise (dictId,userId,createdAt,timeUsed,questionCount,correctCount,wrongWordIndexes,type) VALUES(1,0,0,60,20,18,'[0,4,6,7,8,9,11,12]',0)");
        // 单词自检记录
        jdbcTemplate.execute("DROP TABLE IF EXISTS selfcheckrecord");
        jdbcTemplate.update("CREATE TABLE selfcheckrecord (" +
                "selfcheckrecordId INT IDENTITY NOT NULL PRIMARY KEY, " +
                "dictId INT NOT NULL, " +
                "userId INT NOT NULL, " +
                "createdAt BIGINT NOT NULL, " +
                "timeUsed INT, " +
                "knownWordIndexes VARCHAR(256), " +
                "vagueWordIndexes VARCHAR(256), " +
                "unknownWordIndexes VARCHAR(256), " +
                "FOREIGN KEY (userId) REFERENCES users(id)" +
                ")");
        jdbcTemplate.update("INSERT INTO selfcheckrecord (dictId,userId,createdAt,timeUsed,knownWordIndexes,vagueWordIndexes,unknownWordIndexes) VALUES(1,0,0,60,'[0,4,6,7,8,9,11,12]','[1,2,3,5,10]','[13,14,15,16,17]')");
        // 无需发送的单词
        jdbcTemplate.execute("DROP TABLE IF EXISTS excludedwords");
        jdbcTemplate.update("CREATE TABLE excludedwords (" +
                "userId INT NOT NULL, " +
                "dictId INT NOT NULL, " +
                "wordIndexes VARCHAR(256) DEFAULT '[-1]', " +
                "wrongWordIndexes VARCHAR(256) DEFAULT '[-1]', " +
                "FOREIGN KEY (userId) REFERENCES users(id), " +
                ")");
        jdbcTemplate.update("INSERT INTO excludedwords (userId,dictId,wordIndexes) VALUES(0,1,'[0,4,6,7,8,9,11,12]')");
    }
}
