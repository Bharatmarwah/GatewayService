package in.bm.GatewayService.SERVICE;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtFilter {

    private static final String SECRET_KEY = System.getenv("SECRET_KEY");

    private static final String ISSUER = "kitflik-auth-service";

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(Claims claims) {
        try {
            boolean isAccess = "ACCESS".equals(claims.get("type", String.class));
            boolean validIssuer = ISSUER.equals(claims.getIssuer());
            return isAccess && validIssuer;
        } catch (Exception e) {
            return false;
        }
    }
}
