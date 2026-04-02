package in.bm.GatewayService.SERVICE;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class JwtFilter {

    private final String publicKey;

    public JwtFilter(@Value("${jwt.public.key}")String publicKey) {
        this.publicKey = publicKey;
    }

    private static final String ISSUER = "kitflik-auth-service";

    private PublicKey publicKey() {
        try {
            byte[] keyByte = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyByte);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePublic(spec);
        }catch (Exception e){
            throw new RuntimeException("Failed to load public key", e);
        }
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(publicKey())
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
