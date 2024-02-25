package fhtechnikum.robert.application.deck;

import fhtechnikum.robert.application.cards.Card;
import fhtechnikum.robert.application.cards.CardRepository;
import fhtechnikum.robert.server.http.HttpStatus;
import fhtechnikum.robert.server.http.Request;
import fhtechnikum.robert.server.http.Response;
import fhtechnikum.robert.system.Controller;

import java.util.List;

public class DeckController implements Controller {
    public DeckController(DeckRepository deckRepo, CardRepository cardRepo) {
        this.deckRepo = deckRepo;
        this.cardRepo = cardRepo;
    }

    private final DeckRepository deckRepo;
    private final CardRepository cardRepo;
    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("GET"))
            return showDeck(request);
        else if (request.getMethod().equals("PUT"))
            return configureDeck(request);

        response.setBody("Wrong method!");
        response.setHttpStatus(HttpStatus.BAD_REQUEST);

        return response;
    }

    private Response showDeck(Request request) {
        String username = request.getUsername();
        String token = request.getToken();

        if (cardRepo.authenticate(username, token)) {
            List<Card> cards = cardRepo.showCards(username, "decks");

            if (!cards.isEmpty()) {
                response.setHttpStatus(HttpStatus.OK);
                String format = request.getFormat();
                if (format != null && format.equals("plain")) {
                    StringBuilder deck = new StringBuilder();
                    deck.append("Deck of ").append(username).append(": \n");

                    for (Card card : cards) {
                        deck.append("ID ").append(card.getId()).append("; Name: ").append(card.getCardName()).append("; Damage: ").append(card.getDamage()).append(";\n");
                    }
                    response.setContentType("plain/text");
                    response.setBody(deck.toString());
                }
                else {
                    response.setContentType("application/json");
                    response.setBody(serializer.serialize(cards));
                }
            } else {
                response.setHttpStatus(HttpStatus.NO_CONTENT);
            }
        } else {
            response.setHttpStatus(HttpStatus.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }
        return response;
    }

    private Response configureDeck(Request request) {
        String username = request.getUsername();
        String token = request.getToken();
        List<String> deck = request.getBodyAs(List.class);

        if (deckRepo.authenticate(username, token)) {
            if (deck.size() == 4) {
                if (deckRepo.configureDeck(username, deck)) {
                    response.setHttpStatus(HttpStatus.OK);
                } else {
                    response.setHttpStatus(HttpStatus.FORBIDDEN);
                    response.setBody("At least one of the provided cards does not belong to the user or is not available.");
                }
            } else {
                response.setHttpStatus(HttpStatus.BAD_REQUEST);
                response.setBody("The provided deck did not include the required amount of cards");
            }
        } else {
            response.setHttpStatus(HttpStatus.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }
        return response;
    }
}
