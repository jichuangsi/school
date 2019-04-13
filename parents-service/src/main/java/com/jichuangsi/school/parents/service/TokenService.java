package com.jichuangsi.school.parents.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

@Service
public class TokenService {
    @Resource
    private Algorithm algorithm;
    @Value("${com.jichuangsi.school.token.userClaim}")
    private String userClaim;

    public String createdToken(String user) throws IllegalArgumentException, UnsupportedEncodingException {
        return StringUtils.isEmpty(user)?null:JWT.create().withClaim(userClaim, user).sign(algorithm);
    }

    public boolean checkToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            return false;
        }
    }
}
