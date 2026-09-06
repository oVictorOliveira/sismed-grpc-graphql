package dev.sismed.aggregator.security.servico;

import dev.sismed.aggregator.security.AuthenticatedUsuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${sismed.jwt.segredo}")
    private String segredo;

    @Value("${sismed.jwt.expiracao-ms:28800000}")
    private long expiracaoMs;

    public String gerarToken(AuthenticatedUsuario usuario) {
        return Jwts.builder()
                .subject(usuario.getUsername())
                .claim("papel", usuario.getPapel().name())
                .claim("pacienteId", usuario.getPacienteId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiracaoMs))
                .signWith(chave())
                .compact();
    }

    public String extrairLogin(String token) {
        return claims(token).getSubject();
    }

    public boolean tokenValido(String token) {
        try {
            claims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims claims(String token) {
        return Jwts.parser()
                .verifyWith(chave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey chave() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(segredo));
    }
}
