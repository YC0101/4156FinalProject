package dev.coms4156.project.finalproject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * This class contains tests for the Resource class.
 */
@SpringBootTest
@ContextConfiguration
public class ResourceUnitTests {

  /**
   * Create variable for testing.
   */
  @BeforeAll
  public static void setupCourseForTesting() {
    testItem = new Item("Food", 10, LocalDate.now().plusDays(5), "Donor123");
    testItem2 = new Item("Medicine", 5, LocalDate.now().minusDays(1), "Donor456");
    testItem3 = new Item("Clothing", 0, null, "Donor789");  // No expiration, but quantity is zero

    testItemMap = new HashMap<>();

  }

  /** The test course instance used for testing. */
  public static Item testItem;
  public static Item testItem2;
  public static Item testItem3;
  public static HashMap<Integer, Item> testItemMap;
}
