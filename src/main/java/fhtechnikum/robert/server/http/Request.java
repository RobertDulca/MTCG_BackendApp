package fhtechnikum.robert.server.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class Request {

    private String method;
    private String route;
    private String body;
    private String token;
    private String username;
    private String pathUser;
    private String tradingId;
    private String contentType;
    private int contentLength;
    private String format;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = Logger.getLogger(Request.class.getName());

    public Request() {}

    public Request(BufferedReader br) {
        parseRequest(br);
    }

    private void parseRequest(BufferedReader br) {
        try {
            String versionString = br.readLine();
            final String[] splitVersionString = versionString.split(" ");

            method = splitVersionString[0];
            route = splitVersionString[1];

            if (route.startsWith("/users/")) {
                pathUser = route.substring("/users/".length());
                route = "/users";
            }

            if (route.startsWith("/tradings/")) {
                tradingId = route.substring("/tradings/".length());
                route = "/tradings";
            }

            if (route.startsWith("/deck?")) {
                format = route.substring("/deck?format=".length());
                route = "/deck";
            }

            String line;
            while (!(line = br.readLine()).equals("")) {
                if (line.startsWith("Authorization: Bearer ")) {
                    token = line.substring("Authorization: Bearer ".length());
                    username = token.substring(0, token.indexOf("-"));
                }
                if (line.startsWith("Content-Type: "))
                    contentType = line.substring("Content-Type: ".length());

                if (line.startsWith("Content-Length: "))
                    contentLength = Integer.parseInt(line.substring("Content-Length: ".length()));
            }

            char[] buffer = new char[contentLength];
            br.read(buffer, 0, contentLength);
            body = new String(buffer);

            logger.info("Method: " + method + "\nPath: " + route + "\nBody: " + body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRoute() {
        return route;
    }

    public void setPath(String path) {
        this.route = path;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPathUser() {
        return pathUser;
    }

    public void setPathUser(String pathUser) {
        this.pathUser = pathUser;
    }

    public String getTradingId() {
        return tradingId;
    }

    public void setTradingId(String tradingId) {
        this.tradingId = tradingId;
    }

    public String getFormat() {
        return format;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public <T> T getBodyAs(Class <T> clazz) {
        try {
            return objectMapper.readValue(body, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> getBodyAsList(Class <T> clazz) {
        try {
            return objectMapper.readValue(body, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
