package com.custempmanag.librarymanagmentsystem.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "1853e68ac644c51927dd451effb2fd4e52f5b6501252dd3e8363eb5f8de507ea28a8a26bad990f8fa6d746871a5cf42f66e622124982a587fa33acc2df34385f361c4125613ef42fd0eeb5e712639a98d20ef445a870b76e69b30817c4bf8ccdea636712b036a6427c46322743c3f98a05bd16afcda79ada2e10e0e7489ed9eec490a8809cca5dc124cc8134dfee110a29a1b0ebc817fb244788868fe2bed0fbe1797067c811c23afd15c73442e3e7c15225ac9f9aa7482267521484e15feecae2fca05bf410dab31eee9e0fe0538a48945cedd792b0f672a875ce30f9776ec8931df412f343dc61ec3847176c6d9883d4f22a8a9039beeaa632eab3e1b9aaf5";
    private final long EXPIRATION_TIME = 86400000;

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String subject){
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token){
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
}
