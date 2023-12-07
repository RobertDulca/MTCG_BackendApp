package fhtechnikum.robert.apps.mtcg;

import fhtechnikum.robert.apps.mtcg.controller.Controller;
import fhtechnikum.robert.apps.mtcg.controller.UserController;
import fhtechnikum.robert.server.ServerApplication;
import fhtechnikum.robert.server.http.HttpContentType;
import fhtechnikum.robert.server.http.HttpStatus;
import fhtechnikum.robert.server.http.Request;
import fhtechnikum.robert.server.http.Response;

import java.util.ArrayList;
import java.util.List;

public class MtcgApp implements ServerApplication {

    private List<Controller> controllers = new ArrayList<>();

    public MtcgApp() {
        controllers.add(new UserController());
    }

    @Override
    public Response handle(Request request) {

        for (Controller controller: controllers) {
            if (!controller.supports(request.getRoute())) {
                continue;
            }

            return controller.handle(request);
        }

        Response response = new Response();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setContentType(HttpContentType.TEXT_PLAIN);
        response.setBody("Route " + request.getRoute() + " not found in app!");

        return response;
    }
}
