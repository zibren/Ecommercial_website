package cn.edu.glut.lpj.backend.Controller;

import cn.edu.glut.lpj.backend.Service.Impl.GoodsService;
import cn.edu.glut.lpj.backend.pojo.DOT.GoodsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    // 根据ID查询商品（GET）
    @GetMapping("/goods")
    public ResponseEntity<?> getGoodsById(@RequestParam String id) {
        Map<String, Object> goods = goodsService.getGoodsById(id);
        return ResponseEntity.ok(goods);
    }

    // 根据商店ID查询商品（POST）
    @PostMapping("/goods")
    public ResponseEntity<?> getGoodsByShop(@RequestBody Map<String, String> store_id) {
        String shopId = store_id.get("store_id");
        List<Map<String, Object>> goodsList = goodsService.getGoodsByShop(shopId);
        return ResponseEntity.ok(goodsList);
    }

    // 添加商品信息（包含图片上传）
    @PostMapping("/goodsInser")
    public ResponseEntity<?> insertGoods(@ModelAttribute GoodsDTO goodsDTO) throws Exception {
        String imagePath = goodsService.saveImage(goodsDTO.getImage());
        goodsService.insertGoods(goodsDTO, imagePath);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据分类ID查询商品列表
     * @param sort 分类ID（数字）或"all"表示查询所有
     * @return 商品列表
     */
    @GetMapping("/goods/sort")
    public ResponseEntity<?> getGoodsBySort(@RequestParam String sort) {
        List<Map<String, Object>> goodsList;
        if ("".equals(sort) || "all".equalsIgnoreCase(sort) ) {
            goodsList = goodsService.getAllGoods();
        } else {
            goodsList = goodsService.getGoodsBySort(sort);
        }
        return ResponseEntity.ok(goodsList);
    }
    @PostMapping("/goodsUpdate")
    public ResponseEntity<?> updateGoods(@ModelAttribute GoodsDTO goodsDTO) throws Exception {
        // 图片处理：如果上传了新图片则保存，否则保留原路径
        String imagePath = goodsDTO.getImage() != null && !goodsDTO.getImage().isEmpty()
                ? goodsService.saveImage(goodsDTO.getImage())
                : null;

        goodsService.updateGoods(goodsDTO, imagePath);
        return ResponseEntity.ok("商品更新成功");
    }
}