package in.ser.the_ultimate_scrum_simulator;

import in.ser.the_ultimate_scrum_simulator.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

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
    public void registerUser() throws SQLException {
        // Test case 1: Successful registration
        Assertions.assertEquals(UserCreateStatus.SUCCESS, this.db.registerUser("new user", "newUser", "password123", 1));

        // Test case 2: Duplicate username, should trigger additional tasks
        Assertions.assertAll("Duplicate username handling",
                () -> Assertions.assertEquals(UserCreateStatus.USERNAME_TAKEN, this.db.registerUser("existing user", "existingUser", "password456", 2)),
                () -> Assertions.assertDoesNotThrow(() -> {
                    // Additional steps for USERNAME_TAKEN case
                    Assertions.assertEquals(UserDeleteStatus.SUCCESS, this.db.deleteUser("existingUser"));
                    Assertions.assertEquals(UserCreateStatus.SUCCESS, this.db.registerUser("existing user", "existingUser", "password789", 3));
                })
        );

        // Test case 3: Invalid username, expect INVALID_USERNAME
        Assertions.assertEquals(UserCreateStatus.INVALID_USERNAME, this.db.registerUser("blank user", "", "password123", 1));

        // Test case 4: Invalid password, expect INVALID_PASSWORD
        Assertions.assertEquals(UserCreateStatus.INVALID_PASSWORD, this.db.registerUser("invalid password", "userWithShortPassword", "short", 1));

        // Cleanup: Assuming deleteUser returns UserDeleteStatus.SUCCESS for successful deletion
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(UserDeleteStatus.SUCCESS, this.db.deleteUser("newUser")));
    }

    @Test
    public void testDeleteUser() {
        // Test Case 1: SUCCESS
        // Adding User for deletion
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(UserCreateStatus.SUCCESS, this.db.registerUser("temp user", "userToDelete", "does not matter", 1)));

        Assertions.assertEquals(UserDeleteStatus.SUCCESS, this.db.deleteUser("userToDelete"));

        // Test case 2: User not found
        Assertions.assertEquals(UserDeleteStatus.USER_NOT_FOUND, this.db.deleteUser("nonexistentUser"));

        // Test case 3: Invalid username
        Assertions.assertEquals(UserDeleteStatus.INVALID_USERNAME, this.db.deleteUser(""));

    }

    @Test
    public void testLogin() throws SQLException {
        // Get the private method using reflection
        Method method = null;
        try {
            method = DbWrapper.class.getDeclaredMethod("setFailedLoginCt", String.class, int.class);
            method.setAccessible(true); // Make the method accessible (since it's private)
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // Invoke the method on reset failed login attempts to 0
        try {
            method.invoke(db, "existingUser", 0);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        // Test Case 1: User Not Found
        Assertions.assertEquals(AuthStatus.USER_NOT_FOUND, this.db.loginWith("No Such User", "why bother!").status());

        // Test Case 2: Wrong Password
        Assertions.assertAll(
                () -> Assertions.assertEquals(AuthStatus.INCORRECT_PASSWORD, this.db.loginWith("existingUser", "wrongPassword").status()),
                () -> Assertions.assertEquals(2, this.db.loginWith("existingUser", "wrongPassword").incorrectPasswordAttempts())
        );

        // Test Case 3: Too many attempts
        Assertions.assertEquals(AuthStatus.INCORRECT_PASSWORD, this.db.loginWith("existingUser", "wrongPassword").status());
        Assertions.assertEquals(AuthStatus.TOO_MANY_FAILED_LOGINS, this.db.loginWith("existingUser", "wrongPassword").status());

        // Invoke the method on reset again failed login attempts to 0
        try {
            method.invoke(db, "existingUser", 0);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        // Test Case 4: Success
        Assertions.assertEquals(AuthStatus.SUCCESS, this.db.loginWith("existingUser", "password789").status());
    }

    @Test
    void validateAndCreateScenario() throws SQLException {
        // Test Case 1: Successful scenario creation
        CreateScenarioStatus status1 = db.validateAndCreateScenario("Title1", "Creator1", 1);
        Assertions.assertEquals(CreateScenarioStatus.SUCCESS, status1);

        // Test Case 2: Invalid title
        CreateScenarioStatus status2 = db.validateAndCreateScenario(null, "Creator2", 1);
        Assertions.assertEquals(CreateScenarioStatus.INVALID_TITLE, status2);

        // Test Case 3: Invalid creator
        CreateScenarioStatus status3 = db.validateAndCreateScenario("Title3", null, 1);
        Assertions.assertEquals(CreateScenarioStatus.INVALID_CREATOR, status3);

        // Test Case 4: Invalid difficulty
        CreateScenarioStatus status4 = db.validateAndCreateScenario("Title4", "Creator4", 3);
        Assertions.assertEquals(CreateScenarioStatus.INVALID_DIFFICULTY, status4);
    }

    @Test
    void testSelectAll() throws SQLException {
        List<String> users = this.db.selectAll();
        boolean found = false;
        for (String usr : users) {
            if (usr.equals("existingUser")) {
                found = true;
                break;
            }
        }
        Assertions.assertTrue(found);
    }

    @Test
    public void testCreateStory() {
        // Assuming you have an instance of DbWrapper, replace with actual instance if needed
        DbWrapper dbWrapper = new DbWrapper();

        // Test data
        String title = "Test Story";
        String description = "This is a test story";
        String owner = "Test Owner";
        int estimate = 5;
        int scenarioId = 1; // Replace with an existing scenario ID

        // Call the method and get the result
        CreateStoryStatus result = dbWrapper.createStory(title, description, owner, estimate, scenarioId);

        // Assert the result
        Assertions.assertEquals(CreateStoryStatus.SUCCESS, result);
    }
}
