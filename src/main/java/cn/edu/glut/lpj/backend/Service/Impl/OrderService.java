package cn.edu.glut.lpj.backend.Service.Impl;

import cn.edu.glut.lpj.backend.Mapper.GoodsMapper;
import cn.edu.glut.lpj.backend.Mapper.OrderCompareMapper;
import cn.edu.glut.lpj.backend.Mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderCompareMapper orderCompareMapper;

    @Autowired
    private GoodsMapper orderGoodsMapper;

    @Transactional
    public String createOrder(Map<String, Object> request) {
        // 生成唯一订单号
        String orderId = generateUniqueOrderId();
        // 插入主订单表
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("orderId", orderId);
        orderMap.put("userId", request.get("user_id"));
        orderMap.put("createTime", new Date());
        orderMap.put("phone", request.get("phone"));
        orderMap.put("receiver", request.get("receiver"));
        orderMap.put("address", request.get("address"));
        orderMap.put("total", request.get("total_amount"));
        orderMapper.insertOrder(orderMap);
        // 插入商品对照表
        List<Map<String, Object>> goodsList = (List<Map<String, Object>>) request.get("goods");
        List<Map<String, Object>> compares = new ArrayList<>();
        for (Map<String, Object> goods : goodsList) {
            Map<String, Object> compare = new HashMap<>();
            compare.put("orderId", orderId);
            compare.put("goodsId", goods.get("goods_id"));
            compare.put("count", goods.get("quantity"));
            compares.add(compare);
        }
        orderCompareMapper.batchInsert(compares);
        return orderId;
    }

    public Map<String, Object> getOrdersByUserId(String userId, int currentPage, int pageSize) {
        // 参数校验
        if(currentPage < 1) currentPage = 1;
        if(pageSize < 1) pageSize = 10;

        // 计算分页偏移量（MySQL风格）
        int offset = (currentPage - 1) * pageSize;

        // 1. 查询分页数据
        List<Map<String, Object>> orders = orderMapper.selectByUserIdWithPagination(
                userId,
                offset,
                pageSize
        );

        // 2. 查询总记录数
        int total = orderMapper.selectCountByUserId(userId);

        // 3. 补充商品信息
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Map<String, Object> order : orders) {
            List<Map<String, Object>> goods = orderCompareMapper.selectByOrderId(
                    (String) order.get("order_id")
            );
            goods.forEach(g -> {
                Map<String, Object> goods_mesg = orderGoodsMapper.selectGoodsById(g.get("goods_id").toString());
                g.remove("order_Id");
                g.put("image",goods_mesg.get("image"));
                g.put("name",goods_mesg.get("name"));
                g.put("shop",goods_mesg.get("shop"));
                g.put("price",goods_mesg.get("price"));
                g.put("sort",goods_mesg.get("sort"));
            });

            Map<String, Object> orderWithGoods = new LinkedHashMap<>(order);
            orderWithGoods.put("goods", goods);
            resultList.add(orderWithGoods);
        }



        // 4. 构造分页结果
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", total);
        result.put("order_mesg", resultList);
        return result;
    }

    private synchronized String generateUniqueOrderId() {
        // 用最大ID+1的方式生成（需要数据库配合）
        Integer maxId = orderMapper.getMaxOrderIdSuffix();
        if (maxId == null) maxId = 100000;
        return "1" + String.format("%05d", maxId + 1);
    }
}