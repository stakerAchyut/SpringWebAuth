package com.akshat.web.service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;

@Service
public class JwtService {
	
	private static final KeyPair KEY_PAIR = getKeyPair().generateKeyPair();
	private static final PrivateKey PRIVATE_KEY = KEY_PAIR.getPrivate();
	private static final PublicKey PUBLIC_KEY = KEY_PAIR.getPublic();
			
	private static KeyPairGenerator getKeyPair() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(2048);
			return generator;
		} catch (Exception e) {
			throw new RuntimeException("Error generating RSA key pair", e);
		}
	}

	public String generateToken(String username) throws InvalidKeyException, NoSuchAlgorithmException {
		
		Map<String, Object> claims = new HashMap<>();
		
		return Jwts.builder()
					.claims(claims)
					.subject(username)
					.issuedAt(new Date())
					.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
					.signWith(PRIVATE_KEY)
					.compact();
	}
		
	public String extractUsername(String token) {
		Jws<Claims> jwsClaims = Jwts.parser()
									.verifyWith(PUBLIC_KEY)
									.build()
									.parseSignedClaims(token);
		return jwsClaims.getPayload().getSubject();
	}
	
	public boolean validateToken(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		return username.equals(userDetails.getUsername());
	}

}
