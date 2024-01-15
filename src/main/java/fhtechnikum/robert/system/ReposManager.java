package fhtechnikum.robert.system;

import java.util.HashMap;
import java.util.Map;

public class ReposManager {

    private final Map<String, Repository> repositories;

    //TODO: Create the repositories and add them here
    public RepositoryManager() {
        repositories = new HashMap<>();

        UserRepository userRepo = new UserRepository();
        PackageRepository packageRepo = new PackageRepository();
        CardRepository cardRepo = new CardRepository();
        GameRepository gameRepo = new GameRepository(cardRepo);
        TradingRepository tradingRepo = new TradingRepository();

        repositories.put("user", userRepo);
        repositories.put("package", packageRepo);
        repositories.put("card", cardRepo);
        repositories.put("game", gameRepo);
        repositories.put("trade", tradingRepo);
    }

    public Map<String, Repository> getRepositories() {
        return repositories;
    }

}
