package cn.edu.glut.lpj.backend.Controller;
import cn.edu.glut.lpj.backend.Service.Impl.ShopCarService;
import cn.edu.glut.lpj.backend.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/car")
public class ShopCarController {
    @Autowired
    private ShopCarService shopCarService;

    // 查询购物车
    @PostMapping
    public Result getCar(@RequestParam String user_id, @RequestParam String car_id) {
        try {
            Map<String, Object> result = shopCarService.getCarDetails(user_id, car_id);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    // 批量添加（使用Map接收复杂JSON结构）
    @PostMapping("/add")
    public Result add(@RequestBody Map<String, Object> params) {
        try {
            String userId = (String) params.get("user_id");
            String carId = (String) params.get("car_id");
            List<Map<String, Object>> items = (List<Map<String, Object>>) params.get("items");
            shopCarService.batchAddItems(userId, carId, items);
            return Result.success();
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    // 批量更新
    @PostMapping("/update")
    public Result update(@RequestBody Map<String, Object> params) {
        try {
            String userId = (String) params.get("user_id");
            String carId = (String) params.get("car_id");
            List<Map<String, Object>> items = (List<Map<String, Object>>) params.get("items");
            shopCarService.batchUpdateItems(userId, carId, items);
            return Result.success();
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    // 批量删除
    @PostMapping("/delete")
    public Result delete(@RequestBody Map<String,Object> param) {
        try {
            String user_id = (String) param.get("user_id");
            String car_id = (String) param.get("car_id");
            List<String>  goods_ids = (List<String>) param.get("goods_ids");
            shopCarService.batchDeleteItems(user_id, car_id, goods_ids);
            return Result.success();
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }
    @GetMapping("/getCarId")
    public Result getCarId(@RequestParam String user_id) {
        String carId = shopCarService.getCarIdByUserId(user_id);
        Map<String,Object> data = new HashMap<>();
        data.put("car_id", carId);
        return carId == null ? Result.error(501,"no such user"):Result.success(data);
    }
}
