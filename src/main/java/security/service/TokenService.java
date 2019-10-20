package security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import repository.entities.Role;
import repository.entities.User;
import security.exception.AuthorizationException;
import security.model.TokenDetails;

import javax.enterprise.context.ApplicationScoped;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class TokenService {

    // TODO: Create PropertyProducer for properties from application.properties file
    private final String secret = "SomeSecret";

    public String createToken(TokenDetails tokenDetails) {
        return Jwts.builder()
                .setId(tokenDetails.getId())
                .setSubject(tokenDetails.getUsername())
                .setIssuedAt(Date.from(tokenDetails.getIssuedTime().toInstant()))
                .claim("roles", tokenDetails.getRoles())
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String createTokenForUser(User user) {
        TokenDetails tokenDetails = TokenDetails.builder()
                .id(user.getId().toString())
                .username(user.getName())
                .issuedTime(ZonedDateTime.now())
                .roles(getRoles(user))
                .build();
        return createToken(tokenDetails);
    }

    public TokenDetails parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            Set<String> roles = Set.copyOf((List<String>) claims.getOrDefault("roles", List.of()));
            String username = claims.getSubject();
            String id = claims.getId();
            Date issuedAt = claims.getIssuedAt();

            return TokenDetails.builder()
                    .id(id)
                    .username(username)
                    .roles(roles)
                    .issuedTime(getIssuedTime(issuedAt))
                    .build();
        } catch (ExpiredJwtException
                | MalformedJwtException
                | SignatureException
                | UnsupportedJwtException
                | IllegalArgumentException e) {
            throw new AuthorizationException("Unable to parse token", e);
        }
    }

    public Set<String> getRoles(User user) {
        return user.getUserCredentials().getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());
    }

    private ZonedDateTime getIssuedTime(Date issuedAt) {
        return ZonedDateTime.ofInstant(issuedAt.toInstant(), ZoneId.systemDefault());
    }
}
