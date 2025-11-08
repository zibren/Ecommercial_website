package cn.edu.glut.lpj.backend.Mapper;
import cn.edu.glut.lpj.backend.pojo.ShopCar;
import org.apache.ibatis.annotations.*;
import java.util.List;


@Mapper
public interface ShopCarMapper {
    List<ShopCar> selectByUserIdAndCarId(@Param("user_id") String userId, @Param("car_id") String carId);

    void batchInsert(List<ShopCar> items);

    void updateCount(@Param("user_id") String userId,
                     @Param("car_id") String carId,
                     @Param("goods_id") String goodsId,
                     @Param("count") int count);

    void batchDelete(@Param("user_id") String userId,
                     @Param("car_id") String carId,
                     @Param("goods_ids") List<String> goodsIds);
    String selectCarIdByUserId(@Param("user_id") String userId);

    @Insert("INSERT INTO shop_car(user_id, car_id) value (#{user_id},#{car_id})")
    void createNewUserIdAndCarId(String user_id,String car_id);

}
