package cn.edu.glut.lpj.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.File;

@Configuration
public class FileConfig {
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Bean
    public String imageStoragePath() {
        // 获取JAR所在目录的绝对路径
        String path = System.getProperty("user.dir") + File.separator + uploadDir;
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return path;
    }
}