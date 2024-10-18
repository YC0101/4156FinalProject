package dev.coms4156.project.finalproject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a file-based database containing Resource mappings.
 */
public class MyFileDatabase {

  /**
   * Constructs a MyFileDatabase object and loads up the database with
   * the contents of the file.
   *
   * @param flag     the flag that controls whether to load database from file or not.
   * @param filePath the path to the file containing the entries of the database
   */
  public MyFileDatabase(boolean flag, String filePath) {
    this.filePath = filePath;
    if (flag == true) {
      this.resourceMapping = deSerializeObjectFromFile();
    }
  }

  /**
   * Sets the Resource mapping of the database.
   *
   * @param mapping the mapping of Resource ID to Resource objects
   */
  public void setMapping(HashMap<String, Resource> mapping) {
    this.resourceMapping = mapping;
  }

  /**
   * Deserializes the object from the file and returns the Resource mapping.
   *
   * @return the deserialized Resource mapping
   */
  public HashMap<String, Resource> deSerializeObjectFromFile() {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
      Object obj = in.readObject();
      if (obj instanceof HashMap) {
        return (HashMap<String, Resource>) obj;
      } else {
        throw new IllegalArgumentException("Invalid object type in file.");
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return new HashMap<>();
    }
  }

  /**
   * Saves the contents of the internal data structure to the file. Note that contents of the file 
   * are overwritten through this operation.
   */
  public void saveContentsToFile() {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
      out.writeObject(resourceMapping);
      System.out.println("Object serialized successfully.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets the Resource mapping of the database.
   *
   * @return the Resource mapping
   */
  public HashMap<String, Resource> getResourceMapping() {
    return this.resourceMapping;
  }

  /**
   * Returns a string representation of the database.
   *
   * @return a string representation of the database
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (Map.Entry<String, Resource> entry : resourceMapping.entrySet()) {
      String key = entry.getKey();
      Resource value = entry.getValue();
      result.append("For the ").append(key).append(" Resource: \n").append(value.toString());
    }
    return result.toString();
  }

  private String filePath;
  private HashMap<String, Resource> resourceMapping;
}

