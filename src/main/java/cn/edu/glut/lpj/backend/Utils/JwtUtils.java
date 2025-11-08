package cn.edu.glut.lpj.backend.Utils;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.util.Date;

import java.util.Map;
import java.util.Random;
@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(String username,Map<String, Object> data) {
//        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(data)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.error("Token已过期: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("不支持的Token格式: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Token格式错误: {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("签名验证失败: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("非法参数: {}", e.getMessage());
        }
        return null;
    }

    // 固定盐值（可以修改）
    private static final String SALT_PREFIX = "XQ@";
    private static final String SALT_SUFFIX = "#!Z";

    // 混淆方法
    public static String obfuscate(String input) {
        // 步骤1：加盐
        String salted = SALT_PREFIX + input + SALT_SUFFIX;

        // 步骤2：转换为字符数组
        char[] chars = salted.toCharArray();

        // 步骤3：简单换位（每隔两个字符交换位置）
        for (int i = 0; i < chars.length - 2; i += 2) {
            char temp = chars[i];
            chars[i] = chars[i + 2];
            chars[i + 2] = temp;
        }

        // 步骤4：添加随机干扰字符
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            sb.append(c);
            sb.append((char) (random.nextInt(26) + 65)); // 添加随机大写字母
        }

        return sb.toString();
    }

    public static String deobfuscate(String input) {
        // 步骤1：移除干扰字符
        StringBuilder filtered = new StringBuilder();
        for (int i = 0; i < input.length(); i += 2) {
            filtered.append(input.charAt(i));
        }

        // 步骤2：反向换位
        char[] chars = filtered.toString().toCharArray();
        for (int i = chars.length - 3; i >= 0; i -= 2) {
            char temp = chars[i];
            chars[i] = chars[i + 2];
            chars[i + 2] = temp;
        }

        // 步骤3：去除盐值
        String result = new String(chars);
        return result.substring(
                SALT_PREFIX.length(),
                result.length() - SALT_SUFFIX.length()
        );
    }
}