package dev.coms4156.project.finalproject;

import java.time.LocalDate;
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
  public ResponseEntity<?> createDonation(@RequestParam(value = "resourceId") String resourceId,
      @RequestParam(value = "itemType") String itemType,
      @RequestParam(value = "quantity") int quantity,
      @RequestParam(value = "expirationDate") LocalDate expirationDate,
      @RequestParam(value = "donorId") String donorId) {
    try {
      Item newItem = new Item(itemType, quantity, expirationDate, donorId);

      if (!newItem.validateAttributes()) {
        return new ResponseEntity<>("Invalid Input Item", HttpStatus.BAD_REQUEST);
      } else {
        database.addItem(newItem, resourceId.toUpperCase(Locale.ENGLISH));
        return new ResponseEntity<>(newItem.toString(), HttpStatus.OK);
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
  public ResponseEntity<?> createRequest(@RequestParam(value = "requestId") String requestId,
      @RequestParam(value = "itemIds") List<String> itemIds,
      @RequestParam(value = "status") String status,
      @RequestParam(value = "priorityLevel") String priorityLevel,
      @RequestParam(value = "requesterInfo") String requesterInfo,
      @RequestParam(value = "resourceId") String resourceId) {
    try {
      Request newRequest = new Request(requestId, itemIds, status, priorityLevel, requesterInfo);
      database.addRequest(newRequest, resourceId.toUpperCase(Locale.ENGLISH));
      return new ResponseEntity<>(newRequest.toString(), HttpStatus.OK);
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
  public ResponseEntity<?> processRequests(@RequestParam(value = "resourceId") String resourceId) {
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

      StringBuilder result = new StringBuilder();
      for (Request request : processResult.getLeft()) {
        database.updateRequestStatus(resourceId, request.getRequestId(), request.getStatus());
        result.append("Dispatched: ").append(request).append("\n");
      }
      return new ResponseEntity<>(result.toString(), HttpStatus.OK);
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
      @RequestParam(value = "resourceId") String resourceId) {
    try {
      List<Request> requests =
          database.fetchRequestsByResource(resourceId.toUpperCase(Locale.ENGLISH));
      if (requests.isEmpty()) {
        return new ResponseEntity<>("Requests By Resource Not Found", HttpStatus.NOT_FOUND);
      } else {
        StringBuilder result = new StringBuilder();
        for (Request request : requests) {
          result.append(request.toString()).append("\n");
        }
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
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
  public ResponseEntity<?> retrieveRequest(@RequestParam(value = "resourceId") String resourceId,
      @RequestParam(value = "requestId") String requestId) {
    try {
      List<Request> requests =
          database.fetchRequest(resourceId.toUpperCase(Locale.ENGLISH), requestId);

      if (requests.isEmpty()) {
        requests = database.fetchRequestsByResource(resourceId.toUpperCase(Locale.ENGLISH), 1);
        if (requests.isEmpty()) {
          return new ResponseEntity<>("Requests By Resource Not Found", HttpStatus.NOT_FOUND);
        } else {
          return new ResponseEntity<>("Request Not Found", HttpStatus.NOT_FOUND);
        }
      } else {
        return new ResponseEntity<>(requests.get(0).toString(), HttpStatus.OK);
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
  public ResponseEntity<?> retrieveResource(@RequestParam(value = "resourceId") String resourceId) {
    try {
      Resource resource = database.fetchResource(resourceId.toUpperCase(Locale.ENGLISH));
      if (resource.getAllItems().isEmpty()) {
        return new ResponseEntity<>("Resource Not Found", HttpStatus.NOT_FOUND);
      } else {
        return new ResponseEntity<>(resource.toString(), HttpStatus.OK);
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
  public ResponseEntity<?> retrieveItem(@RequestParam(value = "resourceId") String resourceId,
      @RequestParam(value = "itemId") String itemId) {
    try {
      Resource resource = database.fetchItem(resourceId.toUpperCase(Locale.ENGLISH), itemId);
      Map<String, Item> itemsMapping = resource.getAllItems();

      if (itemsMapping.isEmpty()) {
        resource = database.fetchResource(resourceId.toUpperCase(Locale.ENGLISH), 1);
        itemsMapping = resource.getAllItems();
        if (itemsMapping.isEmpty()) {
          return new ResponseEntity<>("Resource Not Found", HttpStatus.NOT_FOUND);
        } else {
          return new ResponseEntity<>("Item Not Found", HttpStatus.NOT_FOUND);
        }
      } else {
        return new ResponseEntity<>(resourceId + ": " + itemsMapping.get(itemId).toString(), HttpStatus.OK);
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
      @RequestParam(value = "resourceId") String resourceId) {
    try {
      Map<String, Item> itemsMapping;
      Resource resource = database.fetchResource(resourceId.toUpperCase(Locale.ENGLISH));
      itemsMapping = resource.getAllItems();
      if (itemsMapping.isEmpty()) {
        return new ResponseEntity<>("Resource Not Found", HttpStatus.NOT_FOUND);
      }

      StringBuilder result = new StringBuilder();
      for (Map.Entry<String, Item> entry : itemsMapping.entrySet()) {
        Item item = entry.getValue();
        if ("available".equals(item.getStatus())) {
          result.append(resourceId).append(": ").append(item.toString()).append("\n");
        }
      }

      if (result.length() == 0) {
        return new ResponseEntity<>("No Available Items Found", HttpStatus.NOT_FOUND);
      } else {
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
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
      @RequestParam(value = "resourceId") String resourceId) {
    try {
      Map<String, Item> itemsMapping;
      Resource resource = database.fetchResource(resourceId.toUpperCase(Locale.ENGLISH));
      itemsMapping = resource.getAllItems();
      if (itemsMapping.isEmpty()) {
        return new ResponseEntity<>("Resource Not Found", HttpStatus.NOT_FOUND);
      }

      StringBuilder result = new StringBuilder();
      for (Map.Entry<String, Item> entry : itemsMapping.entrySet()) {
        Item item = entry.getValue();
        if ("dispatched".equals(item.getStatus())) {
          result.append(resourceId).append(": ").append(item.toString()).append("\n");
        }
      }

      if (result.length() == 0) {
        return new ResponseEntity<>("No Dispatched Items Found", HttpStatus.NOT_FOUND);
      } else {
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
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
      @RequestParam(value = "resourceId") String resourceId,
      @RequestParam(value = "donorId") String donorId) {
    try {
      Map<String, Item> itemsMapping;
      Resource resource = database.fetchResource(resourceId.toUpperCase(Locale.ENGLISH));
      itemsMapping = resource.getAllItems();
      if (itemsMapping.isEmpty()) {
        return new ResponseEntity<>("Resource Not Found", HttpStatus.NOT_FOUND);
      }

      StringBuilder result = new StringBuilder();
      for (Map.Entry<String, Item> entry : itemsMapping.entrySet()) {
        Item item = entry.getValue();
        if (donorId.equals(item.getDonorId())) {
          result.append(resourceId).append(": ").append(item.toString()).append("\n");
        }
      }

      if (result.length() == 0) {
        return new ResponseEntity<>("No Items Found", HttpStatus.NOT_FOUND);
      } else {
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
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
      database.delResourceByResourceId(resourceId.toUpperCase(Locale.ENGLISH));
      return new ResponseEntity<>("Reset " + resourceId + " successfully", HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  private ResponseEntity<?> handleException(Exception e) {
    System.err.println(e.toString());
    return new ResponseEntity<>("An Error has occurred", HttpStatus.OK);
  }

}
