package cn.edu.glut.lpj.backend.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    void insertOrder(Map<String, Object> order);
    List<Map<String, Object>> selectByUserId(String userId);
    Integer getMaxOrderIdSuffix();
    // 分页查询（带起始位置和数量）
    List<Map<String, Object>> selectByUserIdWithPagination(
            @Param("userId") String userId,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    // 总数查询
    int selectCountByUserId(@Param("userId") String userId);
}