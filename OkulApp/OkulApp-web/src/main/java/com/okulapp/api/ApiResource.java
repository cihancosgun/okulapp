/*
 * 
 * 
 * 
 */
package com.okulapp.api;

import com.mongodb.BasicDBObject;
import com.okulapp.data.okul.MyDataSBLocal;
import com.okulapp.notify.BoardUtil;
import com.okulapp.security.SecurityUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

/**
 * REST Web Service
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
@Path("api")
@RequestScoped
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
public class ApiResource {

    @Context
    private UriInfo uriInfo;

    @Inject
    private KeyGenerator keyGenerator;

    @EJB
    MyDataSBLocal myDataSB;

    /**
     * Creates a new instance of ApiResource
     */
    public ApiResource() {
    }

    @POST
    @Path("/login")
    @Consumes(APPLICATION_FORM_URLENCODED)
    public Response authenticateUser(@FormParam("login") String login,
            @FormParam("password") String password, @Context HttpServletRequest request) {
        try {

            // Authenticate the user using the credentials provided
            // authenticate(login, password);
            request.login(login, password);

            // Issue a token for the user
            String token = issueToken(login);

            //MyLoginBean myLoginBean = new MyLoginBean(login, token);
            // Return the token on the response
            return Response.ok().header(AUTHORIZATION, "Bearer " + token).build();

        } catch (Exception e) {
            return Response.status(UNAUTHORIZED).build();
        }
    }

    private String issueToken(String login) {
        Key key = keyGenerator.generateKey();
        String jwtToken = Jwts.builder()
                .setSubject(login)
                .setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date())
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        return jwtToken;
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    @GET
    @Path("/getUserRole")
    @Produces(APPLICATION_JSON)
    @JWTTokenNeeded
    public String getUserRole(@Context HttpServletRequest request) {
        Jws<Claims> claim = TokenUtil.parseMyToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        return new BasicDBObject("role", SecurityUtil.getUserRoleFromUserTable(myDataSB, claim.getBody().getSubject())).toJson();
    }

    @GET
    @Path("/getBoardOfUser")
    @Produces(APPLICATION_JSON)
    @JWTTokenNeeded
    public String getBoardOfUser(@Context HttpServletRequest request) {
        Jws<Claims> claim = TokenUtil.parseMyToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        return new BasicDBObject("list", BoardUtil.getBoardOfUser(myDataSB, claim.getBody().getSubject())).toJson();
    }

    @GET
    @Path("/getUnreadedBoard")
    @Produces(APPLICATION_JSON)
    @JWTTokenNeeded
    public String getUnreadedBoard(@Context HttpServletRequest request) {
        Jws<Claims> claim = TokenUtil.parseMyToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        return new BasicDBObject("list", BoardUtil.getUnreadedBoard(myDataSB, claim.getBody().getSubject())).toJson();
    }

    @GET
    @Path("/hello")
    @Produces(APPLICATION_JSON)
    @JWTTokenNeeded
    public String hello(@QueryParam("message") String message, @Context HttpServletRequest request) {
        return new BasicDBObject("return", message).toJson();
    }

}
