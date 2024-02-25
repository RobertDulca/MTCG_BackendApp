package fhtechnikum.robert.application.packages;

import fhtechnikum.robert.application.cards.Card;
import fhtechnikum.robert.server.http.HttpStatus;
import fhtechnikum.robert.server.http.Request;
import fhtechnikum.robert.server.http.Response;
import fhtechnikum.robert.system.Controller;

import java.util.List;

public class PackagesController implements Controller {
    public PackagesController(PackagesRepository packageRepo) {
        this.packageRepo = packageRepo;
    }

    private final PackagesRepository packageRepo;
    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("POST"))
            return createPackage(request);

        response.setBody("Wrong method!");
        response.setHttpStatus(HttpStatus.BAD_REQUEST);

        return response;
    }

    private Response createPackage(Request request) {
        String token = request.getToken();
        String username = request.getUsername();
        List<Card> cards = request.getBodyAsList(Card.class);

        for (Card card : cards)
            card.getElements();

        if (packageRepo.authenticate(username, token)) {
            if (token.equals("admin-mtcgToken")) {
                if (packageRepo.createPackage(cards)) {
                    response.setHttpStatus(HttpStatus.CREATED);
                    response.setBody(serializer.serialize(cards));
                } else {
                    response.setHttpStatus(HttpStatus.CONFLICT);
                    response.setBody("At least one card in the packages already exists");
                }
            } else {
                response.setHttpStatus(HttpStatus.FORBIDDEN);
                response.setBody("Provided user is not 'admin'");
            }
        } else {
            response.setHttpStatus(HttpStatus.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }
        return response;
    }
}
