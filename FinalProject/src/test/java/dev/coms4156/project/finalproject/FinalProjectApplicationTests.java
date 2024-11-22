package dev.coms4156.project.finalproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * This class contains tests for the FinalProjectApplication class.
 */
@SpringBootTest
@ContextConfiguration
public class FinalProjectApplicationTests {

    @BeforeEach
    public void setupForTesting() {
        finalProjectApplication = new FinalProjectApplication(database);
    }

    @Test
    public void testResetData() {
        finalProjectApplication.overriedDefaultResourceId(resourceId);
        Pair<List<Request>, Resource> data = finalProjectApplication.resetData();
        assertEquals(data.getLeft(), database.fetchRequestsByResource(resourceId));
        assertEquals(data.getRight(), database.fetchResource(resourceId));
        database.delRequestsByResourceId(resourceId);
        database.delResource(resourceId);
    }

    FinalProjectApplication finalProjectApplication;
    @Autowired
    DatabaseService database;
    public static String resourceId = "R_TEST_AUTO";
}
