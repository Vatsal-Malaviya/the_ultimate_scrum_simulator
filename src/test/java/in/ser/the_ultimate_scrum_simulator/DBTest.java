package in.ser.the_ultimate_scrum_simulator;

import in.ser.the_ultimate_scrum_simulator.model.UserCreateStatus;
import in.ser.the_ultimate_scrum_simulator.model.UserDeleteStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DBTest {
    private DbWrapper db;

    @Test
    void getConn() {
        Assertions.assertDoesNotThrow(() -> {
            this.db = new DbWrapper();
            Assertions.assertNotNull(this.db.conn);
        });
    }

    @Test
    public void registerUser() {
        DbWrapper dbWrapper = new DbWrapper();

        // Test case 1: Successful registration
        Assertions.assertEquals(UserCreateStatus.SUCCESS, dbWrapper.registerUser("newUser", "password123", 1));

        // Test case 2: Duplicate username, should trigger additional tasks
        Assertions.assertAll("Duplicate username handling",
                () -> Assertions.assertEquals(UserCreateStatus.USERNAME_TAKEN, dbWrapper.registerUser("existingUser", "password456", 2)),
                () -> Assertions.assertDoesNotThrow(() -> {
                    // Additional steps for USERNAME_TAKEN case
                    Assertions.assertEquals(UserDeleteStatus.SUCCESS, dbWrapper.deleteUser("existingUser"));
                    Assertions.assertEquals(UserCreateStatus.SUCCESS, dbWrapper.registerUser("existingUser", "password789", 3));
                })
        );

        // Test case 3: Invalid username, expect INVALID_USERNAME
        Assertions.assertEquals(UserCreateStatus.INVALID_USERNAME, dbWrapper.registerUser("", "password123", 1));

        // Test case 4: Invalid password, expect INVALID_PASSWORD
        Assertions.assertEquals(UserCreateStatus.INVALID_PASSWORD, dbWrapper.registerUser("userWithShortPassword", "short", 1));

        // Cleanup: Assuming deleteUser returns UserDeleteStatus.SUCCESS for successful deletion
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(UserDeleteStatus.SUCCESS, dbWrapper.deleteUser("newUser")));
    }

}
