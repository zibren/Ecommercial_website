package cn.edu.glut.lpj.backend.pojo.DOT;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDTO {
    private String id;
    private String name;
    private Double price;
    private String shop;  // store_id
    private MultipartFile image;
    private String sort;  // sort_id
    private Integer quantity;
    // Getters & Setters
}