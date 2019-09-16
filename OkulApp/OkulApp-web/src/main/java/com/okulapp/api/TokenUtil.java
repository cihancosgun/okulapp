/*
 * 
 * 
 * 
 */
package com.okulapp.api;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.security.Key;
import java.util.logging.Logger;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
public class TokenUtil {

    private static final Logger logger = Logger.getLogger("JWTTokenNeededFilter");

    private static final KeyGenerator keyGenerator = new SimpleKeyGenerator();

    public static Jws<Claims> parseMyToken(String authorizationHeader) {
        // Get the HTTP Authorization header from the request
        logger.info("#### authorizationHeader : " + authorizationHeader);

        // Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            logger.severe("#### invalid authorizationHeader : " + authorizationHeader);
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer".length()).trim();

        try {

            // Validate the token
            Key key = keyGenerator.generateKey();
            Jws<Claims> claimResult = Jwts.parser().setSigningKey(key).parseClaimsJws(token);

            logger.info("#### valid token : " + token);
            return claimResult;

        } catch (Exception e) {
            logger.severe("#### invalid token : " + token);
            return null;
        }
    }
}
