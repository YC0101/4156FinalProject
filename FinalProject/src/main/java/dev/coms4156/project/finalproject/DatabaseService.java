package dev.coms4156.project.finalproject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * This class is for the connection of database on GCP.
 */
@Service
public class DatabaseService {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  /**
   * Converts a SQL query result of items into a mapping of item IDs to {@link Item} objects.
   *
   * @param queryResult   a list of maps, where each map represents a row in the query result.
   * @return    a map where the keys are item IDs (Strings) and the values are {@link Item} objects.
   */
  private static Map<String, Item> resultToItems(List<Map<String, Object>> queryResult) {
    Map<String, Item> itemsMapping = new HashMap<>();
    for (Map<String, Object> row : queryResult) {
      Item item = new Item((String) row.get("itemType"), (int) row.get("quantity"),
          ((Date) row.get("expirationDate")).toLocalDate(), (String) row.get("donorId"));
      item.setItemId((String) row.get("itemId"));
      item.setStatus((String) row.get("status"));
      itemsMapping.put((String) row.get("itemId"), item);
    }
    return itemsMapping;
  }

  /**
   * Converts a SQL query result of requests into a list of {@link Request} objects,
   * , with each {@link Request} containing its associated item IDs and item quantities.
   *
   * @param queryResult a list of maps, where each map represents a row in the query result.
   * @return a list of {@link Request} objects, each representing a unique request with associated
   */
  private static List<Request> resultToRequests(List<Map<String, Object>> queryResult) {
    List<Request> requests = new ArrayList<>();
    Map<String, List<String>> requestsItemIds = new HashMap<>();
    Map<String, List<Integer>> requestsItemQuantities = new HashMap<>();
    for (Map<String, Object> row : queryResult) {
      if (!requestsItemIds.containsKey((String) row.get("requestId"))) {
        List<String> newArrayStr = new ArrayList<>();
        List<Integer> newArrayInt = new ArrayList<>();
        requestsItemIds.put((String) row.get("requestId"), newArrayStr);
        requestsItemQuantities.put((String) row.get("requestId"), newArrayInt);
        Request request = new Request((String) row.get("requestId"), newArrayStr, newArrayInt,
            (String) row.get("status"), (String) row.get("priorityLevel"),
            (String) row.get("requesterInfo"));
        requests.add(request);
      }
      requestsItemIds.get((String) row.get("requestId")).add((String) row.get("itemId"));
      requestsItemQuantities.get((String) row.get("requestId"))
          .add((Integer) row.get("itemQuantity"));
    }
    return requests;
  }

  /**
   * Fetches a {@link Resource} object by its resource ID from the database.
   *
   * @param resourceId  the ID of the resource to fetch (String).
   * @return a {@link Resource} object containing the resource details and a mapping of
   *          associated item IDs to their corresponding {@link Item} objects.
   */
  public Resource fetchResource(String resourceId) {
    String sql = "SELECT * FROM resource WHERE resourceId = ?";
    Map<String, Item> itemsMapping = resultToItems(jdbcTemplate.queryForList(sql, resourceId));
    return new Resource(itemsMapping, resourceId);
  }

  /**
   * Fetches a {@link Resource} object by its resource ID from the database,
   * with a limit on the number of items retrieved.
   *
   * @param resourceId  the ID of the resource to fetch (String).
   * @param limit the maximum number of items to retrieve for the resource (int).
   * @return a {@link Resource} object containing the resource details and a mapping of
   *          associated item IDs to their corresponding {@link Item} objects.
   */
  public Resource fetchResource(String resourceId, int limit) {
    String sql = "SELECT * FROM resource WHERE resourceId = ? LIMIT ?";
    Map<String, Item> itemsMapping =
        resultToItems(jdbcTemplate.queryForList(sql, resourceId, limit));
    return new Resource(itemsMapping, resourceId);
  }

  /**
   * Fetches a specific {@link Item} associated with a given resource ID from the database.
   *
   * @param resourceId  the ID of the resource to fetch (String).
   * @param itemId  the ID of the item to fetch (String).
   * @return a {@link Resource} object containing the requested {@link Item} mapped by its ID, and
   *          associated with the specified `resourceId`.
   */
  public Resource fetchItem(String resourceId, String itemId) {
    String sql = "SELECT * FROM resource WHERE resourceId = ? AND itemId = ?";
    Map<String, Item> itemsMapping =
        resultToItems(jdbcTemplate.queryForList(sql, resourceId, itemId));
    return new Resource(itemsMapping, resourceId);
  }

  /**
   * Fetches a list of {@link Request} objects associated with a given resource ID from the
   * database.
   *
   * @param resourceId  the ID of the resource to fetch (String).
   * @return a list of {@link Request} objects representing all requests associated with the
   *         specified `resourceId`. If no requests are found, an empty list is returned.
   */
  public List<Request> fetchRequestsByResource(String resourceId) {
    String sql = "SELECT * FROM request WHERE resourceId = ?";
    return resultToRequests(jdbcTemplate.queryForList(sql, resourceId));
  }

