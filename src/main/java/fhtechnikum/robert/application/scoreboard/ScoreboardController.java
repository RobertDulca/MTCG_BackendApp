package fhtechnikum.robert.application.scoreboard;

import fhtechnikum.robert.application.stats.Stats;
import fhtechnikum.robert.server.http.HttpStatus;
import fhtechnikum.robert.server.http.Request;
import fhtechnikum.robert.server.http.Response;
import fhtechnikum.robert.system.Controller;

import java.util.List;

public class ScoreboardController implements Controller {
    private final ScoreboardRepository scoreboardRepo;

    public ScoreboardController(ScoreboardRepository scoreboardRepo) {
        this.scoreboardRepo = scoreboardRepo;
    }

    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("GET"))
            return retrieveScoreboard(request);

        response.setBody("Wrong method!");
        response.setHttpStatus(HttpStatus.BAD_REQUEST);

        return response;
    }

    private Response retrieveScoreboard(Request request) {
        String username = request.getUsername();
        String token = request.getToken();

        if (scoreboardRepo.authenticate(username, token)) {
            List<Stats> scoreboard = scoreboardRepo.getScoreboard();

            response.setHttpStatus(HttpStatus.OK);
            response.setContentType("application/json");
            response.setBody(serializer.serialize(scoreboard));
        } else {
            response.setHttpStatus(HttpStatus.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }
        return response;
    }
}
