package uz.darvoza.baxrin_darvoza.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import uz.darvoza.baxrin_darvoza.dto.JwtDTO;
import uz.darvoza.baxrin_darvoza.enums.Roles;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class JwtUtil {
    private static final int tokenLiveTime = 1000* 60*60* 24; // 1-day
    private static final String secretKey = "veryLongSecretmazgillattayevlasharaaxmojonjinnijonsurbetbekkiydirhonuxlatdibekloxovdangasabekochkozjonduxovmashaynikmaydagapchishularnioqiganbolsangizgapyoqaniqsizmazgi";

    public static String encode(String userName,Integer id, List<Roles> roleList) {
        String strRole=roleList.stream().map(item -> item.name()).collect(
                Collectors.joining(","));

        Map<String,String> claims = new HashMap<>();
        claims.put("roles", strRole);
        claims.put("id", String.valueOf(id));
        return Jwts
                .builder()
                .subject(userName)
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenLiveTime))
                .signWith(getSignInKey())
                .compact();
    }

    public static JwtDTO decode(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String userName = String.valueOf(claims.getSubject());
        Integer id = Integer.valueOf(claims.get("id").toString());
        String strRoleList=String.valueOf(claims.get("roles"));
        List<Roles> roleArr= Arrays.stream(strRoleList
                        .split(","))
                .map(Roles :: valueOf)
                .toList();
        return new JwtDTO(userName,id, roleArr);

    }

    public static String encode(Integer id) {

        return Jwts
                .builder()
                .subject(String.valueOf(id))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000* 60*60))
                .signWith(getSignInKey())
                .compact();
    }

    public static Integer decodeRegVerToken(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return   Integer.valueOf(claims.getSubject());

    }

    private static SecretKey getSignInKey() {
        // Use the secret key bytes directly for HMAC
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
