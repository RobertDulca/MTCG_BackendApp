package fhtechnikum.robert.server.http;

public class Response {

    private String contentType = "text/plain";
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private String body;

    protected String responseBuilder() {
        return "HTTP/1.1 " + httpStatus.getCode() + " " + httpStatus.getMessage() + "\n" +
                "Content-Type: " + contentType + "\n" +
                "\n" +
                body;
    }
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public Status getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Status httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
