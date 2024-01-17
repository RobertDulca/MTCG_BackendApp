package fhtechnikum.robert.application.user;

import fhtechnikum.robert.application.data.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserRepository extends fhtechnikum.robert.system.Repository {
    public boolean createUser(String username, String password) {
        String query = "INSERT INTO users (username, password, coins) VALUES (?, ?, ?)";

        try (
                Connection con = Database.getConnection();
                PreparedStatement stmt = con.prepareStatement(query)
        ) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setInt(3, 20);

            stmt.execute();
            return true;
            /*int result = stmt.executeUpdate();

            if (result != 0) {
                if (createStats(username))
                    return true;
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String findUser(String username) {
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = Database.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, username);

                ResultSet result = stmt.executeQuery();

                if (result.next())
                    return result.getString("username");
            } finally {
                Database.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
