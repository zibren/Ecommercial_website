package cn.edu.glut.lpj.backend.Controller;

import cn.edu.glut.lpj.backend.Service.Impl.OrderService;
import cn.edu.glut.lpj.backend.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping
    public Result createOrder(@RequestBody Map<String, Object> request) {
        String orderId = orderService.createOrder(request);
        return Result.success(orderId);
    }
    @GetMapping("/all")
    public Result getOrdersByUser(
            @RequestParam String user_id,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize) {

        Map<String, Object> result = orderService.getOrdersByUserId(user_id, currentPage, pageSize);
        return Result.success(result);
    }
}