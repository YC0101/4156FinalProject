package dev.coms4156.project.finalproject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  public static Map<String, Item> resultToItems(List<Map<String, Object>> queryResult) {
    Map<String, Item> itemsMapping = new HashMap<>();
    for (Map<String, Object> row : queryResult) {
      Item item = new Item((String) row.get("itemType"), (int) row.get("quantity"),
          ((Date) row.get("expirationDate")).toLocalDate(), (String) row.get("donorId"));
      item.setItemId((String) row.get("itemId"));
      itemsMapping.put((String) row.get("itemId"), item);
    }
    return itemsMapping;
  }

  public static List<Request> resultToRequests(List<Map<String, Object>> queryResult) {
    List<Request> requests = new ArrayList<>();
    Map<String, List<String>> requestsItemIds = new HashMap<>();
    for (Map<String, Object> row : queryResult) {
      if (!requestsItemIds.containsKey((String) row.get("requestId"))) {
        List<String> newArray = new ArrayList<String>();
        requestsItemIds.put((String) row.get("requestId"), newArray);
        Request request =
            new Request((String) row.get("requestId"), newArray, (String) row.get("status"),
                (String) row.get("priorityLevel"), (String) row.get("requesterInfo"));
        requests.add(request);
      }
      requestsItemIds.get((String) row.get("requestId")).add((String) row.get("itemId"));
    }
    return requests;
  }

  public Resource fetchResource(String resourceId) {
    String sql = "SELECT * FROM resource WHERE resourceId = ?";
    Map<String, Item> itemsMapping = resultToItems(jdbcTemplate.queryForList(sql, resourceId));
    return new Resource(itemsMapping, resourceId);
  }

  public Resource fetchResource(String resourceId, int limit) {
    String sql = "SELECT * FROM resource WHERE resourceId = ? LIMIT ?";
    Map<String, Item> itemsMapping = resultToItems(jdbcTemplate.queryForList(sql, resourceId, limit));
    return new Resource(itemsMapping, resourceId);
  }

  public Resource fetchItem(String resourceId, String itemId) {
    String sql = "SELECT * FROM resource WHERE resourceId = ? AND itemId = ?";
    Map<String, Item> itemsMapping = resultToItems(jdbcTemplate.queryForList(sql, resourceId, itemId));
    return new Resource(itemsMapping, resourceId);
  }

  public List<Request> fetchRequestsByResource(String resourceId) {
    String sql = "SELECT * FROM request WHERE resourceId = ?";
    return resultToRequests(jdbcTemplate.queryForList(sql, resourceId));
  }

  public List<Request> fetchRequestsByResource(String resourceId, int limit) {
    String sql = "SELECT * FROM request WHERE resourceId = ? LIMIT ?";
    return resultToRequests(jdbcTemplate.queryForList(sql, resourceId, limit));
  }

  public List<Request> fetchRequest(String resourceId, String requestId) {
    String sql = "SELECT * FROM request WHERE resourceId = ? AND requestId = ?";
    return resultToRequests(jdbcTemplate.queryForList(sql, resourceId, requestId));
  }

  public void addItem(Item item, String resourceId) {
    String sql = "INSERT INTO resource\n" + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql, item.getItemId(), item.getItemType(), item.getQuantity(),
        item.getExpirationDate(), item.getStatus(), item.getDonorId(), resourceId);
  }

  public void addRequest(Request request, String resourceId) {
    String sql = "INSERT INTO request\n" + "VALUES (?, ?, ?, ?, ?, ?)";
    for (String itemId : request.getItemIds()) {
      jdbcTemplate.update(sql, request.getRequestId(), itemId, request.getStatus(),
          request.getPriorityLevel(), request.getRequesterInfo(), resourceId);
    }
  }

  public void updateItemQuantity(String itemId, int quantity) {
    String sql = "UPDATE resource SET quantity = ? WHERE itemId = ?";
    jdbcTemplate.update(sql, quantity, itemId);
  }

  public void updateItemStatus(String itemId, String status) {
    String sql = "UPDATE resource SET status = ? WHERE itemId = ?";
    jdbcTemplate.update(sql, status, itemId);
  }

  public void updateRequestStatus(String requestId, String status) {
    String sql = "UPDATE request SET status = ? WHERE requestId = ?";
    jdbcTemplate.update(sql, status, requestId);
  }

  public void delRequestsByResourceId(String resourceId) {
    String sql = "DELETE FROM request WHERE resourceId = ?";
    jdbcTemplate.update(sql, resourceId);
  }

  public void delResourceByResourceId(String resourceId) {
    String sql = "DELETE FROM resource WHERE resourceId = ?";
    jdbcTemplate.update(sql, resourceId);
  }
}
