package com.changgou.oauth;

import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

public class ParseJwtTest {

    @Test
    public void parseJwt(){
        //基于公钥去解析jwt
        String jwt ="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJhcHAiXSwibmFtZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU5MTY2NTUwMywiYXV0aG9yaXRpZXMiOlsiYWNjb3VudGFudCIsInVzZXIiLCJzYWxlc21hbiJdLCJqdGkiOiJiNWVmYjAxMS1lZTNhLTRmNmEtYjlhOS04MzJjNjQ5ZGU2NTQiLCJjbGllbnRfaWQiOiJjaGFuZ2dvdSIsInVzZXJuYW1lIjoiaGVpbWEifQ.Z-h2q7rgp22NAVATcQH7iJ1CZsqzAtWfB6lQ4aRa9unJWoNmFSeGRcHTnod610FtoQIzwEMWEeKVaLoavEKpnNg9Rt5HxMqrBAQ6eD51aXFVzYSyg5xfmwD8i02YgykngScmtJrs6bp7nC0vO3zrzUV7VXjKeS9KiG7nXsgeCa5hsqOYBl-551gsSxCKLGt2dmAj_7et69B7C8w3liFQ6fHNBqEJtb1C-tjNBasGQ8qMMjEj0vU-Cd3pD5F-TePWqpnPc13nlq2QskcN8Q3xHiaokkoG8WYt2DlQpZ399foW5XxbhPqPiq07fkhpGgtsKP5PEwblirTM4fZFTgEiPQ";

        String publicKey ="-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvFsEiaLvij9C1Mz+oyAmt47whAaRkRu/8kePM+X8760UGU0RMwGti6Z9y3LQ0RvK6I0brXmbGB/RsN38PVnhcP8ZfxGUH26kX0RK+tlrxcrG+HkPYOH4XPAL8Q1lu1n9x3tLcIPxq8ZZtuIyKYEmoLKyMsvTviG5flTpDprT25unWgE4md1kthRWXOnfWHATVY7Y/r4obiOL1mS5bEa/iNKotQNnvIAKtjBM4RlIDWMa6dmz+lHtLtqDD2LF1qwoiSIHI75LQZ/CNYaHCfZSxtOydpNKq8eb1/PGiLNolD4La2zf0/1dlcr5mkesV570NxRmU1tFm8Zd3MZlZmyv9QIDAQAB-----END PUBLIC KEY-----";

        Jwt token = JwtHelper.decodeAndVerify(jwt, new RsaVerifier(publicKey));

        String claims = token.getClaims();
        System.out.println(claims);
    }
}
