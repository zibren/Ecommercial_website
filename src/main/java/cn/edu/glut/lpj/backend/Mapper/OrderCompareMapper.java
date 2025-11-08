package cn.edu.glut.lpj.backend.Mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderCompareMapper {
    void batchInsert(List<Map<String, Object>> compares);
    List<Map<String, Object>> selectByOrderId(String orderId);
    List<Map<String, Object>> selectByOrderIdAndLimitPage(String orderId, Integer pageSize, Integer offset);
}