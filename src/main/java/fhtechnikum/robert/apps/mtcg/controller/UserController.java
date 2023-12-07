package fhtechnikum.robert.apps.mtcg.controller;

import fhtechnikum.robert.server.http.HttpContentType;
import fhtechnikum.robert.server.http.HttpStatus;
import fhtechnikum.robert.server.http.Request;
import fhtechnikum.robert.server.http.Response;

public class UserController implements Controller {

    @Override
    public boolean supports(String route) {
        return route.equals("/users");
    }

    @Override
    public Response handle(Request request) {
        Response response = new Response();
        response.setStatus(HttpStatus.OK);
        response.setContentType(HttpContentType.TEXT_PLAIN);
        response.setBody("user controller");

        return response;
    }
}
