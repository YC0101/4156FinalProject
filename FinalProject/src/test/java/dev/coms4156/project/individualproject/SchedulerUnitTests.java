package dev.coms4156.project.finalproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration
public class SchedulerUnitTests {

  private Scheduler scheduler;
  private Map<String, Integer> resourceRepository;
  private List<Request> requests;

  @BeforeEach
  public void setUp() {
    resourceRepository = new HashMap<>();
    resourceRepository.put("water", 10);
    resourceRepository.put("food", 5);

    requests = new ArrayList<>();
    scheduler = new Scheduler(requests, resourceRepository);
  }

  @Test
  public void testProcessRequests_SuccessfulDispatch() {
    // Adding a valid request where resources are available
    Request request1 = new Request("REQ1", Arrays.asList("water", "food"), "Pending", "High",
        "John Doe");
    requests.add(request1);

    scheduler.processRequests();

    // Check if the resources are updated
    assertEquals(9, resourceRepository.get("water"));
    assertEquals(4, resourceRepository.get("food"));
    assertEquals("Dispatched", request1.getStatus());
  }

  @Test
  public void testProcessRequests_ResourceUnavailable() {
    // Adding a request where one resource is not available
    Request request2 = new Request("REQ2", Arrays.asList("water", "medicine"), "Pending", "Low",
        "Jane Doe");
    requests.add(request2);

    scheduler.processRequests();

    // Check if the status of request2 hasn't changed since "medicine" isn't available
    assertEquals("Pending", request2.getStatus());
  }

  @Test
  public void testProcessRequests_InsufficientResourceQuantity() {
    // Adding a request with quantity that exceeds the available resource
    resourceRepository.put("food", 0); // Only 1 unit of food available

    Request request3 = new Request("REQ3", Arrays.asList("food"), "Pending", "Low", "Jake Doe");
    requests.add(request3);

    scheduler.processRequests();

    // Check if no dispatch occurs due to insufficient quantity
    assertEquals(0, resourceRepository.get("food"));
    assertEquals("Pending", request3.getStatus());
  }

  @Test
  public void testProcessRequests_EmptyRequestList() {
    // Processing requests with an empty list
    scheduler.processRequests();
    // Since no requests are present, nothing should change
    assertTrue(requests.isEmpty());
  }

  @Test
  public void testAddRequest() {
    // Add a request and check if it gets added to the list
    Request request4 = new Request("REQ4", Arrays.asList("water"), "Pending", "Medium",
        "Alice Doe");
    scheduler.addRequest(request4);

    assertEquals(1, requests.size());
    assertEquals("REQ4", requests.get(0).getRequestId());
  }

  @Test
  public void testShowResourceRepository() {
    // Testing if the repository displays correctly (stdout check can be manual)
    scheduler.showResourceRepository();

    // Check if resources remain unchanged
    assertEquals(10, (int) resourceRepository.get("water"));
    assertEquals(5, (int) resourceRepository.get("food"));
  }

  @Test
  public void testProcessMultipleRequests() {
    // Adding multiple requests to test sequential processing
    Request request5 = new Request("REQ5", Arrays.asList("water"), "Pending", "High", "Tom Doe");
    Request request6 = new Request("REQ6", Arrays.asList("food"), "Pending", "Low", "Sara Doe");

    requests.add(request5);
    requests.add(request6);

    scheduler.processRequests();

    // Check if both requests are processed and dispatched
    assertEquals("Dispatched", request5.getStatus());
    assertEquals("Dispatched", request6.getStatus());
    assertEquals(9, resourceRepository.get("water"));
    assertEquals(4, resourceRepository.get("food"));
  }


}
