package in.ser.the_ultimate_scrum_simulator;


import in.ser.the_ultimate_scrum_simulator.model.AuthStatus;
import in.ser.the_ultimate_scrum_simulator.model.User;
import in.ser.the_ultimate_scrum_simulator.model.UserAuthResult;
import in.ser.the_ultimate_scrum_simulator.model.UserCreateStatus;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class DbWrapper {
    private static final int MAX_LOGIN_ATTEMPTS = 3;

    static Argon2PasswordEncoder argon2 = new Argon2PasswordEncoder(16, 128, 1, 20000, 3);
    public Connection conn;

    public DbWrapper() {
        this.conn = createConnection();
    }

    private Connection createConnection(){

        try {
            Class.forName(
                    "org.sqlite.JDBC"); // Driver name

            Connection con = DriverManager.getConnection(
                    "jdbc:sqlite:"+ Path.of("Reduction.db").toAbsolutePath());
            System.out.println("Path : "+Path.of("Reduction.db").toAbsolutePath());

            return con;
        } catch (Exception e) {
            System.out.println("Failed to create connection : "+e);
            return null;
        }
    }

    public UserAuthResult loginWith(String username, String password) {
        try {
            var sql = "select * from users where name = ?";
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

            var hash = r.getString(4);
            if (!argon2.matches(password, hash)) {
                int nc = failedLogins + 1;
                setFailedLoginCt(username, nc);
                return new UserAuthResult(AuthStatus.INCORRECT_PASSWORD, null, nc);
            }

            var u = new User(r.getInt(1), r.getString(2), r.getString(4));
            setFailedLoginCt(username, 0);
            return new UserAuthResult(AuthStatus.SUCCESS, Optional.of(u), 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new UserAuthResult(AuthStatus.UNKNOWN_ERROR, null, 0);
    }

    private void setFailedLoginCt(String username, int ct) throws SQLException {
        var sql = "update users set consecutive_incorrect_pass = ? where name = ?";
        var ps = conn.prepareStatement(sql);

        ps.setInt(1, ct);
        ps.setString(2, username);
        ps.execute();
    }

    public UserCreateStatus registerUser(String username, String password, int accessGroup) {
        try {
            if (username.isBlank()) {return UserCreateStatus.INVALID_USERNAME;}
            if (isUsernameTaken(username)) {return UserCreateStatus.USERNAME_TAKEN;}
            if (password.length() < 8) {return UserCreateStatus.INVALID_PASSWORD;}

            var sql = "insert into users(token, name, pass_hash, consecutive_incorrect_pass, access_group) values(?, ?, ?, 0, ?)";
            var ps = conn.prepareStatement(sql);

            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, username);
            ps.setString(3, argon2.encode(password));
            ps.setInt(4, accessGroup);

            ps.execute();
            return UserCreateStatus.SUCCESS;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return UserCreateStatus.UNKNOWN_ERROR;
    }

    private boolean isUsernameTaken(String username) throws SQLException {
        var sql = "select * from users where name = ?";
        var ps = conn.prepareStatement(sql);

        ps.setString(1,username);
        var r = ps.executeQuery();
        return r.next();
    }


}
