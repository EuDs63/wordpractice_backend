package com.bebop.wordpractice_backend.web;

import com.bebop.wordpractice_backend.entity.ResultCode;
import com.bebop.wordpractice_backend.entity.User;
import com.bebop.wordpractice_backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserService userService;

    /**
     * 处理前端传来的登录请求
     * 有个问题是无论登录是否成功，响应码都为200
     * @param loginInfo
     * @return
     */
    @PostMapping(value = "/login")//指定该方法处理HTTP POST服务路径为"/login"的请求。
    @CrossOrigin(origins = "*")//允许所有来源的跨域请求。这意味着客户端可以从任何源（域名）向服务器发起POST请求。
    public ResultCode login (@RequestBody Map<String, String> loginInfo)
    {
        String email = loginInfo.get("email");
        String password = loginInfo.get("password");

        if(userService.login(email,password)!=null){
            logger.info("{} login successfully",email);
            return ResultCode.SUCCESS;
        }
        else {
            logger.info("{} login failed",email);
            return ResultCode.FAILED;
        }
    }
    /**
     * 处理前端传来的注册请求
     * @param user
     * @return
     */
    @PostMapping(value = "/register")
    @CrossOrigin(origins = "*")
    public ResultCode register(@RequestBody User user) {
        if (userService.register(user.getEmail(), user.getPassword(), user.getName(), user.getLevel()) != null) {
            logger.info("{} register successfully", user.getEmail());
            return ResultCode.SUCCESS;
        } else {
            logger.info("{} register failed", user.getEmail());
            return ResultCode.FAILED;
        }
    }

    /**
     * 处理前端传来的改密码请求
     * @param oldPassword
     * @param newPassword
     * @param email
     * @return
     */
    @PostMapping(value = "/resetpassword")//指定该方法处理HTTP POST服务路径为"/resetpassword"的请求。
    @CrossOrigin(origins = "*")//允许所有来源的跨域请求。这意味着客户端可以从任何源（域名）向服务器发起POST请求。
    public ResultCode resetpassword (@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String email)
    {
        if(userService.resetpassword(email,oldPassword,newPassword)){
            logger.info("{} resetpassword successfully",email);
            return ResultCode.SUCCESS;
        }
        else {
            logger.info("{} resetpassword failed",email);
            return ResultCode.FAILED;
        }
    }

    /**
     * 处理前端传来的改名字请求
     * @param email
     * @param name
     * @return
     */
    @PostMapping(value = "/resetname")//指定该方法处理HTTP POST服务路径为"/resetname"的请求。
    @CrossOrigin(origins = "*")//允许所有来源的跨域请求。这意味着客户端可以从任何源（域名）向服务器发起POST请求。
    public ResultCode resetname (@RequestParam String email,@RequestParam String name)
    {
        if(userService.resetname(email,name)){
            return ResultCode.SUCCESS;
        }
        else {
            logger.info("{} resetname failed",email);
            return ResultCode.FAILED;
        }
    }

    @GetMapping("/userinfo")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String, Object>> getUserByEmail(@RequestParam String email) {
        // 通过邮箱查询用户信息
        User user = userService.getUserByEmail(email);

        if (user != null) {
            logger.info("return information of user {}",email);
            // 用户存在，构建返回的JSON对象
            Map<String, Object> response = new HashMap<>();
            response.put("username", user.getName());
            response.put("email", user.getEmail());
            response.put("level", user.getLevel());
            response.put("isAdmin", user.getIsAdmin());
            response.put("userId",user.getId());
            return ResponseEntity.ok(response);
        } else {
            // 用户不存在，返回适当的错误信息
            return ResponseEntity.notFound().build();
        }
    }
}
