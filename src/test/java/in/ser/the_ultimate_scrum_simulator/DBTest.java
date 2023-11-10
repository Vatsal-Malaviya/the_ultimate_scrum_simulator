package in.ser.the_ultimate_scrum_simulator;

import in.ser.the_ultimate_scrum_simulator.model.UserCreateStatus;
import in.ser.the_ultimate_scrum_simulator.model.UserDeleteStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DBTest {
    private DbWrapper db;

    @BeforeEach
    void setup() {
        // Create a new DbWrapper instance before each test method
        db = new DbWrapper();
    }

    @AfterEach
    void cleanup() {
        // Close the database connection after each test method
        try {
            db.conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getConn() {
        Assertions.assertNotNull(this.db.conn);
    }

    @Test
    public void registerUser() {
        // Test case 1: Successful registration
        Assertions.assertEquals(UserCreateStatus.SUCCESS, this.db.registerUser("newUser", "password123", 1));

        // Test case 2: Duplicate username, should trigger additional tasks
        Assertions.assertAll("Duplicate username handling",
                () -> Assertions.assertEquals(UserCreateStatus.USERNAME_TAKEN, this.db.registerUser("existingUser", "password456", 2)),
                () -> Assertions.assertDoesNotThrow(() -> {
                    // Additional steps for USERNAME_TAKEN case
                    Assertions.assertEquals(UserDeleteStatus.SUCCESS, this.db.deleteUser("existingUser"));
                    Assertions.assertEquals(UserCreateStatus.SUCCESS, this.db.registerUser("existingUser", "password789", 3));
                })
        );

        // Test case 3: Invalid username, expect INVALID_USERNAME
        Assertions.assertEquals(UserCreateStatus.INVALID_USERNAME, this.db.registerUser("", "password123", 1));

        // Test case 4: Invalid password, expect INVALID_PASSWORD
        Assertions.assertEquals(UserCreateStatus.INVALID_PASSWORD, this.db.registerUser("userWithShortPassword", "short", 1));

        // Cleanup: Assuming deleteUser returns UserDeleteStatus.SUCCESS for successful deletion
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(UserDeleteStatus.SUCCESS, this.db.deleteUser("newUser")));
    }

    @Test
    public void testDeleteUser() {
        // Test Case 1: SUCCESS
        // Adding User for deletion
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(UserCreateStatus.SUCCESS, this.db.registerUser("userToDelete", "does not matter", 1)));

        Assertions.assertEquals(UserDeleteStatus.SUCCESS, this.db.deleteUser("userToDelete"));

        // Test case 2: User not found
        Assertions.assertEquals(UserDeleteStatus.USER_NOT_FOUND, this.db.deleteUser("nonexistentUser"));

        // Test case 3: Invalid username
        Assertions.assertEquals(UserDeleteStatus.INVALID_USERNAME, this.db.deleteUser(""));

    }
}
