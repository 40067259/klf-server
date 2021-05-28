package com.example.KLF.server.controller;
import com.example.KLF.server.domain.Report;
import com.example.KLF.server.domain.User;
import com.example.KLF.server.exception.DuplicatedIdException;
import com.example.KLF.server.exception.RepException;
import com.example.KLF.server.service.KlfService;
import com.example.KLF.server.service.MyResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.sql.rowset.serial.SerialBlob;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("user/")
@CrossOrigin
public class UserController implements Serializable {
    private static ArrayList<User> users = new ArrayList<>();
    private static Map<String, String> tokenUsername = new HashMap<String, String>();
    private static Map<String, Date> tokenExpiration = new HashMap<String, Date>();
    @Autowired
    KlfService service;

    @GetMapping("report")
    public List<Report> getReport(@RequestHeader(value = "token") String token){
        List<Report> res = new ArrayList<>();
        if(isValidateToken(token)) res = service.getReport();
        return res;
    }

    @PutMapping(path = "update",produces = "application/json")
    public String updateUserInfo(
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "username") String name,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "email",required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "image",required = false) MultipartFile image
    ) throws RepException, DuplicatedIdException, IOException, SQLException {
        String[] namePassword = findNameAndPassword(token);
        String preName = namePassword[0];
        String prePassword = namePassword[1];
        if(isValidateToken(token))
        {
            Blob blob = null;
            if(image != null ){
                System.out.println(image.getContentType()+" "+image.getBytes().length);
                blob = new SerialBlob(image.getBytes());
            }else{
                System.out.println("cover is empty");
            }
            User user = service.searchUser(preName,prePassword);
            System.out.println(user);
            service.importUserActivity(4, user.getId(), new Timestamp(System.currentTimeMillis()));
            service.updateUser(name,password,email,phone,blob,preName,prePassword);
            if(!name.equals(preName) || !password.equals(prePassword)){
                tokenUsername.remove(user.getToken());
                tokenExpiration.remove(user.getToken());
                user.destroyToken();
                return "Your Profile has been changed."+"\t"+"But your login info changed, please log in again";
            }
            return "Your Profile has been changed.";
        }
        else
        {
            return "User not authenticated.";
        }
    }

    private String[] findNameAndPassword(String token){
        String name = null,password = null;
        if(isValidateToken(token)){
            for (String t: tokenUsername.keySet())
            {
                if (t.equalsIgnoreCase(token))
                {
                    name = tokenUsername.get(t);
                    break;
                }
            }

            for(User user: users){
                if(user.getName().equals(name)){
                    password = user.getPassword();
                }
            }
        }
        return new String[]{name,password};
    }

    @GetMapping(path = "getUserInfo",produces = "application/json")
    public User getUserInfo(@RequestHeader(value = "token") String token){
        System.out.println("GetAll token: "+token);

        String name = findNameAndPassword(token)[0];
        String password = findNameAndPassword(token)[1];

        if(name == null){
            System.out.println("Not find the Name");
            return null;
        }
        User user = service.searchUser(name,password);
        service.importUserActivity(4, user.getId(), new Timestamp(System.currentTimeMillis()));
        System.out.println(user);
        return user;
    }

    @PostMapping(path = "register", produces = "application/json")
    public String createUser(@RequestParam("username") String name, @RequestParam("password") String password
    ,@RequestParam(value = "email",required = false) String email,@RequestParam(value = "phone",required = false) String phone,
                             @RequestParam(value = "image",required = false) MultipartFile image) throws IOException, SQLException {
        User user = new User(name, password);
        users.add(user);
        Blob blob = null;
        if(image != null){
            byte[] images = image.getBytes();
            blob = new SerialBlob(images);
        }

        if(service.searchUserByName(name) == null){
            service.importUser(name,password,email,phone,blob);
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        User userInfo = service.searchUserByName(name);
        service.importUserActivity(1,userInfo.getId(),timestamp);
        return "Created user: " + name+ " successfully";
    }

    @PostMapping(path = "login", produces = "application/json")
    public Response login(@FormParam("username") String username, @FormParam("password") String password) {
        System.out.println(username +" : "+password);
        User user = users.stream().filter(user1 -> user1.getUsername().equals(username))
                .findFirst()
                .orElse(null);MyResponse authResponse;
        Response.Status status;
        if (user != null || service.searchUser(username,password) != null) {
            if(user == null) {
                user = new User(username,password);
                users.add(user);
            }
            if (user.getPassword().equals(password)) {
                user.generateToken();
                System.out.println("Login token1: "+user.getToken());
                tokenUsername.put(user.getToken(), username);
                tokenExpiration.put(user.getToken(), new Date());
                System.out.println("Login token: "+user.getToken());
                authResponse = new MyResponse(true, user.getToken());
                status = Response.Status.OK;
                User userInfo = service.searchUser(username,password);
                service.importUserActivity(2,userInfo.getId(),new Timestamp(System.currentTimeMillis()));
            } else {
                authResponse = new MyResponse(false, "");
                status = Response.Status.UNAUTHORIZED;
            }
        } else {
            authResponse = new MyResponse(false, "");
            status = Response.Status.FORBIDDEN;
        }
        return Response.status(status).entity(authResponse).build();
    }

    @GetMapping(path = "logout")
    @Produces("application/json")
    public String logout(@RequestHeader(value = "token") String token) {
          String[] nameAndPassword = findNameAndPassword(token);
          String name = nameAndPassword[0];
          String password = nameAndPassword[1];
          User user = service.searchUser(name,password);
          tokenUsername.remove(user.getToken());
          tokenExpiration.remove(user.getToken());
          user.destroyToken();
          service.importUserActivity(3, user.getId(), new Timestamp(System.currentTimeMillis()));
          return "You have logged out successfully";
    }
    @PostMapping(path = "auth", produces = MediaType.APPLICATION_JSON_VALUE)
    public static ResponseEntity<String> validateToken(@RequestHeader("token") String token){
        Map<String, String> body = new HashMap<>();
        if (tokenUsername.containsKey(token)) {
            body.put("success", "true");
            Date timeNow = new Date();
            long diff = timeNow.getTime() - tokenExpiration.get(token).getTime();
            long tokenDuration = TimeUnit.MILLISECONDS.toMinutes(diff);
            System.out.println("Duration: " + tokenDuration);
            if (tokenDuration > 30) {
                tokenUsername.remove(token);
                tokenExpiration.remove(token);
            } else {
                return new ResponseEntity<String>("true", HttpStatus.OK);
            }
        }
        body.put("success", "false");
        return new ResponseEntity<String>("false", HttpStatus.UNAUTHORIZED);
    }

    public boolean isValidateToken(String token) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(String.format("http://localhost:8877/user/auth"));
            httpPost.addHeader("token", token);
            CloseableHttpResponse httpResponse = client.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            String isAuthenticated = EntityUtils.toString(entity);
            httpResponse.close();
            if(isAuthenticated.equals("true")){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
