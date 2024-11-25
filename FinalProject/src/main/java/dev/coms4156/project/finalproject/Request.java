package dev.coms4156.project.finalproject;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * This class represents a request for resources, tracking its status, items involved, and other
 * details.
 */
public class Request implements Serializable {

  @Serial
  private static final long serialVersionUID = 345678L;
  private String requestId;
  private List<String> itemIds;
  private List<Integer> itemQuantities;
  private String status;
  private String priorityLevel;
  private String requesterInfo;

  /**
   * Constructs a new Request with specified details.
   *
   * @param requestId The unique identifier for the request.
   * @param itemIds List of item IDs being requested.
   * @param itemQuantities List of item quantities being requested.
   * @param status Current status of the request.
   * @param priorityLevel Priority level of the request.
   * @param requesterInfo Information about the requester.
   */
  public Request(String requestId, List<String> itemIds, List<Integer> itemQuantities,
      String status, String priorityLevel, String requesterInfo) {
    this.requestId = requestId;
    this.itemIds = itemIds;
    this.itemQuantities = itemQuantities;
    this.status = status;
    this.priorityLevel = priorityLevel;
    this.requesterInfo = requesterInfo;
  }

  /**
   * Validates the request's attributes.
   *
   * @return true if the attributes are valid, false otherwise.
   */
  public boolean validateAttributes() {
    if (itemIds.size() != itemQuantities.size() || itemIds.isEmpty()) {
      return false;
    }
    for (Integer quantity : itemQuantities) {
      if (quantity <= 0) {
        return false;
      }
    }
    return ("Low".equals(priorityLevel) || "Medium".equals(priorityLevel)
            || "High".equals(priorityLevel)) && ("Dispatched".equals(status)
            || "Pending".equals(status));
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
   * Returns the list of item quantities involved in the request.
   *
   * @return list of item IDs.
   */
  public List<Integer> getItemQuantities() {
    return itemQuantities;
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
   * Sets the status of the request.
   *
   * @param status New status to set for the request.
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Sets the priority level of the request.
   *
   * @param newPriority New priority level to set for the request.
   */
  public void setPriority(String newPriority) {
    this.priorityLevel = newPriority;
  }

  /**
   * Provides a string representation of the request.
   *
   * @return A string describing the request and its attributes.
   */
  @Override
  public String toString() {
    return "Request[ID=" + requestId + ", itemIds=" + itemIds + ", itemQuantities=" + itemQuantities
        + ", Status=" + status + ", priorityLevel=" + priorityLevel + ", requesterInfo="
        + requesterInfo + "]";
  }

  /**
   * Returns whether the other object is equal to this one (deep comparison).
   *
   * @return whether the other object is equal to this one.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Request)) {
      return false;
    }
    Request request = (Request) o;
    return requestId.equals(request.requestId) && itemIds.equals(request.itemIds)
        && itemQuantities.equals(request.itemQuantities) && status.equals(request.status)
        && priorityLevel.equals(request.priorityLevel)
        && requesterInfo.equals(request.requesterInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestId, itemIds, itemQuantities, priorityLevel, requesterInfo);
  }
}
