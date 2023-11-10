package in.ser.the_ultimate_scrum_simulator;

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

}
