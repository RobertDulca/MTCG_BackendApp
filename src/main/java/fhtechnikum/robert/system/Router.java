package fhtechnikum.robert.system;


import fhtechnikum.robert.application.cards.CardController;
import fhtechnikum.robert.application.cards.CardRepository;
import fhtechnikum.robert.application.deck.DeckController;
import fhtechnikum.robert.application.deck.DeckRepository;
import fhtechnikum.robert.application.packages.PackagesController;
import fhtechnikum.robert.application.packages.PackagesRepository;
import fhtechnikum.robert.application.scoreboard.ScoreboardController;
import fhtechnikum.robert.application.scoreboard.ScoreboardRepository;
import fhtechnikum.robert.application.stats.StatsController;
import fhtechnikum.robert.application.stats.StatsRepository;
import fhtechnikum.robert.application.transactions.TransactionsController;
import fhtechnikum.robert.application.transactions.TransactionsRepository;
import fhtechnikum.robert.application.user.UserController;
import fhtechnikum.robert.application.user.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Map<String, Controller> handler = new HashMap<>();
    private final Map<String, Repository> repositories;

    //TODO: Create the controllers and them here
    public Router(Map<String, Repository> repositories) {
        this.repositories = repositories;

        UserController userController = new UserController((UserRepository) repositories.get("user"));
        PackagesController packagesController = new PackagesController((PackagesRepository) repositories.get("package"));
        TransactionsController transactionController = new TransactionsController((TransactionsRepository) repositories.get("transaction"));
        CardController cardsController = new CardController((CardRepository) repositories.get("card"));
        DeckController deckController = new DeckController((DeckRepository) repositories.get("deck"), (CardRepository) repositories.get("card"));
        StatsController statsController = new StatsController((StatsRepository) repositories.get("stats"));
        ScoreboardController scoreboardController = new ScoreboardController((ScoreboardRepository) repositories.get("scoreboard"));
        /*BattleController battleController = new BattleController((GameRepository) repositories.get("game"));
        TradingController tradingController = new TradingController((TradingRepository) repositories.get("trade"));*/


        handler.put("/users", userController);
        handler.put("/sessions", userController);
        handler.put("/packages", packagesController);
        handler.put("/transactions/packages", transactionController);
        handler.put("/cards", cardsController);
        handler.put("/deck", deckController);
        handler.put("/stats", statsController);
        handler.put("/scoreboard", scoreboardController);
        /*handler.put("/battles", battleController);
        handler.put("/tradings", tradingController);*/
    }

    public Controller route(String path) {
        return handler.get(path);
    }
}
