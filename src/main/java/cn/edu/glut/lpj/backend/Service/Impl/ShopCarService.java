package cn.edu.glut.lpj.backend.Service.Impl;
import cn.edu.glut.lpj.backend.Mapper.GoodsMapper;
import cn.edu.glut.lpj.backend.Mapper.ShopCarMapper;
import cn.edu.glut.lpj.backend.pojo.ShopCar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class ShopCarService {
    @Autowired
    private ShopCarMapper shopCarMapper;
    @Autowired
    private GoodsMapper goodsMapperCar;

    private static final Random RANDOM = new Random();

    // 获取购物车详情
    @Transactional
    public Map<String, Object> getCarDetails(String userId, String carId) {
        List<ShopCar> carItems = shopCarMapper.selectByUserIdAndCarId(userId, carId);
        List<Map<String, Object>> goodsList = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (ShopCar item : carItems) {
            Map<String, Object> goodsInfo = goodsMapperCar.selectGoodsById(item.getGoods_id());
            if (goodsInfo != null) {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("id", item.getGoods_id());
                itemMap.put("name", goodsInfo.get("name"));
                itemMap.put("price", goodsInfo.get("price"));
                itemMap.put("image", goodsInfo.get("image"));
                itemMap.put("shop", goodsInfo.get("shop"));
                itemMap.put("quantity", item.getCount());

                // 计算总金额
                BigDecimal price = new BigDecimal(goodsInfo.get("price").toString());
                total = total.add(price.multiply(BigDecimal.valueOf(item.getCount())));
                goodsList.add(itemMap);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("goods_list", goodsList);
        result.put("total_amount", total.setScale(2, RoundingMode.HALF_UP));
        return result;
    }

    // 批量添加商品
    @Transactional
    public void batchAddItems(String userId, String carId, List<Map<String, Object>> items) {
        List<ShopCar> insertList = new ArrayList<>();
        String carIdByUserId = getCarIdByUserId(userId);
        if (carId == null || carId.isEmpty() || !carId.equals(carIdByUserId)) {
            carId = carIdByUserId;
        }
        for (Map<String, Object> item : items) {
            ShopCar car = new ShopCar();
            car.setUser_id(userId);
            car.setCar_id(carId);
            car.setGoods_id((String) item.get("goods_id"));
            car.setCount((Integer) item.get("count"));
            insertList.add(car);
        }
        shopCarMapper.batchInsert(insertList);
    }

    // 批量更新数量
    @Transactional
    public void batchUpdateItems(String userId, String carId, List<Map<String, Object>> items) {
        /**
         * 这里添加对更新商品的判断，若是数据库中没有的，则添加，有的则更新
         */
        List<ShopCar> shopCars = shopCarMapper.selectByUserIdAndCarId(userId, carId);
        List<ShopCar> tempShopCars = new ArrayList<>();
        List<ShopCar> newShopCarList = new ArrayList<>();
        // 函数式编程真好用
        items.forEach(i-> tempShopCars.add(new ShopCar(userId,carId,(String) i.get("goods_id"),(Integer) i.get("count"))));
        tempShopCars.forEach(ts -> {
            if(!shopCars.contains(ts)) newShopCarList.add(ts);
        });
        if (!newShopCarList.isEmpty()) shopCarMapper.batchInsert(newShopCarList);
        for (Map<String, Object> item : items) {
            shopCarMapper.updateCount(userId, carId,
                    (String) item.get("goods_id"),
                    (Integer) item.get("count"));
        }
    }

    // 批量删除商品
    @Transactional
    public void batchDeleteItems(String userId, String carId, List<String> goodsIds) {
        shopCarMapper.batchDelete(userId, carId, goodsIds);
    }
    // ShopCarService.java 新增方法
    public String getCarIdByUserId(String userId) {
        String carId = shopCarMapper.selectCarIdByUserId(userId);
        // 若是第一次使用购物车则先创建购物车id
        if(carId == null){
            String genaratedId = generateCarId();
            shopCarMapper.createNewUserIdAndCarId(userId, genaratedId);
            carId = genaratedId;
        }
        return carId;
    }

    private String generateCarId(){
        int i = RANDOM.nextInt(10101-10000)+10001;
        return String.valueOf(i);
    }
}