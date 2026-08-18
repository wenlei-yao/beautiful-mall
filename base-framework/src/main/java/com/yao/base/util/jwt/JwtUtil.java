package com.yao.base.util.jwt;

import com.yao.base.framework.config.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.jsonwebtoken.Jwts;

public class JwtUtil {

    private final JwtProperties jwtProperties;

    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }
    // 根据密钥生成 HMAC-SHA 密钥对
    public  SecretKey getSecretKey() {
        byte[] bytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);
    }

    /**
     * 生成 Token
     * @param userId 用户ID
     * @param claims 额外自定义Payload数据 (如 roles, username 等)
     * @param expirationMs 过期时间(毫秒)
     */
    public String generateToken(Long userId, Map<String, Object> claims, long expirationMs) {
        if (claims == null) {
            claims = new HashMap<>();
        }
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(userId)) // 将 userId 设为 Subject
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(Long userId,long expirationMs) {
        return generateToken(userId, null, expirationMs);
    }

    /**
     * 解析并校验 Token（若过期或被篡改，抛出异常）
     */
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    /**
     * 获取 Token 的过期时间 (Date 类型)
     */
    public Date getExpirationDate(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration(); // 直接获取 exp 字段并转为 Date
    }
    /**
     * 获取 Token 剩余的有效时间（单位：毫秒）
     * 常用场景：存入 Redis 黑名单时设置 TTL 过期时间
     */
    public long getRemainExpirationMs(String token) {
        Date expiration = getExpirationDate(token);
        long remainMs = expiration.getTime() - System.currentTimeMillis();
        // 如果已经过期则返回 0，未过期返回剩余毫秒数
        return Math.max(remainMs, 0L);
    }
    /**
     * 校验 Token 是否有效
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Token 过期、签名错误、格式不正确等情况
            return false;
        }
    }

    /**
     * 从 Token 中直接获取 UserId
     */
    public Long getUserId(String token) {
        Claims claims = parseToken(token);
        return Long.parseLong(claims.getSubject());
    }


}