  /**
   * Fetches a list of {@link Request} objects associated with a given resource ID from the
   * database, with a limit on the number of requests retrieved.
   *
   * @param resourceId  the ID of the resource to fetch (String).
   * @param limit the maximum number of items to retrieve for the resource (int).
   * @return a list of {@link Request} objects representing all requests associated with the
   *         specified `resourceId`. If no requests are found, an empty list is returned.
   */
  public List<Request> fetchRequestsByResource(String resourceId, int limit) {
    String sql = "SELECT * FROM request WHERE resourceId = ? LIMIT ?";
    return resultToRequests(jdbcTemplate.queryForList(sql, resourceId, limit));
  }

  /**
   * Fetches a {@link Request} object associated with a given resource ID from the database.
   *
   * @param resourceId  the ID of the resource to fetch (String).
   * @param requestId the ID of the request to fetch (String).
   * @return a list of {@link Request} objects representing all requests associated with the
   *          specified `resourceId` and 'requestId'. If no requests are found, an
   *          empty list is returned.
   */
  public List<Request> fetchRequest(String resourceId, String requestId) {
    String sql = "SELECT * FROM request WHERE resourceId = ? AND requestId = ?";
    return resultToRequests(jdbcTemplate.queryForList(sql, resourceId, requestId));
  }

  /**
   * Adds a new {@link Item} to the resource table in the database.
   *
   * @param item the {@link Item} object containing the details of the item to be added.
   * @param resourceId the ID of the resource with which the item is associated (String).
   *                   This is used to link the item to the appropriate resource in the database.
   */
  public void addItem(Item item, String resourceId) {
    String sql = "INSERT INTO resource\n" + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql, item.getItemId(), item.getItemType(), item.getQuantity(),
        item.getExpirationDate(), item.getStatus(), item.getDonorId(), resourceId);
  }

  /**
   * Adds a {@link Request} to the database, associating it with a specific resource ID.
   *
   * @param request the {@link Request} object containing the details of the request to be added.
   * @param resourceId the ID of the resource to which the request is associated (String).
   *                   This links the request to the appropriate resource in the database.
   */
  public void addRequest(Request request, String resourceId) {
    String sql = "INSERT INTO request\n" + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    for (int i = 0; i < request.getItemIds().size(); i++) {
      String itemId = request.getItemIds().get(i);
      Integer itemQuantity = request.getItemQuantities().get(i);
      jdbcTemplate.update(sql, request.getRequestId(), itemId, itemQuantity, request.getStatus(),
          request.getPriorityLevel(), request.getRequesterInfo(), resourceId);
    }
  }

  /**
   * Updates the quantity of a specific item associated with a given resource ID in the database.
   *
   * @param resourceId the ID of the resource to which the item belongs (String). This identifies
   *                   the resource in the database.
   * @param itemId the ID of the item whose quantity is to be updated (String). This identifies
   *               the specific item in the database.
   * @param quantity the new quantity to set for the specified item (int).
   */
  public void updateItemQuantity(String resourceId, String itemId, int quantity) {
    String sql = "UPDATE resource SET quantity = ? WHERE resourceId = ? AND itemId = ?";
    jdbcTemplate.update(sql, quantity, resourceId, itemId);
  }

  /**
   * Updates the status of a specific item associated with a given resource ID in the database.
   *
   * @param resourceId the ID of the resource to which the item belongs (String). This identifies
   *                   the resource in the database.
   * @param itemId the ID of the item whose status is to be updated (String). This identifies
   *               the specific item in the database.
   * @param status the new status to set for the specified item (String). This should represent
   *               a valid status value.
   */
  public void updateItemStatus(String resourceId, String itemId, String status) {
    String sql = "UPDATE resource SET status = ? WHERE resourceId = ? AND itemId = ?";
    jdbcTemplate.update(sql, status, resourceId, itemId);
  }

  /**
   * Updates the status of a specific request associated with a given resource ID in the database.
   *
   * @param resourceId the ID of the resource to which the request belongs (String). This identifies
   *                   the resource in the database.
   * @param requestId the ID of the request whose status is to be updated (String). This identifies
   *                  the specific request in the database.
   * @param status the new status to set for the specified request (String). This should represent
   *               a valid status value.
   */
  public void updateRequestStatus(String resourceId, String requestId, String status) {
    String sql = "UPDATE request SET status = ? WHERE resourceId = ? AND requestId = ?";
    jdbcTemplate.update(sql, status, resourceId, requestId);
  }

  /**
   * Deletes all requests associated with a specific resource ID from the database.
   *
   * @param resourceId the ID of the resource whose associated requests are to be deleted (String).
   *                   This identifies the resource in the database.
   */
  public void delRequestsByResourceId(String resourceId) {
    String sql = "DELETE FROM request WHERE resourceId = ?";
    jdbcTemplate.update(sql, resourceId);
  }

  /**
   * Deletes a specific resource from the database.
   *
   * @param resourceId the ID of the resource to be deleted (String). This identifies the resource
   *                   in the database.
   */
  public void delResource(String resourceId) {
    String sql = "DELETE FROM resource WHERE resourceId = ?";
    jdbcTemplate.update(sql, resourceId);
  }
}
