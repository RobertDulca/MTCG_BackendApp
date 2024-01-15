package fhtechnikum.robert.system;


import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Map<String, Controller> handler = new HashMap<>();
    private final Map<String, Repository> repositories;

    //TODO: Create the controllers and them here
    public Router(Map<String, Repository> repositories) {
        this.repositories = repositories;

        UserController userController = new UserController((UserRepository) repositories.get("user"));
        LoginController loginController = new LoginController((UserRepository) repositories.get("user"));
        PackagesController packagesController = new PackagesController((PackageRepository) repositories.get("package"));
        TransactionController transactionController = new TransactionController((PackageRepository) repositories.get("package"));
        CardsController cardsController = new CardsController((CardRepository) repositories.get("card"));
        DeckController deckController = new DeckController((CardRepository) repositories.get("card"));
        StatsController statsController = new StatsController((GameRepository) repositories.get("game"));
        ScoreboardController scoreboardController = new ScoreboardController((GameRepository) repositories.get("game"));
        BattleController battleController = new BattleController((GameRepository) repositories.get("game"));
        TradingController tradingController = new TradingController((TradingRepository) repositories.get("trade"));

        handler.put("/users", userController);
        handler.put("/sessions", loginController);
        handler.put("/packages", packagesController);
        handler.put("/transactions/packages", transactionController);
        handler.put("/cards", cardsController);
        handler.put("/deck", deckController);
        handler.put("/stats", statsController);
        handler.put("/score", scoreboardController);
        handler.put("/battles", battleController);
        handler.put("/tradings", tradingController);
    }

    public Controller route(String path) {
        return handler.get(path);
    }
}
