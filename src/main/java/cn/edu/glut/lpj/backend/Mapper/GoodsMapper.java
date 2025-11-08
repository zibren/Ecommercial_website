package cn.edu.glut.lpj.backend.Mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;
@Mapper
public interface GoodsMapper {
    // 根据ID查询商品详情（多表关联）
    Map<String, Object> selectGoodsById(String id);

    // 根据店铺ID查询商品列表
    List<Map<String, Object>> selectGoodsByShop(String shopId);

    // 插入goods表
    int insertGoods(@Param("id") String id, @Param("name") String name);

    // 插入goodsMesg表
    int insertGoodsMesg(
            @Param("goodsId") String goodsId,
            @Param("storeId") String storeId,
            @Param("picPath") String picPath,
            @Param("sortId") String sortId,
            @Param("price") Double price
    );

    // 插入inventory表
    int insertInventory(
            @Param("storeId") String storeId,
            @Param("goodsId") String goodsId,
            @Param("quantity") Integer quantity
    );
    /**
     * 根据分类ID查询商品
     * @param sortId 分类ID（对应category.sort_id）
     */
    List<Map<String, Object>> selectGoodsBySort(@Param("sortId") String sortId);

    /**
     * 查询所有商品
     */
    List<Map<String, Object>> selectAllGoods();

    // 商品基础信息更新
    int updateGoodsBase(@Param("id") String id, @Param("name") String name);

    // 商品详情更新
    int updateGoodsDetail(
            @Param("goodsId") String goodsId,
            @Param("storeId") String storeId,
            @Param("picPath") String picPath,  // 允许null
            @Param("sortId") String sortId,
            @Param("price") Double price
    );

    // 库存更新
    int updateInventory(
            @Param("storeId") String storeId,
            @Param("goodsId") String goodsId,
            @Param("quantity") Integer quantity
    );

    public List<Map<String,Object>>selectGoodsByListGoodsId(@Param("goods_ids")List<String> goods_ids);
}