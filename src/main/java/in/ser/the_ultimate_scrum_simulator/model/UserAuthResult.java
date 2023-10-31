package in.ser.the_ultimate_scrum_simulator.model;

import java.util.Objects;
import java.util.Optional;

public final class UserAuthResult {
    private final AuthStatus status;
    private final Optional<User> user;
    private final int incorrectPasswordAttempts;

    public UserAuthResult(
            AuthStatus status,
            Optional<User> user,
            int incorrectPasswordAttempts
    ) {
        this.status = status;
        this.user = user;
        this.incorrectPasswordAttempts = incorrectPasswordAttempts;
    }

    public AuthStatus status() {
        return status;
    }

    public Optional<User> user() {
        return user;
    }

    public int incorrectPasswordAttempts() {
        return incorrectPasswordAttempts;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserAuthResult) obj;
        return Objects.equals(this.status, that.status) &&
               Objects.equals(this.user, that.user) &&
               this.incorrectPasswordAttempts == that.incorrectPasswordAttempts;
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, user, incorrectPasswordAttempts);
    }

    @Override
    public String toString() {
        return "UserAuthResult[" +
               "status=" + status + ", " +
               "user=" + user + ", " +
               "incorrectPasswordAttempts=" + incorrectPasswordAttempts + ']';
    }

}
