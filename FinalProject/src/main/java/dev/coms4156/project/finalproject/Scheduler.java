package dev.coms4156.project.finalproject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a scheduler that processes requests and schedules dispatches based on
 * resource availability.
 */
public class Scheduler {

  private List<Request> requests; // List of requests to process
  private Map<String, Integer> resourceRepository; // Simulates the resource inventory

  /**
   * Constructs a Scheduler with a given list of requests and resource repository.
   *
   * @param requests           List of requests to be processed.
   * @param resourceRepository Map representing available resources (itemId -> quantity).
   */
  public Scheduler(List<Request> requests, Map<String, Integer> resourceRepository) {
    this.requests = requests;
    this.resourceRepository = resourceRepository;
  }

  /**
   * Processes all the requests by checking availability and scheduling dispatches.
   */
  public void processRequests() {
    for (Request request : requests) {
      if (checkResourceAvailability(request)) {
        scheduleDispatch(request);
      } else {
        System.out.println("Resource(s) unavailable for Request ID: " + request.getRequestId());
      }
    }
  }

  /**
   * Checks if the resources requested are available in sufficient quantities.
   *
   * @param request The request to check.
   * @return true if all requested resources are available, false otherwise.
   */
  private boolean checkResourceAvailability(Request request) {
    for (String itemId : request.getItemIds()) {
      // Check if resource exists and if it has enough quantity
      if (!resourceRepository.containsKey(itemId)
          || resourceRepository.get(itemId) <= 0) { //<= request.getItemNumbers
        return false;
      }
    }
    return true;
  }

  /**
   * Schedules the dispatch for a given request by reducing resource quantities.
   *
   * @param request The request to schedule.
   */
  private void scheduleDispatch(Request request) {
    // Check again if all resources are available
    if (checkResourceAvailability(request)) {
      for (String itemId : request.getItemIds()) {
        if (resourceRepository.containsKey(itemId)) {
          // Decrement resource quantity only when dispatching
          resourceRepository.put(itemId, resourceRepository.get(itemId) - 1);
        }
      }
      request.updateStatus("Dispatched");
      System.out.println("Dispatch scheduled for Request ID: " + request.getRequestId());
    } else {
      System.out.println("Cannot dispatch Request ID: " + request.getRequestId()
          + " due to insufficient resources.");
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
   * Displays the current status of the resource repository.
   */
  public void showResourceRepository() {
    System.out.println("Resource Repository:");
    for (Map.Entry<String, Integer> entry : resourceRepository.entrySet()) {
      System.out.println("Item ID: " + entry.getKey() + ", Quantity: " + entry.getValue());
    }
  }
}
