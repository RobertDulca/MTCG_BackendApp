package router;

import fhtechnikum.robert.application.battle.BattleController;
import fhtechnikum.robert.application.cards.CardController;
import fhtechnikum.robert.application.deck.DeckController;
import fhtechnikum.robert.application.packages.PackagesController;
import fhtechnikum.robert.application.scoreboard.ScoreboardController;
import fhtechnikum.robert.application.stats.StatsController;
import fhtechnikum.robert.application.transactions.TransactionsController;
import fhtechnikum.robert.application.user.UserController;
import fhtechnikum.robert.server.http.Request;
import fhtechnikum.robert.system.ReposManager;
import fhtechnikum.robert.system.Router;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class RouterTest {
    static Router router;

    @BeforeAll
    static void initRouter() {
        ReposManager repoManager = new ReposManager();
        router = new Router(repoManager.getRepositories());
    }

    // Assert paths getting routed correctly
    @Test
    void properRouting() {
        assertEquals(UserController.class, router.route("/users").getClass());
        assertEquals(UserController.class, router.route("/sessions").getClass());
        assertEquals(PackagesController.class, router.route("/packages").getClass());
        assertEquals(TransactionsController.class, router.route("/transactions/packages").getClass());
        assertEquals(CardController.class, router.route("/cards").getClass());
        assertEquals(DeckController.class, router.route("/deck").getClass());
        assertEquals(StatsController.class, router.route("/stats").getClass());
        assertEquals(ScoreboardController.class, router.route("/scoreboard").getClass());
        assertEquals(BattleController.class, router.route("/battles").getClass());
    }

    @Test
    void wrongPath() {
        assertNull(router.route("/what"));
    }

    // Test if router routes to UserController despite having a username after the path
    @Test
    void pathWithUsername() {
        String req = """
                POST /users/Steven HTTP/1.1\r
                Host: localhost:10001\r
                \r
                """;

        BufferedReader br = new BufferedReader(new StringReader(req));

        Request request = new Request(br);

        assertEquals(UserController.class, router.route(request.getRoute()).getClass());
    }
}
