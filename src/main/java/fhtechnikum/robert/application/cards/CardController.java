package fhtechnikum.robert.application.cards;

import fhtechnikum.robert.server.http.HttpStatus;
import fhtechnikum.robert.server.http.Request;
import fhtechnikum.robert.server.http.Response;
import fhtechnikum.robert.system.Controller;

import java.util.List;

public class CardController implements Controller {
    private final CardRepository cardRepo;
    public CardController(CardRepository cardRepo) {
        this.cardRepo = cardRepo;
    }

    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("GET"))
            return showCards(request);

        response.setBody("Wrong method!");
        response.setHttpStatus(HttpStatus.BAD_REQUEST);

        return response;
    }

    private Response showCards(Request request) {
        String username = request.getUsername();
        String token = request.getToken();
        List<Card> cards;

        if (cardRepo.authenticate(username, token)) {
            cards = cardRepo.showCards(username, "cards");
            if (!cards.isEmpty()) {
                response.setHttpStatus(HttpStatus.OK);
                response.setContentType("application/json");
                response.setBody(serializer.serialize(cards));
            } else {
                response.setHttpStatus(HttpStatus.NO_CONTENT);
            }
        } else {
            response.setHttpStatus(HttpStatus.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }
        return response;
    }
}
