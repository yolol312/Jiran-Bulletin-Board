package com.example.jiranbulletinboard.Security.SessionTokenManager;

import com.example.jiranbulletinboard.Security.SessionToken.AccessToken;
import com.example.jiranbulletinboard.Security.SessionToken.RefreshToken;
import com.example.jiranbulletinboard.Security.SessionToken.SessionToken;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.Map;

public class RefreshTokenManager implements SessionTokenManager {

    private final RSAPublicKey authPublicKey;
    private final RSAPublicKey signPublicKey;
    private final RSAPrivateKey authPrivateKey;
    private final RSAPrivateKey signPrivateKey;
    private final long jwtExpirationInMs = 604800000; // 1 hour

    public RefreshTokenManager() throws Exception {
        KeyPairGenerator authKeyGen = KeyPairGenerator.getInstance("RSA");
        authKeyGen.initialize(2048);
        KeyPair authKeyPair = authKeyGen.generateKeyPair();
        this.authPublicKey = (RSAPublicKey) authKeyPair.getPublic();
        this.authPrivateKey = (RSAPrivateKey) authKeyPair.getPrivate();

        KeyPairGenerator signKeyGen = KeyPairGenerator.getInstance("RSA");
        signKeyGen.initialize(2048);
        KeyPair signKeyPair = signKeyGen.generateKeyPair();
        this.signPublicKey = (RSAPublicKey) signKeyPair.getPublic();
        this.signPrivateKey = (RSAPrivateKey) signKeyPair.getPrivate();
    }

    @Override
    public RefreshToken generateToken(String key, Object data) {
        if (!(data instanceof Map)) {
            throw new IllegalArgumentException("Data must be of type Map<String, Object>");
        }
        Map<String, Object> dataMap = (Map<String, Object>) data;

        try {
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(key)
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                    .claim("data", dataMap)
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);
            JWSSigner signer = new RSASSASigner(signPrivateKey);
            signedJWT.sign(signer);

            JWEObject jweObject = new JWEObject(
                    new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM).contentType("JWT").build(),
                    new Payload(signedJWT)
            );

            jweObject.encrypt(new RSAEncrypter(authPublicKey));

            return new RefreshToken(jweObject.serialize());
        } catch (Exception e) {
            throw new RuntimeException("Error generating JWE token", e);
        }
    }

    @Override
    public boolean validateToken(SessionToken token) {
        try {
            JWEObject jweObject = JWEObject.parse(token.getToken());
            jweObject.decrypt(new RSADecrypter(authPrivateKey));

            SignedJWT signedJWT = jweObject.getPayload().toSignedJWT();
            return signedJWT.verify(new RSASSAVerifier(signPublicKey));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getSubjectFromToken(SessionToken token) {
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }

    @Override
    public Claims extractClaims(SessionToken token) {
        try {
            JWEObject jweObject = JWEObject.parse(token.getToken());
            jweObject.decrypt(new RSADecrypter(authPrivateKey));

            SignedJWT signedJWT = jweObject.getPayload().toSignedJWT();
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();

            // Convert JWTClaimsSet to Claims
            return new DefaultClaims(jwtClaimsSet.getClaims());
        } catch (Exception e) {
            throw new RuntimeException("Error extracting claims from JWE token", e);
        }
    }
}