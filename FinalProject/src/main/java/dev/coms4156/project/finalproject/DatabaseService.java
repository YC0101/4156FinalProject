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
      item.setStatus((String) row.get("status"));
      itemsMapping.put((String) row.get("itemId"), item);
    }
    return itemsMapping;
  }

  public static List<Request> resultToRequests(List<Map<String, Object>> queryResult) {
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

  public Resource fetchResource(String resourceId) {
    String sql = "SELECT * FROM resource WHERE resourceId = ?";
    Map<String, Item> itemsMapping = resultToItems(jdbcTemplate.queryForList(sql, resourceId));
    return new Resource(itemsMapping, resourceId);
  }

  public Resource fetchResource(String resourceId, int limit) {
    String sql = "SELECT * FROM resource WHERE resourceId = ? LIMIT ?";
    Map<String, Item> itemsMapping =
        resultToItems(jdbcTemplate.queryForList(sql, resourceId, limit));
    return new Resource(itemsMapping, resourceId);
  }

  public Resource fetchItem(String resourceId, String itemId) {
    String sql = "SELECT * FROM resource WHERE resourceId = ? AND itemId = ?";
    Map<String, Item> itemsMapping =
        resultToItems(jdbcTemplate.queryForList(sql, resourceId, itemId));
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
    String sql = "INSERT INTO request\n" + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    for (int i = 0; i < request.getItemIds().size(); i++) {
      String itemId = request.getItemIds().get(i);
      Integer itemQuantity = request.getItemQuantities().get(i);
      jdbcTemplate.update(sql, request.getRequestId(), itemId, itemQuantity, request.getStatus(),
          request.getPriorityLevel(), request.getRequesterInfo(), resourceId);
    }
  }

  public void updateItemQuantity(String resourceId, String itemId, int quantity) {
    String sql = "UPDATE resource SET quantity = ? WHERE resourceId = ? AND itemId = ?";
    jdbcTemplate.update(sql, quantity, resourceId, itemId);
  }

  public void updateItemStatus(String resourceId, String itemId, String status) {
    String sql = "UPDATE resource SET status = ? WHERE resourceId = ? AND itemId = ?";
    jdbcTemplate.update(sql, status, resourceId, itemId);
  }

  public void updateRequestStatus(String resourceId, String requestId, String status) {
    String sql = "UPDATE request SET status = ? WHERE resourceId = ? AND requestId = ?";
    jdbcTemplate.update(sql, status, resourceId, requestId);
  }

  public void delRequestsByResourceId(String resourceId) {
    String sql = "DELETE FROM request WHERE resourceId = ?";
    jdbcTemplate.update(sql, resourceId);
  }

  public void delResource(String resourceId) {
    String sql = "DELETE FROM resource WHERE resourceId = ?";
    jdbcTemplate.update(sql, resourceId);
  }
}
