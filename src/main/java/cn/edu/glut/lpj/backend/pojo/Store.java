package cn.edu.glut.lpj.backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Store {
    private String store_id;
    private String login_code;
    private String brand;
    private String owner;
    private Date create_time;
    private String phone;
}
