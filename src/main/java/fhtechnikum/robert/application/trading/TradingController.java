package fhtechnikum.robert.application.trading;

import fhtechnikum.robert.server.http.HttpStatus;
import fhtechnikum.robert.server.http.Request;
import fhtechnikum.robert.server.http.Response;
import fhtechnikum.robert.system.Controller;

import java.util.List;

public class TradingController implements Controller {
    public TradingController(TradingRepository tradingRepo) {
        this.tradingRepo = tradingRepo;
    }

    private final TradingRepository tradingRepo;
    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("GET"))
            return getDeals(request);
        if (request.getMethod().equals("POST")) {
            if (request.getTradingId() != null)
                return trade(request);
            return createDeal(request);
        }
        if (request.getMethod().equals("DELETE"))
            return deleteDeal(request);

        response.setBody("Wrong method!");
        response.setHttpStatus(HttpStatus.BAD_REQUEST);

        return response;
    }

    private Response getDeals(Request request) {
        String username = request.getUsername();
        String token = request.getToken();

        if(tradingRepo.authenticate(username, token)) {
            List<Trade> deals = tradingRepo.getDeals();

            if (!deals.isEmpty()) {
                response.setHttpStatus(HttpStatus.OK);
                response.setBody(serializer.serialize(deals));
            } else {
                response.setHttpStatus(HttpStatus.NO_CONTENT);
            }
        } else {
            response.setHttpStatus(HttpStatus.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }
        return response;
    }

    private Response createDeal(Request request) {
        String username = request.getUsername();
        String token = request.getToken();

        if(tradingRepo.authenticate(username, token)) {
            Trade trade = request.getBodyAs(Trade.class);

            if (!tradingRepo.dealExists(trade.getId())) {
                if(tradingRepo.createDeal(trade)) {
                    response.setHttpStatus(HttpStatus.CREATED);
                    response.setBody(serializer.serialize(trade));
                } else {
                    response.setHttpStatus(HttpStatus.FORBIDDEN);
                    response.setBody("The provided card does not belong to the user or is not available.");
                }
            } else {
                response.setHttpStatus(HttpStatus.CONFLICT);
                response.setBody("A deal with this deal ID already exists.");
            }
        } else {
            response.setHttpStatus(HttpStatus.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }
        return response;
    }

    private Response deleteDeal(Request request) {
        String username = request.getUsername();
        String token = request.getToken();

        if(tradingRepo.authenticate(username, token)) {
            String dealId = request.getTradingId();

            if (tradingRepo.dealExists(dealId)) {
                if(tradingRepo.deleteDeal(dealId, username)) {
                    response.setHttpStatus(HttpStatus.OK);
                    response.setBody("Trading deal successfully deleted");
                } else {
                    response.setHttpStatus(HttpStatus.FORBIDDEN);
                    response.setBody("The deal contains a card that is not owned by the user.");
                }
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setBody("The provided deal ID was not found");
            }
        } else {
            response.setHttpStatus(HttpStatus.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }
        return response;
    }

    private Response trade(Request request) {
        String username = request.getUsername();
        String token = request.getToken();

        if(tradingRepo.authenticate(username, token)) {
            String dealId = request.getTradingId();
            String tradingCardId = request.getBody().replaceAll("\"", "");

            if (tradingRepo.dealExists(dealId)) {
                if(tradingRepo.trade(dealId, username, tradingCardId)) {
                    response.setHttpStatus(HttpStatus.OK);
                    response.setBody("Trading deal successfully executed");
                } else {
                    response.setHttpStatus(HttpStatus.FORBIDDEN);
                    response.setBody("The offered card is not owned by the user, the requirements are not met " +
                            "(Type, MinimumDamage), the offered card is locked in the deck or the user " +
                            "tried to trade with himself");
                }
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setBody("The provided deal ID was not found");
            }
        } else {
            response.setHttpStatus(HttpStatus.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }
        return response;
    }
}
