package dev.coms4156.project.finalproject;

import java.util.List;
import java.util.Map;

/**
 * This class represents a scheduler that processes requests and schedules dispatches based on
 * resource availability.
 */
public class Scheduler {

  private List<Request> requests; // List of requests to process
  private Map<String, Item> resourceRepository; // Simulates the resource inventory

  /**
   * Constructs a Scheduler with a given list of requests and resource repository.
   *
   * @param requests           List of requests to be processed.
   * @param resourceRepository Map representing available resources (itemId -> quantity).
   */
  public Scheduler(List<Request> requests, Map<String, Item> resourceRepository) {
    this.requests = requests;
    this.resourceRepository = resourceRepository;
  }

  /**
   * Constructs a Scheduler with a given list of requests.
   *
   * @param requests           List of requests to be processed.
   */
  public Scheduler(List<Request> requests) {
    this.requests = requests;
  }

  /**
   * Processes all the requests by checking availability and scheduling dispatches.
   * 
   * @return The information about all dispatched requests.
   */
  public String processRequests() {
    StringBuilder result = new StringBuilder();
    for (Request request : requests) {
      if (checkResourceAvailability(request) && "Pending".equals(request.getStatus())) {
        result.append(scheduleDispatch(request)).append("\n");
      } else {
        System.out.println("Resource(s) unavailable for Request ID: " + request.getRequestId());
      }
    }
    return result.toString();
  }

  /**
   * Checks if the resources requested are available in sufficient quantities.
   *
   * @param request The request to check.
   * @return true if all requested resources are available, false otherwise.
   */
  public boolean checkResourceAvailability(Request request) {
    for (String itemId : request.getItemIds()) {
      // Check if the resource exists in the repository
      if (!resourceRepository.containsKey(itemId)) {
        System.out.println("Item ID " + itemId + " does not exist in the repository.");
        return false;
      }

      // Get the item and check if it has enough quantity
      Item item = resourceRepository.get(itemId);
      if (item.getQuantity() <= 0) {
//        System.out.println("Item ID " + itemId + " has insufficient quantity.");
        return false;
      }
    }
    return true;
  }


  /**
   * Schedules the dispatch for a given request by reducing resource quantities.
   *
   * @param request The request to schedule.
   * @return The information about the dispatched request.
   */
  public String scheduleDispatch(Request request) {
    // Check if all resources are available
    if (checkResourceAvailability(request)) {
      for (String itemId : request.getItemIds()) {
        // Get the item from the repository
        Item item = resourceRepository.get(itemId);
        // Decrement the item quantity when dispatching ** TO BE MODIFIED
        item.setQuantity(item.getQuantity() - 1);
        System.out.println("Dispatched 1 unit of item ID: " + item.getItemId());

        // If the item quantity reaches zero, update the item status
        if (item.getQuantity() == 0) {
          item.setStatus("dispatched");
          System.out.println("Item ID " + item.getItemId() + " is now fully dispatched.");
        }
      }
      request.updateStatus("Dispatched");
      System.out.println("Dispatch scheduled for Request ID: " + request.getRequestId());
      return "Dispatched: " + request.toString();
    } else {
      System.out.println("Cannot dispatch Request ID: " + request.getRequestId()
          + " due to insufficient resources.");
      return "Cannot dispatch: " + request.toString();
    }
  }

  /**
   * Adds a new request to the list of requests to process.
   *
   * @param request The request to add.
   */
  public void addRequest(Request request) {
    requests.add(request);
  }

  /**
   * Retrieves the list of requests.
   *
   * @return The list of requests.
   */
  public List<Request> getRequests() {
    return requests;
  }

  /**
   * Sets the resourceRepository of the scheduler.
   *
   * @param resource The resource to allocate.
   */
  public void setResource(Resource resource) {
    resourceRepository = resource.getAllItems();
  }
}
