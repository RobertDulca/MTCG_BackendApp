package fhtechnikum.robert.application.stats;

import fhtechnikum.robert.application.data.Database;
import fhtechnikum.robert.system.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StatsRepository extends Repository {
    public Stats getStats(String username) {
        String query = "SELECT users.name, games_played, games_won, games_lost, elo FROM stats " +
                "INNER JOIN users ON stats.username = users.username " +
                "WHERE stats.username = ?";

        try (Connection connection = Database.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, username);

                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                    Stats stats = new Stats();
                    stats.setUsername(result.getString("name"));
                    stats.setTotalGames(result.getInt("games_played"));
                    stats.setGamesWon(result.getInt("games_won"));
                    stats.setGamesLost(result.getInt("games_lost"));
                    stats.setElo(result.getInt("elo"));

                    return stats;
                }
            } finally {
                Database.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
