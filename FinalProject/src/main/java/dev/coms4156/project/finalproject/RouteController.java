package dev.coms4156.project.finalproject;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

  /**
   * Redirects to the homepage.
   *
   * @return A String containing the name of the html file to be loaded.
   */
  @GetMapping({"/", "/index", "/home"})
  public String index() {
    return "Welcome, in order to make an API call direct your browser or Postman to an endpoint "
        + "\n\n This can be done using the following format: \n\n http:127.0.0"
        + ".1:8080/endpoint?arg=value";
  }

  /**
   * Handles the POST request for creating a new donation. It validates the input and
   * triggers the donation creation in the service layer.
   *
   * @param resourceId A {@code String} the unique ID of the resource the item will
   *        be added to.
   *
   * @param itemType A {@code String} representing the type of the item the
   *        donor wants to donate.
   * 
   * @param quantity A {@code int} representing the quantity of the donated item.
   * 
   * @param expirationDate A {@code LocalDate} representing the expiration date
   *        of the donated item.
   * 
   * @param donorId A{@code String} representing the ID of the donor who provided 
   *        the item.
   *
   * @return A {@code ResponseEntity} object containing either the created item as a string
   *         and an HTTP 200 response or, an appropriate message indicating the proper response.
   */
  @PostMapping(value = "/createDonation", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createDonation(
      @RequestParam(value = "resourceId") String resourceId,
      @RequestParam(value = "itemType") String itemType,
      @RequestParam(value = "quantity") int quantity,
      @RequestParam(value = "expirationDate") LocalDate expirationDate,
      @RequestParam(value = "donorId") String donorId
  ) {
    try {
      Item newItem = new Item(itemType, quantity, expirationDate, donorId);

      if (!newItem.validateAttributes()) {
        return new ResponseEntity<>("Invalid Input Item", HttpStatus.BAD_REQUEST);
      } else {
        Resource resource = FinalProjectApplication.myFileDatabase.getResources()
                            .get(resourceId.toUpperCase(Locale.ENGLISH));
        resource.addItem(newItem.getItemId(), newItem);
        return new ResponseEntity<>(newItem.toString(), HttpStatus.OK);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Handles the POST request for creating a new request. 
   *
   * @param requestId A {@code String} the unique ID of the request to be
   *        added to.
   *
   * @param itemIds A {@code List<String>} representing the list of item IDs being requested.
   * 
   * @param status A {@code String} representing the current status of the request.
   * 
   * @param priorityLevel A {@code String} representing the priority level of the request.
   * 
   * @param requesterInfo A{@code String} representing the information about the requester.
   *
   * @return A {@code ResponseEntity} object containing either the unique ID of the created
   *         request and an HTTP 200 response or, an appropriate message indicating the proper 
   *         response.
   */
  @PostMapping(value = "/createRequest", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createRequest(
      @RequestParam(value = "requestId") String requestId,
      @RequestParam(value = "itemIds") List<String> itemIds,
      @RequestParam(value = "status") String status,
      @RequestParam(value = "priorityLevel") String priorityLevel,
      @RequestParam(value = "requesterInfo") String requesterInfo
  ) {
    try {
      Request newRequest = new Request(requestId, itemIds, status, priorityLevel, requesterInfo);
      Scheduler scheduler = FinalProjectApplication.myFileDatabase.getRequests();
      scheduler.addRequest(newRequest);
      return new ResponseEntity<>(newRequest.getRequestId(), HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Attempts to dispatch current requests with specified resource.
   *
   * @param resourceId A {@code String} the unique ID of the request to be
   *        added to.
   *
   * @return A {@code ResponseEntity} object containing either the information about the
   *         dispatched requests and an HTTP 200 response or, an appropriate message indicating 
   *         the proper response.
   */
  @PatchMapping(value = "/processRequests", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> processRequests(@RequestParam(value = "resourceId") String resourceId) {
    try {
      Scheduler scheduler = FinalProjectApplication.myFileDatabase.getRequests();
      Resource resource = FinalProjectApplication.myFileDatabase.getResources()
                            .get(resourceId.toUpperCase(Locale.ENGLISH));
      scheduler.setResource(resource);
      return new ResponseEntity<>(scheduler.processRequests(), HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Returns the details of the resource specified by the resource ID.
   *
   * @param resourceId A {@code String} the unique ID of the resource.
   *
   * @return A {@code ResponseEntity} object containing either the details of the resource and
   *         an HTTP 200 response or, or an error message if the item is not found.
   */
  @GetMapping(value = "/retrieveResource", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveResource(@RequestParam(value = "resourceId") String resourceId) {
    try {
      Resource resource = FinalProjectApplication.myFileDatabase.getResources()
                          .get(resourceId.toUpperCase(Locale.ENGLISH));
      return new ResponseEntity<>(resource.toString(), HttpStatus.OK);
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
   * @return A {@code ResponseEntity} object containing either the details of the item and
   *         an HTTP 200 response or, or an error message if the item is not found.
   */
  @GetMapping(value = "/retrieveItem", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveItem(
      @RequestParam(value = "resourceId") String resourceId,
      @RequestParam(value = "itemId") String itemId
  ) {
    try {
      Map<String, Item> itemsMapping;
      Resource resource = FinalProjectApplication.myFileDatabase.getResources()
                          .get(resourceId.toUpperCase(Locale.ENGLISH));
      itemsMapping = resource.getAllItems();

      if (!itemsMapping.containsKey(itemId)) {
        return new ResponseEntity<>("Item Not Found", HttpStatus.NOT_FOUND);
      } else {
        return new ResponseEntity<>(itemsMapping.get(itemId).toString(),
            HttpStatus.OK);
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
   * @return A {@code ResponseEntity} object containing either the details of all available items
   *         as a string and an HTTP 200 response or, or an error message if no items are found.
   */
  @GetMapping(value = "/retrieveAvailableItems", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveAvailableItems(
      @RequestParam(value = "resourceId") String resourceId
  ) {
    try {
      Map<String, Item> itemsMapping;
      Resource resource = FinalProjectApplication.myFileDatabase.getResources()
                          .get(resourceId.toUpperCase(Locale.ENGLISH));
      itemsMapping = resource.getAllItems();

      StringBuilder result = new StringBuilder();
      for (Map.Entry<String, Item> entry : itemsMapping.entrySet()) {
        String itemId = entry.getKey();
        Item item = entry.getValue();
        if (item.getStatus() == "available") {
          result.append(itemId).append(": ").append(item.toString())
          .append("\n");
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
      @RequestParam(value = "resourceId") String resourceId
  ) {
    try {
      Map<String, Item> itemsMapping;
      Resource resource = FinalProjectApplication.myFileDatabase.getResources()
                          .get(resourceId.toUpperCase(Locale.ENGLISH));
      itemsMapping = resource.getAllItems();

      StringBuilder result = new StringBuilder();
      for (Map.Entry<String, Item> entry : itemsMapping.entrySet()) {
        String itemId = entry.getKey();
        Item item = entry.getValue();
        if (item.getStatus() == "dispatched") {
          result.append(itemId).append(": ").append(item.toString())
          .append("\n");
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
   * @param donorId A{@code String} representing the ID of the donor who provided 
   *        the item.
   *
   * @return A {@code ResponseEntity} object containing either the details of the items and
   *         an HTTP 200 response or, or an error message if no item are found.
   */
  @GetMapping(value = "/retrieveItemsByDonor", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveItemsByDonor(
      @RequestParam(value = "resourceId") String resourceId,
      @RequestParam(value = "donorId") String donorId
  ) {
    try {
      Map<String, Item> itemsMapping;
      Resource resource = FinalProjectApplication.myFileDatabase.getResources()
                          .get(resourceId.toUpperCase(Locale.ENGLISH));
      itemsMapping = resource.getAllItems();

      StringBuilder result = new StringBuilder();
      for (Map.Entry<String, Item> entry : itemsMapping.entrySet()) {
        String itemId = entry.getKey();
        Item item = entry.getValue();
        if (item.getDonorId() == donorId) {
          result.append(itemId).append(": ").append(item.toString())
          .append("\n");
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

  private ResponseEntity<?> handleException(Exception e) {
    System.err.println(e.toString());
    return new ResponseEntity<>("An Error has occurred", HttpStatus.OK);
  }

}