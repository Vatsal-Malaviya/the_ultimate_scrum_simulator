package in.ser.the_ultimate_scrum_simulator.model;

import java.util.Objects;

public final class User {
    private final int id;
    private final String token;
    private static String name;

    private static int accessGroup;

    public User(
            int id,
            String token,
            String name,
            int accessGroup) {
        this.id = id;
        this.token = token;
        User.name = name;
        User.accessGroup = accessGroup;
    }

    public int id() {
        return id;
    }

    public String token() {
        return token;
    }

    public static String name() {
        return name;
    }

    public static int accessGroup(){
        return accessGroup;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (User) obj;
        return this.id == that.id &&
               Objects.equals(this.token, that.token) &&
               Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, name);
    }

    @Override
    public String toString() {
        return "User[" +
               "id=" + id + ", " +
               "token=" + token + ", " +
               "name=" + name + ']';
    }

}
