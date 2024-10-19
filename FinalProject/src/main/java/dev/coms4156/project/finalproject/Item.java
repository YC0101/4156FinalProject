package dev.coms4156.project.finalproject;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents an item being managed by the Resource Management System (RMS).
 * This class handles individual item attributes such as type, quantity, expiration date, 
 * status, and donor information.
 */
public class Item implements Serializable {

  @Serial
  private static final long serialVersionUID = 123456L;
  private String itemId;       // Unique identifier for the item
  private String itemType;     // Type of the item (e.g., "Food", "Clothing")
  private int quantity;        // Quantity of the item
  private LocalDate expirationDate; // Expiration date, if applicable
  private String status;       // Status of the item ("available", "dispatched", "unknown")
  private String donorId;      // Identifier of the donor who provided the item

  /**
   * Constructor to create a new Item object.
   *
   * @param itemType       The type of the item.
   * @param quantity       The quantity of the item.
   * @param expirationDate The expiration date of the item, if applicable.
   * @param donorId        The ID of the donor who provided the item.
   */
  public Item(String itemType, int quantity, LocalDate expirationDate, String donorId) {
    this.itemId = generateUniqueItemId(); // Generate a unique ID for the item
    this.itemType = itemType;
    this.quantity = quantity;
    this.expirationDate = expirationDate;
    this.status = "available"; // Default status is "available"
    this.donorId = donorId;
  }

  /**
   * Marks the item as dispatched by updating the status.
   */
  public void markAsDispatched() {
    this.status = "dispatched";
  }

  /**
   * Marks the item as unknown by updating the status.
   */
  public void markAsUnknown() {
    this.status = "unknown";
  }

  /**
   * Validates the item's attributes.
   * Check whether the current attributes of the item 
   * (such as quantity and expirationDate) are valid
   *
   * @return true if the attributes are valid, false otherwise.
   */
  public boolean validateAttributes() {
    if (this.quantity <= 0) {
      return false; // Quantity must be positive
    }
    if (this.expirationDate != null && this.expirationDate.isBefore(LocalDate.now())) {
      return false; // Expiration date must not be in the past
    }
    return true;
  }

  /**
   * Generates a unique item ID using UUID.
   *
   * @return A unique identifier for the item.
   */
  public static String generateUniqueItemId() {
    return UUID.randomUUID().toString();
  }

  // Getters for the item attributes
  public String getItemId() {
    return this.itemId;
  }

  public String getItemType() {
    return this.itemType;
  }

  public int getQuantity() {
    return this.quantity;
  }

  public LocalDate getExpirationDate() {
    return this.expirationDate;
  }

  public String getStatus() {
    return this.status;
  }

  public String getDonorId() {
    return this.donorId;
  }

  // Setters for modifiable attributes
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public void setExpirationDate(LocalDate expirationDate) {
    this.expirationDate = expirationDate;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Provides a string representation of the item.
   *
   * @return A string describing the item and its attributes.
   */
  @Override
  public String toString() {
    return "Item[ID=" + this.itemId + ", Type=" + this.itemType + ", Quantity=" + this.quantity
        + ", Expiration=" + this.expirationDate + ", Status="
        + this.status + ", DonorID=" + this.donorId + "]";
  }
}
