package com.bebop.wordpractice_backend.service;

import com.bebop.wordpractice_backend.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Statement;

@Component
public class UserService {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    JdbcTemplate jdbcTemplate;

    RowMapper<User> userRowMapper = new BeanPropertyRowMapper<>(User.class);

    public User getUserById(long id){
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", userRowMapper,id);
    }

    /**
     * 根据email查找User，如成功，返回User，失败，返回null
     * @param email
     * @return
     */
    public User getUserByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email = ?", userRowMapper, email);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    /**
     *
     * @param email
     * @param password
     * @return
     */
    public User login(String email, String password){
        logger.info("try login by {}...",email);
        User user = getUserByEmail(email);
        if(user != null)
        {
            if (user.getPassword().equals(password))
            {
                return user;
            }
        }
        return null;

    }

    //注册操作
    public User register(String email, String password, String name, Integer level){
        logger.info("try register by {}...",email);
        User user = new User();
        //设置基本信息
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setCreatedAt(System.currentTimeMillis());
        user.setLevel(level);

        //传入数据库
        KeyHolder holder = new GeneratedKeyHolder();
        if(1 != jdbcTemplate.update((conn) -> {
            var ps = conn.prepareStatement("INSERT INTO users (email,password,name,createdAt,level) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1,user.getEmail());
            ps.setObject(2, user.getPassword());
            ps.setObject(3, user.getName());
            ps.setObject(4, user.getCreatedAt());
            ps.setObject(5,user.getLevel());
            return ps;
        }, holder))
        {
            throw new RuntimeException("Insert failed!");
        }
        user.setId(holder.getKey().longValue());
        return user;
    }

    /**
     * 重置密码操作
     * @param email
     * @param oldPassword
     * @param newPassword
     * @return
     */
    public Boolean resetpassword(String email, String oldPassword, String newPassword) {
        logger.info("try resetpassword by {}...", email);
        User user = getUserByEmail(email);

        if (user == null) {
            // 用户不存在
            logger.error("User does not exist!");
            return false;
        }

        if (!user.getPassword().equals(oldPassword)) {
            // 密码不正确
            logger.error("Incorrect password!");
            return false;
        }

        if (1 != jdbcTemplate.update("UPDATE users SET password = ? WHERE email = ?", newPassword, email)) {
            throw new RuntimeException("Update failed!");
        }

        logger.info("{} resetpassword successfully", email);
        return true;
    }


    /**
     * 修改名字
     * @param email
     * @param name
     * @return
     */
    public Boolean resetname(String email, String name){
        logger.info("try resetname by {}...",email);
        User user = getUserByEmail(email);
        if(user != null)
        {
            if (1 != jdbcTemplate.update("UPDATE users SET name = ? WHERE email = ?", name, email))
            {
                throw new RuntimeException("Update failed!");
            }
            else{
                logger.info("{} resetname successfully",email);
                return true;
            }

        }
        logger.error("User does not exist!");
        return false;

    }
}
