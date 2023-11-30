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
        }finally {
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
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
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
        }finally {
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
        }finally {
            ps.close();
        }

        return UserCreateStatus.UNKNOWN_ERROR;
    }

    public UserDeleteStatus deleteUser(String username) {
        System.out.println(username);
        try {
            if (username.isBlank()) {
                return UserDeleteStatus.INVALID_USERNAME;
            }

            var sql = "delete from users where username = ?";
            var ps = conn.prepareStatement(sql);

            ps.setString(1, username);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                return UserDeleteStatus.SUCCESS;
            } else {
                return UserDeleteStatus.UNKNOWN_ERROR;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return UserDeleteStatus.UNKNOWN_ERROR;
    }


    private boolean isUsernameTaken(String username) throws SQLException {
        PreparedStatement ps = null;
        ResultSet r = null;

        try {
            var sql = "select * from users where username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            r = ps.executeQuery();
            System.out.println("hi");
            System.out.println(r.next());
            return r.next();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            ps.close();
        }
        System.out.println("fix");
        System.out.println(r.next());
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
                //System.out.println(rs.getString("name"));
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

}