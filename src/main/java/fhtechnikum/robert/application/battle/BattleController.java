package fhtechnikum.robert.application.battle;

import fhtechnikum.robert.server.http.HttpStatus;
import fhtechnikum.robert.server.http.Request;
import fhtechnikum.robert.server.http.Response;
import fhtechnikum.robert.system.Controller;

import java.util.ArrayList;
import java.util.List;

public class BattleController implements Controller {
    public BattleController(BattleRepository battleRepo) {
        this.battleRepo = battleRepo;
    }

    private final List<String> lobby = new ArrayList<>();
    private final int TIMEOUT = 10;     // Seconds
    private boolean battleStarted = false;
    private final BattleRepository battleRepo;

    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("POST")) {
            if (battleRepo.authenticate(request.getUsername(), request.getToken()))
                return battle(request);
            else {
                response.setHttpStatus(HttpStatus.UNAUTHORIZED);
                response.setBody("Access token is missing or invalid");
            }
        }
        response.setBody("Wrong method!");
        response.setHttpStatus(HttpStatus.BAD_REQUEST);

        return response;
    }

    private synchronized Response battle(Request request) {
        String username = request.getUsername();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(!battleStarted) {
            lobby.add(username);
        }

        if (lobby.size() == 2) {
            battleStarted = true;
            String user1 = lobby.get(0);
            String user2 = lobby.get(1);

            if (user1.equals(user2)) {
                response.setHttpStatus(HttpStatus.BAD_REQUEST);
                response.setBody("You can't battle yourself");
                return response;
            }

            String battleLog = battleRepo.startBattle(user1, user2);

            lobby.clear();
            battleStarted = false;

            response.setHttpStatus(HttpStatus.OK);
            response.setBody(battleLog);
            return response;
        }
        response.setHttpStatus(HttpStatus.OK);
        response.setBody("1 Player in lobby");
        return response;
    }
}
