/*
 * 
 * 
 * 
 */
package com.okulapp.api;

import com.mongodb.BasicDBObject;
import com.okulapp.chat.ChatUtil;
import com.okulapp.data.okul.MyDataSBLocal;
import com.okulapp.notify.BoardUtil;
import com.okulapp.notify.NotifyUtil;
import com.okulapp.security.SecurityUtil;
import com.okulapp.utils.ByteArrayUploadedFile;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.bson.types.ObjectId;
import java.util.List;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.ws.rs.core.MediaType;
import org.primefaces.model.UploadedFile;

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
        return new BasicDBObject("list", BoardUtil.getBoardOfUser(myDataSB, claim.getBody().getSubject(), false, false)).toJson();
    }

    @GET
    @Path("/getConversations")
    @Produces(APPLICATION_JSON)
    @JWTTokenNeeded
    public String getConversations(@QueryParam("searchText") String searchText, @Context HttpServletRequest request) {
        Jws<Claims> claim = TokenUtil.parseMyToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        return new BasicDBObject("list", ChatUtil.getConversations(myDataSB, claim.getBody().getSubject(), searchText)).toJson();
    }

    @GET
    @Path("/getChat")
    @Produces(APPLICATION_JSON)
    @JWTTokenNeeded
    public String getChat(@QueryParam("chatId") String chatId, @Context HttpServletRequest request) {
        Jws<Claims> claim = TokenUtil.parseMyToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        return new BasicDBObject("result", ChatUtil.getChat(myDataSB, chatId, claim.getBody().getSubject())).toJson();
    }

    @POST
    @Path("/addMessageToChat")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @JWTTokenNeeded
    public String addMessageToChat(String jsonData, @Context HttpServletRequest request) {
        Jws<Claims> claim = TokenUtil.parseMyToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        BasicDBObject dbo = BasicDBObject.parse(jsonData);
        Map<String, Object> user = SecurityUtil.getUserFromEmail(myDataSB, claim.getBody().getSubject());
        ChatUtil.addMessageToConversation(myDataSB, dbo.getString("message"), (ObjectId) dbo.get("chatId"), claim.getBody().getSubject(), user.get("nameSurname").toString());
        return new BasicDBObject("result", true).toJson();
    }

    @POST
    @Path("/startNewChat")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @JWTTokenNeeded
    public String startNewChat(String jsonData, @Context HttpServletRequest request) {
        Jws<Claims> claim = TokenUtil.parseMyToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        BasicDBObject dbo = BasicDBObject.parse(jsonData);
        Map<String, Object> user = SecurityUtil.getUserFromEmail(myDataSB, claim.getBody().getSubject());
        Map<String, Object> receiver = SecurityUtil.getUserFromEmail(myDataSB, dbo.getString("receiverEmail"));
        Map<String, Object> chat = ChatUtil.startNewConversation(myDataSB, receiver, user);
        return new BasicDBObject("result", chat).toJson();
    }

    @GET
    @Path("/getClasses")
    @Produces(APPLICATION_JSON)
    @JWTTokenNeeded
    public String getClasses(@QueryParam("searchText") String searchText, @Context HttpServletRequest request) {
        Jws<Claims> claim = TokenUtil.parseMyToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        ObjectId branchOfUser = SecurityUtil.getBranchOfUser(myDataSB, claim.getBody().getSubject());
        if (branchOfUser != null) {
            return new BasicDBObject("result", ChatUtil.getClasses(myDataSB, claim.getBody().getSubject(), searchText, branchOfUser)).toJson();
        } else {
            return new BasicDBObject("result", null).toJson();
        }
    }

    @GET
    @Path("/getTeachers")
    @Produces(APPLICATION_JSON)
    @JWTTokenNeeded
    public String getTeachers(@QueryParam("searchText") String searchText, @Context HttpServletRequest request) {
        Jws<Claims> claim = TokenUtil.parseMyToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        ObjectId branchOfUser = SecurityUtil.getBranchOfUser(myDataSB, claim.getBody().getSubject());
        if (branchOfUser != null) {
            return new BasicDBObject("result", ChatUtil.getTeachers(myDataSB, claim.getBody().getSubject(), searchText, branchOfUser)).toJson();
        } else {
            return new BasicDBObject("result", null).toJson();
        }
    }

    @GET
    @Path("/getStudentParents")
    @Produces(APPLICATION_JSON)
    @JWTTokenNeeded
    public String getStudentParents(@QueryParam("searchText") String searchText, @Context HttpServletRequest request) {
        Jws<Claims> claim = TokenUtil.parseMyToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        ObjectId branchOfUser = SecurityUtil.getBranchOfUser(myDataSB, claim.getBody().getSubject());
        if (branchOfUser != null) {
            return new BasicDBObject("result", ChatUtil.getStudentParents(myDataSB, claim.getBody().getSubject(), searchText, branchOfUser)).toJson();
        } else {
            return new BasicDBObject("result", null).toJson();
        }
    }

    @GET
    @Path("/getStudentParentsOfClass")
    @Produces(APPLICATION_JSON)
    @JWTTokenNeeded
    public String getStudentParentsOfClass(@QueryParam("classId") String classId, @Context HttpServletRequest request) {
        if (classId != null) {
            return new BasicDBObject("result", ChatUtil.getStudentParentsOfClass(myDataSB, new ObjectId(classId))).toJson();
        } else {
            return new BasicDBObject("result", null).toJson();
        }
    }

    @GET
    @Path("/getStuffs")
    @Produces(APPLICATION_JSON)
    @JWTTokenNeeded
    public String getStuffs(@QueryParam("searchText") String searchText, @Context HttpServletRequest request) {
        Jws<Claims> claim = TokenUtil.parseMyToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        ObjectId branchOfUser = SecurityUtil.getBranchOfUser(myDataSB, claim.getBody().getSubject());
        if (branchOfUser != null) {
            return new BasicDBObject("result", ChatUtil.getStuffs(myDataSB, claim.getBody().getSubject(), searchText, branchOfUser)).toJson();
        } else {
            return new BasicDBObject("result", null).toJson();
        }
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

    private byte[] inputStreamToByteArray(InputStream initialStream) {
        try {
            byte[] targetArray = new byte[initialStream.available()];
            initialStream.read(targetArray);
            return targetArray;
        } catch (IOException ex) {
            Logger.getLogger(ApiResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @GET
    @Path("/getMessageTypeIcon")
    @Produces("image/png")
    public Response getMessageTypeIcon(@QueryParam("messageType") String messageType) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(messageType.concat(".png"));
        if (is != null) {
            return Response.ok(inputStreamToByteArray(is)).build();
        } else {
            return Response.ok(inputStreamToByteArray(getClass().getClassLoader().getResourceAsStream("board.png"))).build();
        }
    }

    @GET
    @Path("/getImage")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getImage(@QueryParam("fileId") String fileId) {
        if (fileId != null) {
            byte[] bytes = myDataSB.getFileUpDownManager().downloadFile(new ObjectId(fileId));
            return Response.ok(bytes).build();
        } else {
            return Response.noContent().build();
        }
    }

    @POST
    @Path("/uploadImageFile")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @JWTTokenNeeded
    public String uploadImageFile(String jsonData, @Context HttpServletRequest request) {
        try {
            //Jws<Claims> claim = TokenUtil.parseMyToken(request.getHeader(HttpHeaders.AUTHORIZATION));
            BasicDBObject dbo = BasicDBObject.parse(jsonData);
            if (jsonData == null) {
                return new BasicDBObject("error", "Invalid form data").toJson();
            }
            byte[] readedBytes = Base64.getDecoder().decode(dbo.getString("base64"));
            InputStream uploadedInputStream = new ByteArrayInputStream(readedBytes);

            BufferedImage imageOrj = ImageIO.read(uploadedInputStream);
            BufferedImage imageThumb = myDataSB.getFileUpDownManager().resizeImage(imageOrj, 200, 200);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(imageThumb, "jpg", bos);

            String fileName = UUID.randomUUID() + (dbo.getString("mimeType").equals("image/jpeg") ? ".jpg" : "mp4");
            UploadedFile resizedImage = new ByteArrayUploadedFile(bos.toByteArray(), fileName, dbo.getString("mimeType"));
            ObjectId thumbFileId = myDataSB.getFileUpDownManager().uploadFile(resizedImage.getInputstream(), fileName);
            ObjectId fileId = myDataSB.getFileUpDownManager().uploadFile(new ByteArrayInputStream(readedBytes), fileName);

            if (fileId == null) {
                return new BasicDBObject("error", "Invalid form data").toJson();
            }

            return new BasicDBObject("fileId", fileId).append("thumbFileId", thumbFileId).toJson();
        } catch (IOException ex) {
            Logger.getLogger(ApiResource.class.getName()).log(Level.SEVERE, null, ex);
            return new BasicDBObject("error", "Invalid form data").toJson();
        }
    }

    @POST
    @Path("/insertNotifyMessage")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @JWTTokenNeeded
    public String insertNotifyMessage(String jsonData, @Context HttpServletRequest request) {
        Jws<Claims> claim = TokenUtil.parseMyToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        BasicDBObject dbo = BasicDBObject.parse(jsonData);
        Map<String, Object> user = SecurityUtil.getUserFromEmail(myDataSB, claim.getBody().getSubject());
        Map<String, Object> rec = NotifyUtil.insertNotifyMessage(myDataSB, dbo.getString("messageType"), user,
                (List<String>) dbo.get("receivers"), (List<String>) dbo.get("receiversNS"), dbo.getString("message"), (List<ObjectId>) dbo.get("fileIds"), (List<ObjectId>) dbo.get("thumbFileIds"));
        return new BasicDBObject("result", rec).toJson();
    }
}
