package dev.coms4156.project.finalproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

/**
 * This class contains tests for the Resource class.
 */
@SpringBootTest
@ContextConfiguration
public class RouteControllerUnitTests {

  /**
   * Create variable for testing.
   */
  @BeforeEach
  public void setupTestDatabase() {
    testRouteController = new RouteController(database);
    testRouteController.resetTestData(resourceId);
  }

  @AfterEach
  public void cleanTestDatabase() {
    testRouteController.resetTestData(resourceId);
  }

  @Test
  public void testCreateDonation() {
    Item item = new Item("Food", 10, LocalDate.now().plusDays(7), "Robert");
    ResponseEntity<?> response = testRouteController.createDonation(resourceId, "Food", 10,
        LocalDate.now().plusDays(7), "Robert");
    item.setItemId(((Item) response.getBody()).getItemId());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(item, response.getBody());

    response = testRouteController.createDonation(resourceId, "Food", -1,
        LocalDate.now().plusDays(7), "Robert");
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void testCreateRequest() {
    Request request = new Request("REQ1", Arrays.asList("ABCD", "EFGH"), Arrays.asList(1, 2),
        "Pending", "High", "John Doe");
    ResponseEntity<?> response =
        testRouteController.createRequest("REQ1", Arrays.asList("ABCD", "EFGH"),
            Arrays.asList(1, 2), "Pending", "High", "John Doe", resourceId);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(request, response.getBody());
  }

  @Test
  public void testRetrieveRequestsByResource() {
    Request request1 = new Request("REQ1", Arrays.asList("ABCD", "EFGH"), Arrays.asList(1, 2),
        "Pending", "High", "John Doe");
    Request request2 = new Request("REQ2", Arrays.asList("ABCD"), Arrays.asList(6, 8), "Pending",
        "Medium", "Amy Doe");
    List<Request> requests = new ArrayList<>();
    database.addRequest(request1, resourceId);
    database.addRequest(request2, resourceId);

    ResponseEntity<?> response = testRouteController.retrieveRequestsByResource(resourceId);
    requests.add(request1);
    requests.add(request2);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(requests, response.getBody());
  }

  @Test
  public void testRetrieveRequest() {
    Request request1 = new Request("REQ1", Arrays.asList("ABCD", "EFGH"), Arrays.asList(1, 2),
        "Pending", "High", "John Doe");
    Request request2 = new Request("REQ2", Arrays.asList("ABCD"), Arrays.asList(6, 8), "Pending",
        "Medium", "Amy Doe");
    database.addRequest(request1, resourceId);
    database.addRequest(request2, resourceId);

    ResponseEntity<?> response = testRouteController.retrieveRequest(resourceId, "REQ2");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(request2, response.getBody());
  }

  @Test
  public void testRetrieveResource() {
    Item item1 = new Item("Food", 10, LocalDate.now().plusDays(7), "Robert");

    Map<String, Item> items = new HashMap<>();
    items.put(item1.getItemId(), item1);
    Resource resource1 = new Resource(items, resourceId);
    database.addItem(item1, resourceId);

    ResponseEntity<?> response = testRouteController.retrieveResource(resourceId);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(resource1, response.getBody());
  }

  @Test
  public void testRetrieveItem() {
    Item item1 = new Item("Food", 10, LocalDate.now().plusDays(7), "Robert");
    Item item2 = new Item("Clothing", 5, LocalDate.now().plusDays(180), "Charlie");
    database.addItem(item1, resourceId);
    database.addItem(item2, resourceId);

    ResponseEntity<?> response = testRouteController.retrieveItem(resourceId, item1.getItemId());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(item1, response.getBody());

    response = testRouteController.retrieveItem(resourceId, "ABCD");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testRetrieveAvailableItems() {
    Item item1 = new Item("Food", 10, LocalDate.now().plusDays(7), "Robert");
    Item item2 = new Item("Clothing", 5, LocalDate.now().plusDays(180), "Charlie");
    List<Item> items = new ArrayList<>();
    item2.markAsDispatched();
    database.addItem(item1, resourceId);
    database.addItem(item2, resourceId);

    ResponseEntity<?> response = testRouteController.retrieveAvailableItems(resourceId);
    items.add(item1);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(items, response.getBody());

    item1.markAsDispatched();
    database.updateItemStatus(resourceId, item1.getItemId(), "dispatched");
    response = testRouteController.retrieveAvailableItems(resourceId);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testRetrieveDispatchedItems() {
    Item item1 = new Item("Food", 10, LocalDate.now().plusDays(7), "Robert");
    Item item2 = new Item("Clothing", 5, LocalDate.now().plusDays(180), "Charlie");
    List<Item> items = new ArrayList<>();
    database.addItem(item1, resourceId);
    database.addItem(item2, resourceId);

    ResponseEntity<?> response = testRouteController.retrieveDispatchedItems(resourceId);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    item1.markAsDispatched();
    database.updateItemStatus(resourceId, item1.getItemId(), "dispatched");
    response = testRouteController.retrieveDispatchedItems(resourceId);
    items.add(item1);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(items, response.getBody());
  }

  @Test
  public void testRetrieveItemsByDonor() {
    Item item1 = new Item("Food", 10, LocalDate.now().plusDays(7), "Robert");
    Item item2 = new Item("Clothing", 5, LocalDate.now().plusDays(180), "Charlie");
    List<Item> items = new ArrayList<>();
    database.addItem(item1, resourceId);
    database.addItem(item2, resourceId);

    ResponseEntity<?> response = testRouteController.retrieveItemsByDonor(resourceId, "Robert");
    items.add(item1);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(items, response.getBody());

    response = testRouteController.retrieveItemsByDonor(resourceId, "Somebody");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  /** The test resource instance used for testing. */
  public static String resourceId = "R_TEST_AUTO";
  public RouteController testRouteController;
  @Autowired
  public DatabaseService database;
}
