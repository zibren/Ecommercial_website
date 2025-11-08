package cn.edu.glut.lpj.backend.Service.Impl;


import cn.edu.glut.lpj.backend.Mapper.UserMapper;
import cn.edu.glut.lpj.backend.Utils.JwtUtils;
import cn.edu.glut.lpj.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtils jwt;

    public String authenticate(User loginUser) throws Exception {
        User u = userMapper.authUserInfo(loginUser.getID());

        if (u == null || !u.getPswd().equals(loginUser.getPswd())){
            throw new Exception("no such user or wrong password");
        }
        Map<String,Object> data = new HashMap<>();
        data.put("phone",u.getID());
        data.put("nickname",u.getNickname());
        if (!u.isAdministrator()) {
            data.put("role", "user");
        } else {
            data.put("role", "admin");
        }
        return jwt.generateToken(u.getNickname(),data);
    }

    public void registryUser(User user) throws Exception {
        User u = userMapper.getUserById(user.getID());
        if(u != null){
            throw new Exception("user:"+ user.getID()+" has already exist");
        }
        userMapper.insertUser(user.getID(),user.getNickname(),user.getPswd());
    }




}
