package fhtechnikum.robert.application.user;

import fhtechnikum.robert.server.http.HttpStatus;
import fhtechnikum.robert.server.http.Request;
import fhtechnikum.robert.server.http.Response;
import fhtechnikum.robert.system.Controller;


public class LoginController implements Controller {
    public LoginController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    private final UserRepository userRepo;
    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("POST"))
            return loginUser(request.getBodyAs(User.class));

        response.setBody("Wrong method!");
        response.setHttpStatus(HttpStatus.BAD_REQUEST);

        return response;
    }

    private Response loginUser(User cred) {
        String token;

        if ((token = userRepo.loginUser(cred.getUsername(), cred.getPassword())) != null) {
            response.setHttpStatus(HttpStatus.OK);
            response.setContentType("application/json");
            response.setBody(token);
        } else {
            response.setHttpStatus(HttpStatus.UNAUTHORIZED);
            response.setBody("Invalid username/password provided");
        }
        return response;
    }
}
