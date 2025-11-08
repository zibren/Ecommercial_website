package cn.edu.glut.lpj.backend.Mapper;

import cn.edu.glut.lpj.backend.pojo.Goods;
import cn.edu.glut.lpj.backend.pojo.Store;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface MerchantMapper {
    //按id获取某商户
    @Select("SELECT store_id,login_code,brand from stores where store_id =#{id};")
    public Store getStoreById(String id);
    // 插入商户
    int insertStore(Store store);
    //删除商户
    int deleteStore(String storeId);
//    更新商户
    int updateStore(Store store);
//    获取所有商户
    List<Store> getAllStores();
//    获取所有商户通过id
    List<String> getAllStoreIds();

}
