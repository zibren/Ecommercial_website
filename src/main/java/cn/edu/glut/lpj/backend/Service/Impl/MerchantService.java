package cn.edu.glut.lpj.backend.Service.Impl;

import cn.edu.glut.lpj.backend.Mapper.MerchantMapper;

import cn.edu.glut.lpj.backend.Utils.JwtUtils;
import cn.edu.glut.lpj.backend.pojo.Store;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class MerchantService {
    private static final Logger log = LoggerFactory.getLogger(MerchantService.class);
    @Autowired
    MerchantMapper merchantMapper;
    @Autowired
    JwtUtils jwt;

    //TODO 商店登录
    public String authStore(String id,String loginCode) throws Exception {
        Store m = merchantMapper.getStoreById(id);
        if (m == null || !m.getLogin_code().equals(loginCode)){
            throw new Exception("no such store or wrong password");
        }
        Map<String,Object> data = new HashMap<>();
        data.put("storeId",id);
        data.put("storeName",m.getBrand());
        data.put("role", "store");
        return jwt.generateToken(m.getBrand(),data);
    }
    //TODO 根据商店di获取上架物品 这个不用了 goods做了

    public void addStore(Store store) {
        // 生成store_id逻辑
        String newStoreId = generateStoreId();
        store.setStore_id(newStoreId);
        store.setCreate_time(new Date());
        if (merchantMapper.insertStore(store) <= 0) {
            throw new RuntimeException("添加失败");
        }
    }

    private String generateStoreId() {
        List<String> existingIds = merchantMapper.getAllStoreIds();
        for (int i = 10001; i <= 10050; i++) {
            String candidate = String.valueOf(i);
            if (!existingIds.contains(candidate)) {
                return candidate;
            }
        }
        throw new RuntimeException("可用的store_id已用尽");
    }


    public int deleteStore(String storeId) {
        return merchantMapper.deleteStore(storeId);
    }


    public int updateStore(Store store) {
        return merchantMapper.updateStore(store);
    }


    public List<Store> getAllStores() {
        return merchantMapper.getAllStores();
    }
}
