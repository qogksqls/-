package com.ssafy.web.jwt;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.ssafy.web.service.RedisService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class JwtTokenUtil {
	
	@Autowired
	RedisService redisService;

	private static String secretKey;

	
	 public static final String TOKEN_PREFIX = "Bearer ";
	 public static final String HEADER_STRING = "Authorization";
	 public static final String ISSUER = "sssa606.com";

	 
	 public JwtTokenUtil(@Value("${jwt.secret}")String secretKey) {
		 this.secretKey = secretKey;

	 }
	 
//	 public void setExpirationTime() {
//		 JwtTokenUtil.expirationTime = expirationTime;
//	 }
	 
	 //토큰 검증기  
	 public static JWTVerifier getVerifier() {
		 return JWT.require(Algorithm.HMAC512(secretKey.getBytes()))
				 .withIssuer(ISSUER)
				 .build();
	 }
	 
//	 public String resolveToken(HttpServletRequest request) {
//		 String bearerToken = request.getHeader(HEADER_STRING);
//		 if(bearerToken !=null && bearerToken.startsWith(TOKEN_PREFIX)) {
//			 return bearerToken.substring(7);
//		 }
//		 return null;
//	 }
	 
	 
	 //accessToken 설정 
	 public String createAccessToken(String id) {
		 log.debug("test: jwt-crateToken입니다");
		 Long tokenInvalidTime = 1000L * 60 * 30; // 30분
		 return this.getToken(id, tokenInvalidTime);
	 }
	 
	 //refreshToken 설정
	 public String createRefreshToken(String id) {
		 Long tokenInvalidTime = 1000L * 60 * 60 * 24 * 30; // 30일
		 String refreshToken = this.getToken(id, tokenInvalidTime);
		 //redis 에 토큰을 저장 : get id값 
		 redisService.setValues(id, refreshToken, Duration.ofMillis(tokenInvalidTime));
		 return refreshToken;
	 }
	 
	private String getToken(String id, Long tokenInvalidTime) {
		Date expires = JwtTokenUtil.getTokenExpiration(tokenInvalidTime);
		return JWT.create()
				.withSubject(id) // 발행 유저정보
				.withExpiresAt(expires) // 만료시간 
				.withIssuer(ISSUER) // 발행정보
				.withIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
				.sign(Algorithm.HMAC512(secretKey.getBytes()));
//		 Claims claims = Jwts.claims().setSubject(id);
//		 Date date = new Date();
//		 return Jwts.builder()
//				 .setClaims(claims) // 발행 유저 정보 
//				 .setIssuedAt(date) // 발행 시간 
//				 .setIssuer(ISSUER)
//				 .setExpiration(new Date(date.getTime()+tokenInvalidTime)) // 토큰 유효 시간 
//				 .signWith(SignatureAlgorithm.HS256, secretKey)// 해싱 알고리즘 + 키로 서명
//				 .compact();
	}
	 
	public static String getToken(Instant expires, String id) {
		return JWT.create()
		         .withSubject(id)
		         .withExpiresAt(Date.from(expires))
		         .withIssuer(ISSUER)
		         .withIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
		         .sign(Algorithm.HMAC512(secretKey.getBytes()));
		}

	 // 만료 날짜 : 현재시간 + 유효기간 
	 public static Date getTokenExpiration(Long tokenInvalidTime) {
		 Date now = new Date();
		 return new Date(now.getTime() + tokenInvalidTime);
	 }

	 
	 
	 //================================================
	 public static void handleError(String token) {
		    JWTVerifier verifier = JWT
		            .require(Algorithm.HMAC512(secretKey.getBytes()))
		            .withIssuer(ISSUER)
		            .build();
		
		    try {
		        verifier.verify(token.replace(TOKEN_PREFIX, ""));
		    } catch (AlgorithmMismatchException ex) {
		        throw ex;
		    } catch (InvalidClaimException ex) {
		        throw ex;
		    } catch (SignatureGenerationException ex) {
		        throw ex;
		    } catch (SignatureVerificationException ex) {
		        throw ex;
		    } catch (TokenExpiredException ex) {
		        throw ex;
		    } catch (JWTCreationException ex) {
		        throw ex;
		    } catch (JWTDecodeException ex) {
		        throw ex;
		    } catch (JWTVerificationException ex) {
		        throw ex;
		    } catch (Exception ex) {
		        throw ex;
		    }
		}
		
		public static void handleError(JWTVerifier verifier, String token) {
		    try {
		        verifier.verify(token.replace(TOKEN_PREFIX, ""));
		    } catch (AlgorithmMismatchException ex) {
		        throw ex;
		    } catch (InvalidClaimException ex) {
		        throw ex;
		    } catch (SignatureGenerationException ex) {
		        throw ex;
		    } catch (SignatureVerificationException ex) {
		        throw ex;
		    } catch (TokenExpiredException ex) {
		        throw ex;
		    } catch (JWTCreationException ex) {
		        throw ex;
		    } catch (JWTDecodeException ex) {
		        throw ex;
		    } catch (JWTVerificationException ex) {
		        throw ex;
		    } catch (Exception ex) {
		        throw ex;
		    }
		}

	 
	
	
}
