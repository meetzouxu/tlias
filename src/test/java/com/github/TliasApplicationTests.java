package com.github;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// @SpringBootTest
class TliasApplicationTests {

    @Test
    public void testUuid(){
        for (int i = 0; i < 100; i++) {
            String uuid = UUID.randomUUID().toString();
            System.out.println(uuid);
        }
    }

    @Test
    public void testGenJWT(){
        Map<String, Object> Cliams = new HashMap<>();
        Cliams.put("username","zhangsan");
        Cliams.put("id","1");
        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, "github") // 签名算法
                .setClaims(Cliams) //自定义内容（载荷）
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) //设置过期时间为1h
                .compact();
        System.out.println(jwt);
    }

    @Test
    public void testParseJWT() {
        Claims claims = Jwts.parser()
                .setSigningKey("github")
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEiLCJleHAiOjE3MjA1MTk4NzIsInVzZXJuYW1lIjoiemhhbmdzYW4ifQ.rGAvHe18KZ03Mf35eOR61N5hOnytyyHNyh7_9kTlIac")
                .getBody();

        System.out.println(claims);
    }
}
