package com.qinyou.apiserver.core.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Getter
@Setter
public class JwtService {

    @Value("${app.jwt.expire-idle}")
    Integer expireIdle;  // 过期时间，单位小时

    @Value("${app.jwt.secret}")
    String secret;      // 加密密钥

    /**
     * 生成签名
     *
     * @param username 用户名
     * @return 加密的token
     */
    public String generate(String username) {
        Date expireAt = new Date(System.currentTimeMillis() + expireIdle * 3600 * 1000);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create().withClaim("sub", username)
                .withClaim("iat", new Date())
                .withExpiresAt(expireAt)
                .sign(algorithm);
    }

    /**
     * 刷新 token
     * 仅当 token无篡改  返回 刷新后的token
     *
     * @param token
     * @return
     */
    public String refresh(String token) {
        boolean flag = false;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            verifier.verify(token);
            // token 有效 且 为 过期
            flag = true;
        } catch (TokenExpiredException e) {
            // token 过期，返回刷新后的 token
            flag = true;
        } catch (Exception e) {
            // token 被篡改
        }
        if (flag) {
            return generate(getUsername(token));
        } else {
            return null;
        }
    }


    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 是否正确
     */
    public boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            // 过期
            // 被修改 （可以记录警告日志）
            // 等等
            exception.printStackTrace();  // 不记录到日志文件
            return false;
        }
    }


    /**
     * 获得token中 sub （用户名）
     *
     * @return token中包含的用户名
     */
    public String getUsername(String token) {
        return get(token, "sub");
    }

    /**
     * 获得token中的指定KEY值信息，不验证token 合法性
     */
    public String get(String token, String key) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(key).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    /**
     * token 是否过期, 不验证token 合法性
     *
     * @param token
     * @return
     */
    public boolean isExpired(String token) {
        boolean flag;
        try {
            DecodedJWT jwt = JWT.decode(token);
            Long exp = jwt.getClaim("exp").asLong() * 1000;
            Date d = new Date(exp);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(dateFormat.format(d));
            flag = d.before(new Date());
        } catch (JWTDecodeException e) {
            flag = true;
        }
        return flag;
    }
}
