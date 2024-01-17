package fhtechnikum.robert.application.user;

import fhtechnikum.robert.server.http.HttpStatus;
import fhtechnikum.robert.server.http.Request;
import fhtechnikum.robert.server.http.Response;

public class UserController implements fhtechnikum.robert.system.Controller {
    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    private final UserRepository userRepo;
    @Override
    public Response process(Request request) {
        switch (request.getMethod()) {

            case "POST":
                return registerUser(request);

        }

        response.setBody("Wrong method!");
        response.setHttpStatus(HttpStatus.BAD_REQUEST);

        return response;
    }

    private Response registerUser(Request request) {
        User cred = request.getBodyAs(User.class);

        if (userRepo.findUser(cred.getUsername()) == null) {
            if (userRepo.createUser(cred.getUsername(), cred.getPassword())) {
                response.setHttpStatus(HttpStatus.CREATED);
                response.setBody("User created");
            }
        } else {
            response.setHttpStatus(HttpStatus.CONFLICT);
            response.setBody("User with same username already registered");
        }
        return response;
    }
}