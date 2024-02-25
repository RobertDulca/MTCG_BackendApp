package fhtechnikum.robert.application.scoreboard;

import fhtechnikum.robert.application.data.Database;
import fhtechnikum.robert.application.stats.Stats;
import fhtechnikum.robert.system.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardRepository extends Repository {
    public List<Stats> getScoreboard() {
        List <Stats> scoreBoard = new ArrayList<>();
        String query = "SELECT users.name, games_played, games_won, games_lost, elo FROM stats " +
                "INNER JOIN users ON stats.username = users.username " +
                "ORDER BY elo DESC";

        try (Connection connection = Database.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){

                ResultSet result = stmt.executeQuery();

                while (result.next()) {
                    Stats stats = new Stats();
                    stats.setUsername(result.getString("name"));
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
