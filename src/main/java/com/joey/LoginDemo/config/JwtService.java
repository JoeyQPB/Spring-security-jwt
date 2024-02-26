package com.joey.LoginDemo.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	@Value("${application.security.jwt.secret-key}")
	private String SECRETE_KEY;

	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	// for extration of any claim u need
	public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}
	
	public String generateToken( UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}
	
	public String generateToken( Map<String, Object> extraClaims, UserDetails userDetails) {
		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername()) // username for spring -> u can change in your application like now we are using email to unique value
				.setIssuedAt(new Date(System.currentTimeMillis())) // calculate the token expiration
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // valid for 24 hrs	
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact(); // -> generate and return the token
	}
	
	public boolean isTokenValid(String token, UserDetails userDetails) {
		//validate if the token belongs to user
		final String username = extractUserName(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}
	
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRETE_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}

// JWT EXPLANATION 

/*

- Header
{
  "alg": "HS256", -> sigin algothm being used
  "typ": "JWT" -> type of token
}

- PayLoad -> data / claims (statements about an user)
{
  "sub": "1234567890",
  "name": "John Doe",
  "iat": 1516239022
  "authorities": [
  	"ADMIN",
  	"MANAGER"
  ],
  "extra-claims": "some data here"
}

- VERIFY SIGNATURE -> use to verify jwt
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  your-256-bit-secret
)


*/