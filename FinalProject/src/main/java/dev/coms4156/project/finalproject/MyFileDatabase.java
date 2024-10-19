package dev.coms4156.project.finalproject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * This class represents a file-based database containing Resource mappings.
 */
public class MyFileDatabase {

  /**
   * Constructs a MyFileDatabase object and loads up the database with
   * the contents of the file.
   *
   * @param flag     the flag that controls whether to load up database from file or not
   * @param resourceFilePath the path to the file containing the entries of the resource data
   * @param requestFilePath the path to the file containing the entries of the request data
   */
  public MyFileDatabase(boolean flag, String resourceFilePath, String requestFilePath) {
    this.resourceFilePath = resourceFilePath;
    this.requestFilePath = requestFilePath;
    if (flag == true) {
      this.resourceMapping = deSerializeResourcesFromFile();
      this.scheduler = deSerializeRequestsFromFile();
    }
  }

  /**
   * Sets the Resource mapping of the database.
   *
   * @param resourceMapping the mapping of Resource ID to Resource objects
   */
  public void setResources(HashMap<String, Resource> resourceMapping) {
    this.resourceMapping = resourceMapping;
  }

  /**
   * Sets the Request List of the database.
   *
   * @param requests the List of Request objects
   */
  public void setRequests(Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  /**
   * Deserializes the object from the file and returns the Resource mapping.
   *
   * @return the deserialized Resource mapping
   */
  public HashMap<String, Resource> deSerializeResourcesFromFile() {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(resourceFilePath))) {
      Object obj = in.readObject();
      if (obj instanceof HashMap) {
        return (HashMap<String, Resource>) obj;
      } else {
        throw new IllegalArgumentException("Invalid resource object type in file.");
      }
    } catch (IOException | ClassNotFoundException e) {
      System.err.println(e.toString());
      return new HashMap<>();
    }
  }

  /**
   * Deserializes the object from the file and returns the Request List.
   *
   * @return the deserialized Request List
   */
  public Scheduler deSerializeRequestsFromFile() {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(requestFilePath))) {
      Object obj = in.readObject();
      if (obj instanceof List) {
        return new Scheduler((List<Request>) obj);
      } else {
        throw new IllegalArgumentException("Invalid request object type in file.");
      }
    } catch (IOException | ClassNotFoundException e) {
      System.err.println(e.toString());
      return new Scheduler(new ArrayList<>());
    }
  }

  /**
   * Saves the contents of the internal data structure of resource to the file. Note that contents of 
   * the file are overwritten through this operation.
   */
  public void saveResourcesToFile() {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(resourceFilePath))) {
      out.writeObject(resourceMapping);
      System.out.println("Resource object serialized successfully.");
    } catch (IOException e) {
      System.err.println(e.toString());
    }
  }

  /**
   * Saves the contents of the internal data structure of request to the file. Note that contents of 
   * the file are overwritten through this operation.
   */
  public void saveRequestsToFile() {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(requestFilePath))) {
      out.writeObject(this.scheduler.getRequests());
      System.out.println("Request object serialized successfully.");
    } catch (IOException e) {
      System.err.println(e.toString());
    }
  }

  /**
   * Gets the Resource mapping of the database.
   *
   * @return the Resource mapping
   */
  public HashMap<String, Resource> getResources() {
    return this.resourceMapping;
  }

  /**
   * Gets the Request List of the database as a Scheduler Object
   *
   * @return the Request List
   */
  public Scheduler getRequests() {
    return this.scheduler;
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
    for (Request request : scheduler.getRequests()) {
      result.append(request.toString());
    }
    return result.toString();
  }

  private String resourceFilePath;
  private String requestFilePath;
  private HashMap<String, Resource> resourceMapping;
  private Scheduler scheduler;
}
