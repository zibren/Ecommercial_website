package cn.edu.glut.lpj.backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class User {
    private String ID;// 用户手机号，前面疏忽了用int装手机号。。。
    private String nickname;
    private String pswd;
    private boolean isAdministrator;
}
