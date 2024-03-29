package fhtechnikum.robert.system;

import fhtechnikum.robert.application.battle.BattleRepository;
import fhtechnikum.robert.application.cards.CardRepository;
import fhtechnikum.robert.application.deck.DeckRepository;
import fhtechnikum.robert.application.packages.PackagesRepository;
import fhtechnikum.robert.application.scoreboard.ScoreboardRepository;
import fhtechnikum.robert.application.stats.StatsRepository;
import fhtechnikum.robert.application.trading.TradingRepository;
import fhtechnikum.robert.application.transactions.TransactionsRepository;
import fhtechnikum.robert.application.user.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class ReposManager {

    private final Map<String, Repository> repositories;

    //TODO: Create the repositories and add them here
    public ReposManager() {
        repositories = new HashMap<>();

        UserRepository userRepo = new UserRepository();
        PackagesRepository packageRepo = new PackagesRepository();
        TransactionsRepository transactionRepo = new TransactionsRepository();
        CardRepository cardRepo = new CardRepository();
        DeckRepository deckRepo = new DeckRepository();
        StatsRepository statsRepo = new StatsRepository();
        ScoreboardRepository scoreboardRepo = new ScoreboardRepository();
        BattleRepository battleRepo = new BattleRepository(cardRepo);
        TradingRepository tradingRepo = new TradingRepository();


        repositories.put("user", userRepo);
        repositories.put("package", packageRepo);
        repositories.put("transaction", transactionRepo);
        repositories.put("card", cardRepo);
        repositories.put("deck", deckRepo);
        repositories.put("stats", statsRepo);
        repositories.put("scoreboard", scoreboardRepo);
        repositories.put("battle", battleRepo);
        repositories.put("trade", tradingRepo);
    }

    public Map<String, Repository> getRepositories() {
        return repositories;
    }

}
