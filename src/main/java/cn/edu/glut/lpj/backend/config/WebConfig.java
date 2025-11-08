package cn.edu.glut.lpj.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.File;
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取应用运行的目录（JAR所在目录或IDE运行时的项目根目录）
        String appDir;
        try {
            // 获取JAR所在目录
            appDir = new File(WebConfig.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI()).getParent();
        } catch (Exception e) {
            // 如果不是JAR运行环境（如IDE中运行），则使用当前工作目录
            appDir = System.getProperty("user.dir");
        }
        //todo 打包要删掉这个
        appDir = System.getProperty("user.dir");
        String externalPath = appDir + File.separator + "img" + File.separator;

        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:" + externalPath);

        // 可选：添加日志输出以调试路径是否正确
        log.info("映射外部资源路径: " + externalPath);
    }
}