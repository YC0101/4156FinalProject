package dev.coms4156.project.finalproject;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * This class contains unit tests for the Item class.
 */
@SpringBootTest
@ContextConfiguration
public class ItemUnitTests {

  /** Test items for validation. */
  public static Item validItem;
  public static Item expiredItem;
  public static Item zeroQuantityItem;

  /**
   * Set up the items to be tested before all tests.
   */
  @BeforeAll
  public static void setupItemsForTesting() {
    // Setting up items with different properties for validation testing
    validItem = new Item("Food", 10, LocalDate.now().plusDays(5), "Donor123");
    expiredItem = new Item("Medicine", 5, LocalDate.now().minusDays(1), "Donor456");
    zeroQuantityItem = new Item("Clothing", 0, null, "Donor789");  // No expiration, but quantity is zero
  }

}
