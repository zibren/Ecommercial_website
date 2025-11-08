package cn.edu.glut.lpj.backend;
import cn.edu.glut.lpj.backend.Mapper.GoodsMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class BackendApplicationTests {
    @Autowired
    GoodsMapper gm;
    @Test
    void GMtest() {
        System.out.println(gm.selectGoodsByShop("10002"));
    }

}
