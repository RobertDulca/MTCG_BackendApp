package fhtechnikum.robert.application.stats;

import fhtechnikum.robert.server.http.HttpStatus;
import fhtechnikum.robert.server.http.Request;
import fhtechnikum.robert.server.http.Response;
import fhtechnikum.robert.system.Controller;

public class StatsController implements Controller {

    public StatsController(StatsRepository statsRepo) {
        this.statsRepo = statsRepo;
    }

    private final StatsRepository statsRepo;

    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("GET"))
            return retrieveStats(request);

        response.setBody("Wrong method!");
        response.setHttpStatus(HttpStatus.BAD_REQUEST);

        return response;
    }

    private Response retrieveStats(Request request) {
        String username = request.getUsername();
        String token = request.getToken();

        if (statsRepo.authenticate(username, token)) {
            Stats stats = statsRepo.getStats(username);
            response.setHttpStatus(HttpStatus.OK);
            response.setContentType("application/json");
            response.setBody(serializer.serialize(stats));
        } else {
            response.setHttpStatus(HttpStatus.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }
        return response;
    }
}
