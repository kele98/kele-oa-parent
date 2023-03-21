package top.aikele.common.reslut.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
@Slf4j
public class JWTHelper {
    //30分钟过期
    private static long tokenExpiration = 60*30;
    private static String tokenSignKey = "kele1234kele1234kele1234kele1234kele1234kele1234kele1234kele1234kele1234kele1234kele1234kele1234kele1234kele1234kele1234kele1234kele1234kele1234";
    public static String creatToken(Long userID,String username){
        String token = Jwts.builder()
                .setSubject("auth-user")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration*1000))
                .claim("userId", userID)
                .claim("username", username)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();

        return token;
    }
    public static Long getUserId(String token){
        if(StringUtils.isEmpty(token))
            return null;
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        Integer userId = (Integer) claims.get("userId");
        return userId.longValue();
    }
    public static String getUsername(String token) {
        try {
            if (StringUtils.isEmpty(token)) return "";
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return (String) claims.get("username");
        } catch (ExpiredJwtException e) {
            log.error("token{}过期", token, e);
            throw new RuntimeException("token{}过期");
        } catch (SignatureException e) {
            log.error("token=[{}], 签名", token, e);
            throw new RuntimeException("token{}签名错误");
        } catch (Exception e) {
            log.error("token=[{}]解析错误 message:{}", token, e.getMessage(), e);
            throw new RuntimeException("token{}解析错误");
        }
    }
}
