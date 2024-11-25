package dev.coms4156.project.finalproject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class contains all the API routes for the system.
 */
@RestController
public class RouteController {

  private DatabaseService database;

  public RouteController(DatabaseService database) {
    this.database = database;
  }

  /**
   * Redirects to the homepage.
   *
   * @return A String containing the name of the html file to be loaded.
   */
  @GetMapping({"/", "/index", "/home"})
  public String index() {
    return "Welcome, in order to make an API call direct your browser or Postman to an endpoint "
        + "\n\n This can be done using the following format: \n\n 127.0.0"
        + ".1:8080/endpoint?arg=value";
  }

  /**
   * Handles the POST request for creating a new donation. It validates the input and triggers the
   * donation creation in the service layer.
   *
   * @param resourceId A {@code String} the unique ID of the resource the item will be added to.
   *
   * @param itemType A {@code String} representing the type of the item the donor wants to donate.
   * 
   * @param quantity A {@code int} representing the quantity of the donated item.
   * 
   * @param expirationDate A {@code LocalDate} representing the expiration date of the donated item.
   * 
   * @param donorId A{@code String} representing the ID of the donor who provided the item.
   *
   * @return A {@code ResponseEntity} object containing either the created item as a string and an
   *         HTTP 200 response or, an appropriate message indicating the proper response.
   */
  @PostMapping(value = "/createDonation", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createDonation(@RequestParam("resourceId") String resourceId,
      @RequestParam("itemType") String itemType,
      @RequestParam("quantity") int quantity,
      @RequestParam("expirationDate") LocalDate expirationDate,
      @RequestParam("donorId") String donorId) {
    try {
      Item newItem = new Item(itemType, quantity, expirationDate, donorId);

      if (!newItem.validateAttributes()) {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "Invalid Input Item");
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
      } else {
        database.addItem(newItem, resourceId.toUpperCase(Locale.ENGLISH));
        return new ResponseEntity<>(newItem, HttpStatus.OK);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Handles the POST request for creating a new request.
   *
   * @param requestId A {@code String} the unique ID of the request to be added to.
   *
   * @param itemIds A {@code List<String>} representing the list of item IDs being requested.
   * 
   * @param status A {@code String} representing the current status of the request.
   * 
   * @param priorityLevel A {@code String} representing the priority level of the request.
   * 
   * @param requesterInfo A{@code String} representing the information about the requester.
   *
   * @return A {@code ResponseEntity} object containing either the unique ID of the created request
   *         and an HTTP 200 response or, an appropriate message indicating the proper response.
   */
  @PostMapping(value = "/createRequest", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createRequest(@RequestParam("requestId") String requestId,
      @RequestParam("itemIds") List<String> itemIds,
      @RequestParam("itemQuantities") List<Integer> itemQuantities,
      @RequestParam("status") String status,
      @RequestParam("priorityLevel") String priorityLevel,
      @RequestParam("requesterInfo") String requesterInfo,
      @RequestParam("resourceId") String resourceId) {
    try {
      Request newRequest =
          new Request(requestId, itemIds, itemQuantities, status, priorityLevel, requesterInfo);

      if(!newRequest.validateAttributes()) {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "Invalid Input Request");
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
      } else {
        database.addRequest(newRequest, resourceId.toUpperCase(Locale.ENGLISH));
        return new ResponseEntity<>(newRequest, HttpStatus.OK);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Attempts to dispatch current requests with specified resource.
   *
   * @param resourceId A {@code String} the unique ID of the request to be added to.
   *
   * @return A {@code ResponseEntity} object containing either the information about the dispatched
   *         requests and an HTTP 200 response or, an appropriate message indicating the proper
   *         response.
   */
  @PatchMapping(value = "/processRequests", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> processRequests(@RequestParam("resourceId") String resourceId) {
    try {
      List<Request> requests =
          database.fetchRequestsByResource(resourceId.toUpperCase(Locale.ENGLISH));
      Resource resource = database.fetchResource(resourceId.toUpperCase(Locale.ENGLISH));
      Scheduler scheduler = new Scheduler(requests, resource.getAllItems());
      Pair<List<Request>, Set<Item>> processResult = scheduler.processRequests();

      for (Item item : processResult.getRight()) {
        database.updateItemQuantity(resourceId, item.getItemId(), item.getQuantity());
        database.updateItemStatus(resourceId, item.getItemId(), item.getStatus());
      }

      Map<String, List<Request>> result = new HashMap<>();
      result.put("Dispatched", new ArrayList<>());
      for (Request request : processResult.getLeft()) {
        database.updateRequestStatus(resourceId, request.getRequestId(), request.getStatus());
        result.get("Dispatched").add(request);
      }
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Returns the details of all requests on a specific resource.
   *
   * @param resourceId A {@code String} the unique ID of the resource.
   *
   * @return A {@code ResponseEntity} object containing either the details of the requests and an
   *         HTTP 200 response or, or an error message if no requests are not found.
   */
  @GetMapping(value = "/retrieveRequestsByResource", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveRequestsByResource(
      @RequestParam("resourceId") String resourceId) {
    try {
      List<Request> requests =
          database.fetchRequestsByResource(resourceId.toUpperCase(Locale.ENGLISH));
      if (requests.isEmpty()) {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "Requests By Resource Not Found");
        message.put("resourceId", resourceId);
        return new ResponseEntity<>(message, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(requests, HttpStatus.OK);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Returns the details of the request specified by the request ID.
   *
   * @param resourceId A {@code String} the unique ID of the request.
   *
   * @return A {@code ResponseEntity} object containing either the details of the request and an
   *         HTTP 200 response or, or an error message if no requests are not found.
   */
  @GetMapping(value = "/retrieveRequest", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveRequest(@RequestParam("resourceId") String resourceId,
      @RequestParam("requestId") String requestId) {
    try {
      List<Request> requests =
          database.fetchRequest(resourceId.toUpperCase(Locale.ENGLISH), requestId);

      if (requests.isEmpty()) {
        requests = database.fetchRequestsByResource(resourceId.toUpperCase(Locale.ENGLISH), 1);
        if (requests.isEmpty()) {
          Map<String, Object> message = new HashMap<>();
          message.put("message", "Requests By Resource Not Found");
          message.put("resourceId", resourceId);
          return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
          Map<String, Object> message = new HashMap<>();
          message.put("message", "Request Not Found");
          message.put("resourceId", resourceId);
          return new ResponseEntity<>(message, HttpStatus.OK);
        }
      } else {
        return new ResponseEntity<>(requests.get(0), HttpStatus.OK);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Returns the details of the resource specified by the resource ID.
   *
   * @param resourceId A {@code String} the unique ID of the resource.
   *
   * @return A {@code ResponseEntity} object containing either the details of the resource and an
   *         HTTP 200 response or, or an error message if the item is not found.
   */
  @GetMapping(value = "/retrieveResource", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveResource(@RequestParam("resourceId") String resourceId) {
    try {
      Resource resource = database.fetchResource(resourceId.toUpperCase(Locale.ENGLISH));
      if (resource.getAllItems().isEmpty()) {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "Resource Not Found");
        message.put("resourceId", resourceId);
        return new ResponseEntity<>(message, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(resource, HttpStatus.OK);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Returns the details of the item specified by the item ID.
   *
   * @param resourceId A {@code String} the unique ID of the resource.
   * 
   * @param itemId A {@code String} the unique ID of the item.
   *
   * @return A {@code ResponseEntity} object containing either the details of the item and an HTTP
   *         200 response or, or an error message if the item is not found.
   */
  @GetMapping(value = "/retrieveItem", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveItem(@RequestParam("resourceId") String resourceId,
      @RequestParam("itemId") String itemId) {
    try {
      Resource resource = database.fetchItem(resourceId.toUpperCase(Locale.ENGLISH), itemId);
      Map<String, Item> itemsMapping = resource.getAllItems();

      if (itemsMapping.isEmpty()) {
        resource = database.fetchResource(resourceId.toUpperCase(Locale.ENGLISH), 1);
        itemsMapping = resource.getAllItems();
        if (itemsMapping.isEmpty()) {
          Map<String, Object> message = new HashMap<>();
          message.put("message", "Resource Not Found");
          message.put("resourceId", resourceId);
          return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
          Map<String, Object> message = new HashMap<>();
          message.put("message", "Item Not Found");
          message.put("resourceId", resourceId);
          message.put("itemId", itemId);
          return new ResponseEntity<>(message, HttpStatus.OK);
        }
      } else {
        return new ResponseEntity<>(itemsMapping.get(itemId), HttpStatus.OK);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Returns all available items, that is, items with status code 'available'.
   *
   * @param resourceId A {@code String} the unique ID of the resource.
   * 
   * @return A {@code ResponseEntity} object containing either the details of all available items as
   *         a string and an HTTP 200 response or, or an error message if no items are found.
   */
  @GetMapping(value = "/retrieveAvailableItems", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveAvailableItems(
      @RequestParam("resourceId") String resourceId) {
    try {
      Map<String, Item> itemsMapping;
      Resource resource = database.fetchResource(resourceId.toUpperCase(Locale.ENGLISH));
      itemsMapping = resource.getAllItems();
      if (itemsMapping.isEmpty()) {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "Resource Not Found");
        message.put("resourceId", resourceId);
        return new ResponseEntity<>(message, HttpStatus.OK);
      }

      List<Item> result = new ArrayList<>();
      for (Map.Entry<String, Item> entry : itemsMapping.entrySet()) {
        Item item = entry.getValue();
        if ("available".equals(item.getStatus())) {
          result.add(item);
        }
      }

      if (result.isEmpty()) {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "No Available Items Found");
        message.put("resourceId", resourceId);
        return new ResponseEntity<>(message, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(result, HttpStatus.OK);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Returns all dispatched items, that is, items with status code 'dispatched'.
   *
   * @param resourceId A {@code String} the unique ID of the resource.
   *
   * @return A {@code ResponseEntity} object containing either the details of all dispatched items
   *         as a string and an HTTP 200 response or, or an error message if no items are found.
   */
  @GetMapping(value = "/retrieveDispatchedItems", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveDispatchedItems(
      @RequestParam("resourceId") String resourceId) {
    try {
      Map<String, Item> itemsMapping;
      Resource resource = database.fetchResource(resourceId.toUpperCase(Locale.ENGLISH));
      itemsMapping = resource.getAllItems();
      if (itemsMapping.isEmpty()) {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "Resource Not Found");
        message.put("resourceId", resourceId);
        return new ResponseEntity<>(message, HttpStatus.OK);
      }

      List<Item> result = new ArrayList<>();
      for (Map.Entry<String, Item> entry : itemsMapping.entrySet()) {
        Item item = entry.getValue();
        if ("dispatched".equals(item.getStatus())) {
          result.add(item);
        }
      }

      if (result.isEmpty()) {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "No Dispatched Items Found");
        message.put("resourceId", resourceId);
        return new ResponseEntity<>(message, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(result, HttpStatus.OK);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Returns the details of the items provided by a specified donor.
   *
   * @param resourceId A {@code String} the unique ID of the resource.
   *
   * @param donorId A{@code String} representing the ID of the donor who provided the item.
   *
   * @return A {@code ResponseEntity} object containing either the details of the items and an HTTP
   *         200 response or, or an error message if no item are found.
   */
  @GetMapping(value = "/retrieveItemsByDonor", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveItemsByDonor(
      @RequestParam("resourceId") String resourceId,
      @RequestParam("donorId") String donorId) {
    try {
      Map<String, Item> itemsMapping;
      Resource resource = database.fetchResource(resourceId.toUpperCase(Locale.ENGLISH));
      itemsMapping = resource.getAllItems();
      if (itemsMapping.isEmpty()) {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "Resource Not Found");
        message.put("resourceId", resourceId);
        return new ResponseEntity<>(message, HttpStatus.OK);
      }

      List<Item> result = new ArrayList<>();
      for (Map.Entry<String, Item> entry : itemsMapping.entrySet()) {
        Item item = entry.getValue();
        if (donorId.equals(item.getDonorId())) {
          result.add(item);
        }
      }

      if (result.isEmpty()) {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "Items By Donor Not Found");
        message.put("resourceId", resourceId);
        message.put("donorId", donorId);
        return new ResponseEntity<>(message, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(result, HttpStatus.OK);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Attempts to reset and clear all test data.
   *
   * @param resourceId A {@code String} the unique ID of the test resource (default R_TEST).
   */
  @DeleteMapping(value = "/resetTestData", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> resetTestData(
      @RequestParam(value = "resourceId", defaultValue = "R_TEST") String resourceId) {
    try {
      database.delRequestsByResourceId(resourceId.toUpperCase(Locale.ENGLISH));
      database.delResource(resourceId.toUpperCase(Locale.ENGLISH));
      Map<String, Object> message = new HashMap<>();
      message.put("message", "Reset " + resourceId + " successfully");
      return new ResponseEntity<>(message, HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  private ResponseEntity<?> handleException(Exception e) {
    System.err.println(e.toString());
    return new ResponseEntity<>("An Error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
