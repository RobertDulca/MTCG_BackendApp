package fhtechnikum.robert.application.transactions;

import fhtechnikum.robert.application.packages.PackagesRepository;
import fhtechnikum.robert.server.http.HttpStatus;
import fhtechnikum.robert.server.http.Request;
import fhtechnikum.robert.server.http.Response;
import fhtechnikum.robert.system.Controller;

;

public class TransactionsController implements Controller {
    public TransactionsController(TransactionsRepository transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    private final TransactionsRepository transactionRepo;
    @Override
    public Response process(Request request) {
        if (request.getMethod().equals("POST"))
            return acquirePackage(request);

        response.setBody("Wrong method!");
        response.setHttpStatus(HttpStatus.BAD_REQUEST);

        return response;
    }

    private Response acquirePackage(Request request) {
        String username = request.getUsername();
        String token = request.getToken();

        if (transactionRepo.authenticate(username, token)) {
            if (transactionRepo.enoughMoney(username)) {
                if (transactionRepo.buyPackage(username)) {
                    response.setHttpStatus(HttpStatus.OK);
                    response.setBody("A package has been successfully bought");
                } else {
                    response.setHttpStatus(HttpStatus.NOT_FOUND);
                    response.setBody("No card package available for buying");
                }
            } else {
                response.setHttpStatus(HttpStatus.FORBIDDEN);
                response.setBody("Not enough money for buying a card package");
            }
        } else {
            response.setHttpStatus(HttpStatus.UNAUTHORIZED);
            response.setBody("Access token is missing or invalid");
        }
        return response;
    }
}
