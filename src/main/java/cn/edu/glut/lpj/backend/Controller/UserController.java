package cn.edu.glut.lpj.backend.Controller;
import cn.edu.glut.lpj.backend.Service.Impl.AuthService;
import cn.edu.glut.lpj.backend.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
/*
* */
@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        log.info("user: "+loginRequest.getNickname() +" trying to login");
        try {
            String token = authService.authenticate(loginRequest);
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
    @PostMapping("/registry")
    public ResponseEntity<?> registry(@RequestBody User registryRequest){
        log.info("user:"+registryRequest.getNickname() +"trying to registry");
        try {
            authService.registryUser(registryRequest);
            log.info("user:"+registryRequest.getNickname() +"successfully registry");
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            log.info("registry failed caused by:"+e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("code", 401);
            error.put("message", e.getMessage());
            return ResponseEntity.status(401).body(error);
        }

    }
}