package in.ser.the_ultimate_scrum_simulator;

import in.ser.the_ultimate_scrum_simulator.model.*;

import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbWrapper {
    private static final int MAX_LOGIN_ATTEMPTS = 3;

    //    static Argon2PasswordEncoder argon2 = new Argon2PasswordEncoder(16, 128, 1, 20000, 3);
    public Connection conn;

    public DbWrapper() {
        this.conn = createConnection();
        if (conn == null) {
            throw new RuntimeException("Failed to establish a database connection.");
        }
    }

    private Connection createConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:" + Path.of("Reduction.db").toAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public UserAuthResult loginWith(String username, String password) throws SQLException {
        PreparedStatement ps = null;
        ResultSet r = null;
        try {
            var sql = "select * from users where username = ?";
            ps = conn.prepareStatement(sql);

            ps.setString(1, username);

            r = ps.executeQuery();
            if (!r.next()) {
                return new UserAuthResult(AuthStatus.USER_NOT_FOUND, null, 0);
            }

            final int failedLogins = r.getInt(5);
            if (failedLogins >= MAX_LOGIN_ATTEMPTS) {
                return new UserAuthResult(AuthStatus.TOO_MANY_FAILED_LOGINS, null, failedLogins);
            }

            String hash = r.getString(4);
            if (!password.matches(hash)) {
                int nc = failedLogins + 1;
                setFailedLoginCt(username, nc);
                return new UserAuthResult(AuthStatus.INCORRECT_PASSWORD, null, nc);
            }
            int accessGroup = r.getInt("access_group");
            var u = new User(r.getInt(1), r.getString(2), r.getString(3), accessGroup);
            setFailedLoginCt(username, 0);
            setActive(username);
            return new UserAuthResult(AuthStatus.SUCCESS, Optional.of(u), 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ps.close();
            r.close();
        }

        return new UserAuthResult(AuthStatus.UNKNOWN_ERROR, null, 0);
    }

    private void setFailedLoginCt(String username, int ct) throws SQLException {
        PreparedStatement ps = null;
        try {
            var sql = "update users set consecutive_incorrect_pass = ? where username = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, ct);
            ps.setString(2, username);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ps.close();
        }
    }

    private void setActive(String username) throws SQLException {
        PreparedStatement ps = null;
        try {
            var sql = "update users set is_active = 1 where username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ps.close();
        }
    }

    public UserCreateStatus registerUser(String fullname, String username, String password, int accessGroup) throws SQLException {
        PreparedStatement ps = null;
        try {
            if (username.isBlank()) {
                return UserCreateStatus.INVALID_USERNAME;
            }
            if (isUsernameTaken(username)) {
                return UserCreateStatus.USERNAME_TAKEN;
            }
            if (password.length() < 8) {
                return UserCreateStatus.INVALID_PASSWORD;
            }

            var sql = "insert into users (fullname, username, password, consecutive_incorrect_pass, access_group, is_active) values (?, ?, ?, 0, ?, 0)";
            ps = conn.prepareStatement(sql);

            ps.setString(1, fullname);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setInt(4, accessGroup);
            ps.execute();
            return UserCreateStatus.SUCCESS;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }

        return UserCreateStatus.UNKNOWN_ERROR;
    }

    public UserDeleteStatus deleteUser(String username) {
        try {
            if (username == null || username.isBlank()) {
                return UserDeleteStatus.INVALID_USERNAME;
            }

            var sql = "delete from users where username = ?";
            var ps = conn.prepareStatement(sql);

            ps.setString(1, username);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                return UserDeleteStatus.SUCCESS;
            } else {
                return UserDeleteStatus.USER_NOT_FOUND;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return UserDeleteStatus.UNKNOWN_ERROR;
        }
    }


    private boolean isUsernameTaken(String username) throws SQLException {
        PreparedStatement ps = null;
        ResultSet r = null;

        try {
            var sql = "select * from users where username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            r = ps.executeQuery();
            return r.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ps.close();
        }
        return r.next();
    }

    public List<String> selectAll() throws SQLException {
        List<String> userList = new ArrayList<>();
        var sql = "SELECT username FROM users";
        var ps = conn.prepareStatement(sql);
        try (var rs = ps.executeQuery()) {
            // loop through the result set
            while (rs.next()) {
                userList.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        ps.close();
        return userList;
    }

    public CreateScenarioStatus validateAndCreateScenario(String title, String creator, int difficulty) {
        // Validate input
        if (title == null || title.trim().isEmpty()) {
            return CreateScenarioStatus.INVALID_TITLE;
        }
        if (creator == null || creator.trim().isEmpty()) {
            return CreateScenarioStatus.INVALID_CREATOR;
        }
        if (difficulty < 0 || difficulty > 2) {
            return CreateScenarioStatus.INVALID_DIFFICULTY;
        }

        // Insert into database
        String sql = "INSERT INTO scenario (title, creator, difficulty) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, creator);
            pstmt.setInt(3, difficulty);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0 ? CreateScenarioStatus.SUCCESS : CreateScenarioStatus.DATABASE_ERROR;
        } catch (SQLException e) {
            e.printStackTrace();
            return CreateScenarioStatus.UNKNOWN_ERROR;
        }
    }

    public CreateStoryStatus createStory(String title, String description, String owner, int estimate, int scenarioId) {
        try {
            // Validate input
            if (title == null || title.trim().isEmpty() || description == null || description.trim().isEmpty()
                    || owner == null || owner.trim().isEmpty() || estimate < 0) {
                return CreateStoryStatus.INVALID_INPUT;
            }

            // Check if the scenarioId exists in the scenario table
            if (!isScenarioExist(scenarioId)) {
                return CreateStoryStatus.INVALID_SCENARIO_ID;
            }

            // Insert into the story table
            String sql = "INSERT INTO story (title, description, owner, estimate, scenario_id) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, title);
                pstmt.setString(2, description);
                pstmt.setString(3, owner);
                pstmt.setInt(4, estimate);
                pstmt.setInt(5, scenarioId);

                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0 ? CreateStoryStatus.SUCCESS : CreateStoryStatus.DATABASE_ERROR;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return CreateStoryStatus.UNKNOWN_ERROR;
        }
    }

    private boolean isScenarioExist(int scenarioId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM scenario WHERE id = ?")) {
            ps.setInt(1, scenarioId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Integer> getScenarioIds() throws SQLException {
        List<Integer> scenarioIds = new ArrayList<>();
        String sql = "SELECT id FROM scenario";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                scenarioIds.add(rs.getInt("id"));
            }
        }
        return scenarioIds;
    }

    public ScenarioDeleteStatus deleteScenarioById(int scenarioId) {
        try {
            if (scenarioId <= 0) {
                return ScenarioDeleteStatus.INVALID_SCENARIO_ID;
            }

            String sql = "DELETE FROM scenario WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, scenarioId);

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    return ScenarioDeleteStatus.SUCCESS;
                } else {
                    return ScenarioDeleteStatus.SCENARIO_NOT_FOUND;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ScenarioDeleteStatus.UNKNOWN_ERROR;
        }
    }


}