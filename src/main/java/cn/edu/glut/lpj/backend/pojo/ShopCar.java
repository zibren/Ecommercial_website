package cn.edu.glut.lpj.backend.pojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ShopCar {
    private String user_id;
    private String car_id;
    private String goods_id;
    private Integer count;
}
