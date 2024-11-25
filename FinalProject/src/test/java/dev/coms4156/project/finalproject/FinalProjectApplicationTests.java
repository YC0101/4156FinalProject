package dev.coms4156.project.finalproject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  @Test
  public void serverIntegrationTest() {
    finalProjectApplication.overriedDefaultResourceId(resourceId);

    Item testItem1 = new Item("Drink", 30, LocalDate.now().plusDays(14), "Tester");
    Item testItem2 = new Item("Food", 10, LocalDate.now().plusDays(14), "Tester");
    Item testItem3 = new Item("Clothing", 20, LocalDate.now().plusDays(14), "Tester");

    Map<String, Item> items = new HashMap<>();
    items.put(testItem1.getItemId(), testItem1);
    items.put(testItem2.getItemId(), testItem2);
    items.put(testItem3.getItemId(), testItem3);

    Map<String, Item> itemsPartial = new HashMap<>();
    itemsPartial.put(testItem1.getItemId(), testItem1);

    database.addItem(testItem1, resourceId);
    database.addItem(testItem2, resourceId);
    database.addItem(testItem3, resourceId);
    Resource testResource = new Resource(items, resourceId);
    Resource testResourcePartial = new Resource(itemsPartial, resourceId);
    assertEquals(testResource, database.fetchResource(resourceId));
    assertEquals(testResourcePartial, database.fetchItem(resourceId, testItem1.getItemId()));

    database.updateItemQuantity(resourceId, testItem1.getItemId(), 25);
    testItem1.setQuantity(25);
    Map<String, Item> itemsPartialModified = new HashMap<>();
    itemsPartialModified.put(testItem1.getItemId(), testItem1);
    Resource testResourcePartialModified = new Resource(itemsPartialModified, resourceId);
    assertEquals(testResourcePartialModified, database.fetchItem(resourceId,
        testItem1.getItemId()));

    Request testRequest1 = new Request("test123", Arrays.asList(testItem1.getItemId(),
        testItem2.getItemId(), testItem3.getItemId()), Arrays.asList(5, 5, 5), "Pending",
        "High", "TestUser");

    database.addRequest(testRequest1, resourceId);
    List<Request> testRequestList = new ArrayList<>();
    testRequestList.add(testRequest1);
    assertEquals(testRequestList, database.fetchRequestsByResource(resourceId));

    database.updateRequestStatus(resourceId, "test123", "Dispatched");
    testRequest1.setStatus("Dispatched");
    List<Request> testRequestListModified = new ArrayList<>();
    testRequestListModified.add(testRequest1);
    assertEquals(testRequestListModified, database.fetchRequestsByResource(resourceId));

    database.delRequestsByResourceId(resourceId);
    database.delResource(resourceId);
  }

  FinalProjectApplication finalProjectApplication;
  @Autowired
  DatabaseService database;
  public static String resourceId = "R_TEST_AUTO";
}
