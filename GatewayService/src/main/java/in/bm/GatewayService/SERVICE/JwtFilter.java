package in.bm.GatewayService.SERVICE;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtFilter {

    private static final String SECRET_KEY = "YjkxYTczZDgzN2I2NDk4N2MxZWMyNGUwODZjYmY4YzIyZjQzMDE5OWE2NmQ2N2E5M2Q0MzFjYTFmYzU0MGI3MQ==";
    private static final String ISSUER = "kitflik-auth-service";

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(Claims claims) {
        try{

            boolean isAccess = "ACCESS".equals(claims.get("type", String.class));
            boolean validIssuer = ISSUER.equals(claims.getIssuer());

            return isAccess && validIssuer;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims parseClaims(String token){
        return Jwts
                .parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
