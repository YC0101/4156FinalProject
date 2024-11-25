package dev.coms4156.project.finalproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * Test class for the Request class.
 */
@SpringBootTest
@ContextConfiguration
public class RequestUnitTests {
  private Request request;
  private String requestId = "REQ123";
  private List<String> itemIds = Arrays.asList("ITEM001", "ITEM002", "ITEM003");
  private List<Integer> itemQuantities = Arrays.asList(3, 2, 5);
  private String status = "Pending";
  private String priorityLevel = "High";
  private String requesterInfo = "John Doe";

  @BeforeEach
  public void setUp() {
    request = new Request(requestId, itemIds, itemQuantities, status, priorityLevel, requesterInfo);
  }

  @Test
  public void testCreateRequest() {
    assertNotNull(request);
    assertEquals(requestId, request.getRequestId());
    assertEquals(status, request.getStatus());
    assertEquals(priorityLevel, request.getPriorityLevel());
    assertEquals(requesterInfo, request.getRequesterInfo());
  }

  @Test
  public void testGetItemIds() {
    List<String> retrievedItemIds = request.getItemIds();
    assertEquals(itemIds, retrievedItemIds, "Item IDs should match the initial setup.");
  }

  @Test
  public void testSetStatus() {
    String newStatus = "Dispatched";
    request.setStatus(newStatus);
    assertEquals(newStatus, request.getStatus(), "Status should be updated to 'Dispatched'.");
  }

  @Test
  public void testUpdatePriority() {
    String newPriority = "Low";
    request.setPriority(newPriority);
    assertEquals(newPriority, request.getPriorityLevel(), "Priority should be updated to 'Low'.");
  }

  @Test
  public void testValidateAttributes() {
    assertEquals(true, request.validateAttributes());
    Request invalidRequest = new Request(requestId, itemIds, Arrays.asList(0, -1, 1), status,
        priorityLevel, requesterInfo);
    assertEquals(false, invalidRequest.validateAttributes());
    invalidRequest =
        new Request(requestId, itemIds, Arrays.asList(9, 10), status, priorityLevel, requesterInfo);
    assertEquals(false, invalidRequest.validateAttributes());
    invalidRequest = new Request(requestId, itemIds, itemQuantities, status, "NEW", requesterInfo);
    assertEquals(false, invalidRequest.validateAttributes());
    invalidRequest =
        new Request(requestId, itemIds, itemQuantities, "NEW", priorityLevel, requesterInfo);
    assertEquals(false, invalidRequest.validateAttributes());
  }

  @Test
  public void testEquals() {
    assertEquals(true, request.equals(request));
    assertEquals(false, request.equals(1));
    Request newRequest =
        new Request("REQ_NEW", itemIds, itemQuantities, status, priorityLevel, requesterInfo);
    assertEquals(false, request.equals(newRequest));
    newRequest =
        new Request(requestId, itemIds, itemQuantities, "Dispatched", priorityLevel, requesterInfo);
    assertEquals(false, request.equals(newRequest));
    newRequest = new Request(requestId, itemIds, itemQuantities, status, "Low", requesterInfo);
    assertEquals(false, request.equals(newRequest));
    newRequest = new Request(requestId, Arrays.asList("ITEM004"), itemQuantities, status,
        priorityLevel, requesterInfo);
    assertEquals(false, request.equals(newRequest));
    newRequest = new Request(requestId, itemIds, Arrays.asList(2, 3, 5), status, priorityLevel,
        requesterInfo);
    assertEquals(false, request.equals(newRequest));
    newRequest = new Request(requestId, itemIds, itemQuantities, status, priorityLevel,
        "Bob Doe");
    assertEquals(false, request.equals(newRequest));
  }
}
