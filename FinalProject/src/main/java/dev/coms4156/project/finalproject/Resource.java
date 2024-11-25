package dev.coms4156.project.finalproject;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a resource providing different items. This class stores information about the
 * resource, including its resourceId, and items offered.
 */
public class Resource implements Serializable {
  public Resource(Map<String, Item> items, String resourceId) {
    this.items = items;
    this.resourceId = resourceId;
  }

  /**
   * Adds an Item to the repository.
   *
   * @param itemId The ID of the new item.
   * @param item The item object to add.
   */
  public void addItem(String itemId, Item item) {
    items.put(itemId, item);
  }

  /**
   * Returns a map of all stored items.
   *
   * @return the hashmap containing all items.
   */
  public Map<String, Item> getAllItems() {
    return this.items;
  }

  /**
   * Remove an item and returns the item removed.
   *
   * @param itemId The ID of the removing item.
   * @return the removed item.
   */
  public Item removeItem(String itemId) {
    return this.items.remove(itemId);
  }

  /**
   * Retrieves an item from the repository by its ID.
   *
   * @param itemId The ID of the removing item.
   * @return the item with the given itemId.
   */
  public Item getItemById(String itemId) {
    return this.items.get(itemId);
  }

  /**
   * Lists all items that have the status "available".
   *
   * @return all items that have the status "available".
   */
  public String listAvailableItems() {
    StringBuilder result = new StringBuilder();
    for (Map.Entry<String, Item> entry : items.entrySet()) {
      String key = entry.getKey();
      Item value = entry.getValue();
      if (value.getStatus() == "available") {
        result.append(resourceId).append(" ").append(key).append(": ").append(value.toString())
            .append("\n");
      }
    }
    return result.toString();
  }


  /**
   * Returns a string representation of the resource, including its id and items inside.
   *
   * @return A string representing the resource.
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (Map.Entry<String, Item> entry : items.entrySet()) {
      String key = entry.getKey();
      Item value = entry.getValue();
      result.append(resourceId).append(" ").append(key).append(": ").append(value.toString())
          .append("\n");
    }
    return result.toString();
  }

  /**
   * Returns whether the other object is equal to this one (deep comparison).
   *
   * @return The other object.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Resource)) {
      return false;
    }
    Resource resource = (Resource) o;
    return items.equals(resource.items) && resourceId.equals(resource.resourceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, resourceId);
  }

  @Serial
  private static final long serialVersionUID = 234567L;
  private Map<String, Item> items;
  private String resourceId;
}
