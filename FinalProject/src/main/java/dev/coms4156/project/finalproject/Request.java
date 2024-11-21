package dev.coms4156.project.finalproject;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * This class represents a request for resources,
 * tracking its status, items involved, and other details.
 */
public class Request implements Serializable {
  
  @Serial
  private static final long serialVersionUID = 345678L;
  private String requestId;
  private List<String> itemIds;
  private String status;
  private String priorityLevel;
  private String requesterInfo;

  /**
   * Constructs a new Request with specified details.
   *
   * @param requestId The unique identifier for the request.
   * @param itemIds List of item IDs being requested.
   * @param status Current status of the request.
   * @param priorityLevel Priority level of the request.
   * @param requesterInfo Information about the requester.
   */
  public Request(String requestId, List<String> itemIds,
      String status, String priorityLevel, String requesterInfo) {
    this.requestId = requestId;
    this.itemIds = itemIds;
    this.status = status;
    this.priorityLevel = priorityLevel;
    this.requesterInfo = requesterInfo;
  }

  /**
   * Returns the unique identifier of the request.
   *
   * @return the request ID.
   */
  public String getRequestId() {
    return requestId;
  }

  /**
   * Returns the list of item IDs involved in the request.
   *
   * @return list of item IDs.
   */
  public List<String> getItemIds() {
    return itemIds;
  }

  /**
   * Returns the current status of the request.
   *
   * @return The current status of the request.
   */
  public String getStatus() {
    return status;
  }

  /**
   * Returns the priority level of the request.
   *
   * @return The priority level of the request.
   */
  public String getPriorityLevel() {
    return priorityLevel;
  }

  /**
   * Returns information about the requester.
   *
   * @return The requester information.
   */
  public String getRequesterInfo() {
    return requesterInfo;
  }

  /**
   * Updates the status of the request.
   *
   * @param status New status to set for the request.
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Updates the priority level of the request.
   *
   * @param newPriority New priority level to set for the request.
   */
  public void updatePriority(String newPriority) {
    this.priorityLevel = newPriority;
  }

    /**
   * Provides a string representation of the request.
   *
   * @return A string describing the request and its attributes.
   */
  @Override
  public String toString() {
    return "Request[ID=" + requestId + ", itemIds=" + itemIds + ", Status=" + status
        + ", priorityLevel=" + priorityLevel + ", requesterInfo=" + requesterInfo + "]";
  }
}