package in.ser.the_ultimate_scrum_simulator;


import in.ser.the_ultimate_scrum_simulator.model.*;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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


    public UserAuthResult loginWith(String username, String password) {
        try {
            var sql = "select * from users where username = ?";
            var ps = conn.prepareStatement(sql);

            ps.setString(1, username);

            var r = ps.executeQuery();
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
            var u = new User(r.getInt(1), r.getString(2), r.getString(4), accessGroup);
            setFailedLoginCt(username, 0);
            return new UserAuthResult(AuthStatus.SUCCESS, Optional.of(u), 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new UserAuthResult(AuthStatus.UNKNOWN_ERROR, null, 0);
    }

    private void setFailedLoginCt(String username, int ct) throws SQLException {
        var sql = "update users set consecutive_incorrect_pass = ? where username = ?";
        var ps = conn.prepareStatement(sql);

        ps.setInt(1, ct);
        ps.setString(2, username);
        ps.execute();
    }

    public UserCreateStatus registerUser(String fullname, String username, String password, int accessGroup) {
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
            var ps = conn.prepareStatement(sql);

            ps.setString(1, fullname);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setInt(4, accessGroup);

            ps.execute();
            return UserCreateStatus.SUCCESS;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return UserCreateStatus.UNKNOWN_ERROR;
    }

    public UserDeleteStatus deleteUser(String username) {
        try {
            if (username.isBlank()) {
                return UserDeleteStatus.INVALID_USERNAME;
            }
            if (!isUsernameTaken(username)) {
                return UserDeleteStatus.USER_NOT_FOUND;
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
        var sql = "select * from users where username = ?";
        var ps = conn.prepareStatement(sql);

        ps.setString(1, username);
        var r = ps.executeQuery();
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
        return userList;
    }


}