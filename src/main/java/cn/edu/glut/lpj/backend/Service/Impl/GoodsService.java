package cn.edu.glut.lpj.backend.Service.Impl;

import cn.edu.glut.lpj.backend.Mapper.GoodsMapper;
import cn.edu.glut.lpj.backend.pojo.DOT.GoodsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private String imageStoragePath;

    @Transactional
    public void insertGoods(GoodsDTO goodsDTO, String imagePath) {
        // 生成唯一ID
        String goodsId = generateUniqueGoodsId();
        goodsDTO.setId(goodsId);
        // 1. 插入goods表
        goodsMapper.insertGoods(goodsDTO.getId(), goodsDTO.getName());

        // 2. 插入goodsMesg表
        goodsMapper.insertGoodsMesg(
                goodsDTO.getId(),
                goodsDTO.getShop(),
                imagePath,
                goodsDTO.getSort(),
                goodsDTO.getPrice()
        );

        // 3. 插入inventory表
        goodsMapper.insertInventory(
                goodsDTO.getShop(),
                goodsDTO.getId(),
                goodsDTO.getQuantity()
        );
    }


    public String saveImage(MultipartFile image) throws Exception {
        try {
            // 1. 确保目录存在
            Path dir = Paths.get(imageStoragePath);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            // 2. 生成带原始扩展名的文件名
            String originalFilename = image.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID() + extension;

            // 3. 保存文件
            Path path = dir.resolve(fileName);
            image.transferTo(path);

            return fileName;
        } catch (Exception e) {
            throw new Exception("图片保存失败: " + e.getMessage(), e);
        }
    }


    public Map<String, Object> getGoodsById(String id) {
        return goodsMapper.selectGoodsById(id);
    }


    public List<Map<String, Object>> getGoodsByShop(String shopId) {
        return goodsMapper.selectGoodsByShop(shopId);
    }

    public List<Map<String, Object>> getGoodsBySort(String sortId) {
        // 参数校验
        if (!sortId.matches("\\d+")) {
            throw new IllegalArgumentException("分类ID必须为数字");
        }
        return goodsMapper.selectGoodsBySort(sortId);
    }

    public List<Map<String, Object>> getAllGoods() {
        return goodsMapper.selectAllGoods();
    }
    @Transactional
    public void updateGoods(GoodsDTO goodsDTO, String imagePath) {

        // 1. 更新goods表基础信息
        goodsMapper.updateGoodsBase(goodsDTO.getId(), goodsDTO.getName());

        // 2. 更新goodsMesg表扩展信息
        goodsMapper.updateGoodsDetail(
                goodsDTO.getId(),
                goodsDTO.getShop(),
                imagePath,  // 可能为null
                goodsDTO.getSort(),
                goodsDTO.getPrice()
        );

        // 3. 更新库存
        goodsMapper.updateInventory(
                goodsDTO.getShop(),
                goodsDTO.getId(),
                goodsDTO.getQuantity()
        );
    }

    private String generateUniqueGoodsId() {
        Random random = new Random();
        int maxAttempts = 100; // 最大尝试次数防止死循环
        int attempts = 0;

        while (attempts < maxAttempts) {
            // 生成10001-19999范围内的ID
            int idNum = 10001 + random.nextInt(9999);
            String candidateId = String.valueOf(idNum);

            // 检查ID是否已存在
            Map<String, Object> existing = goodsMapper.selectGoodsById(candidateId);
            if (existing == null) {
                return candidateId;
            }

            attempts++;
            // 适度延迟防止CPU空转
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        throw new RuntimeException("Failed to generate unique goods ID after " + maxAttempts + " attempts");
    }
}