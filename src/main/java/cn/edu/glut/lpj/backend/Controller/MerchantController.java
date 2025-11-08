package cn.edu.glut.lpj.backend.Controller;

import cn.edu.glut.lpj.backend.Service.Impl.MerchantService;
import cn.edu.glut.lpj.backend.pojo.Store;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class MerchantController {
    //TODO 商家登录
    @Autowired
    private MerchantService merchantService;
    @PostMapping("/storeLogin")
    public ResponseEntity<?> login(@RequestBody Store s){
        log.info("store: "+s.getStore_id() +" trying to login");
        try {
            String token = merchantService.authStore(s.getStore_id(),s.getLogin_code());
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.info("login failed caused by:"+e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("code", 401);
            error.put("message", e.getMessage());
            return ResponseEntity.status(401).body(error);
        }
    }
    @PostMapping("store/add")
    public ResponseEntity<?> addStore(@RequestBody Store store) {
        try {
            merchantService.addStore(store);
            return ResponseEntity.ok("添加成功");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("store/delete")
    public ResponseEntity<?> deleteStore(@RequestBody Map<String,String>  msg) {
        if (merchantService.deleteStore(msg.get("storeId")) > 0) {
            return ResponseEntity.ok("删除成功");
        } else {
            return ResponseEntity.badRequest().body("删除失败");
        }
    }

    @PostMapping("store/update")
    public ResponseEntity<?> updateStore(@RequestBody Store store) {
        if (merchantService.updateStore(store) > 0) {
            return ResponseEntity.ok("更新成功");
        } else {
            return ResponseEntity.badRequest().body("更新失败");
        }
    }

    @GetMapping("store/all")
    public ResponseEntity<List<Store>> getAllStores() {
        return ResponseEntity.ok(merchantService.getAllStores());
    }

}
