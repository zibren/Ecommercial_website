package cn.edu.glut.lpj.backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String order_id;
    private String user_id;
    private Date create_time;
    private String phone;
    private String receiver_phone;
    private String address;
    private double total;
}
