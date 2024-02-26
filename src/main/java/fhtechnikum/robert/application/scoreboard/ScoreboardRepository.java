package fhtechnikum.robert.application.scoreboard;

import fhtechnikum.robert.application.data.Database;
import fhtechnikum.robert.application.stats.Stats;
import fhtechnikum.robert.system.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScoreboardRepository extends Repository {
    public List<Stats> getScoreboard() {
        List <Stats> scoreBoard = new ArrayList<>();
        String query = "SELECT username, games_played, games_won, games_lost, elo FROM stats " +
                "ORDER BY elo DESC";

        try (Connection connection = Database.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){

                ResultSet result = stmt.executeQuery();

                while (result.next()) {
                    Stats stats = new Stats();
                    stats.setUsername(result.getString("username"));
                    stats.setTotalGames(result.getInt("games_played"));
                    stats.setGamesWon(result.getInt("games_won"));
                    stats.setGamesLost(result.getInt("games_lost"));
                    stats.setElo(result.getInt("elo"));

                    scoreBoard.add(stats);
                }
            } finally {
                Database.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scoreBoard;
    }
}
