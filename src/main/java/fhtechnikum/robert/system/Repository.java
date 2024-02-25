package fhtechnikum.robert.system;

import fhtechnikum.robert.application.data.Database;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Repository {
    //TODO: Implement the Repository class and maybe authentication
    public boolean authenticate(String username, String token) {
        String query = "SELECT token FROM session WHERE username = ?";
        try (Connection connection = Database.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, username);

                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                    String dbToken = result.getString("token");
                    return dbToken.equals(token);
                }
            } finally {
                Database.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}