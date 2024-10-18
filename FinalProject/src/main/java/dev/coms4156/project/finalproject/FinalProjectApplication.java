package dev.coms4156.project.finalproject;

import jakarta.annotation.PreDestroy;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class to start the Spring Boot application for the FinalProject.
 */
@SpringBootApplication
public class FinalProjectApplication {

  public static void main(String[] args) {
    SpringApplication.run(FinalProjectApplication.class, args);
  }

  /**
   * This contains all the setup logic, it will mainly be focused on loading up 
   * and creating an instance of the database based
   * off a saved file or will create a fresh database if the file
   * is not present.
   *
   * @param args A {@code String[]} of any potential runtime args
   */
  public void run(String[] args) {
    for (String arg : args) {
      if ("setup".equals(arg)) {
        myFileDatabase = new MyFileDatabase(false, "./resourceData.txt", "./requestData.txt"); // Reset data
        resetDataFile();
        System.out.println("System Setup");
        return;
      }
    }
    myFileDatabase = new MyFileDatabase(true, "./resourceData.txt", "./requestData.txt");
    System.out.println("Start up");
  }

  /**
   * Overrides the database reference, used when testing.
   *
   * @param testData A {@code MyFileDatabase} object referencing test data.
   */
  public static void overrideDatabase(MyFileDatabase testDatabase) {
    myFileDatabase = testDatabase;
    saveData = false;
  }

  /**
   * Prepare initial data for the database or allows for data to be reset in 
   * event of errors.
   */
  public void resetDataFile() {
    Item[] foodItems = new Item[5];
    foodItems[0] = new Item("Food", 10, LocalDate.now().plusDays(7), "Robert");
    foodItems[1] = new Item("Food", 5, LocalDate.now().plusDays(7), "Fiona");
    foodItems[2] = new Item("Food", 2, LocalDate.now().plusDays(21), "Cici");
    foodItems[3] = new Item("Food-Seafood", 1, LocalDate.now().plusDays(3), "Robert");
    foodItems[4] = new Item("Food-Seafood", 199, LocalDate.now().plusDays(3), "Amy");
    HashMap<String, Item> items = new HashMap<>();
    for (int i = 0; i < foodItems.length; i++) {
      items.put(foodItems[i].getItemId(), foodItems[0]);
    }

    Item[] hygieneItems = new Item[5]; 
    hygieneItems[0] = new Item("Hygiene", 75, LocalDate.now().plusDays(180), "Charlie");
    hygieneItems[1] = new Item("Hygiene", 120, LocalDate.now().plusDays(150), "Ethan");
    hygieneItems[2] = new Item("Hygiene", 60, LocalDate.now().plusDays(100), "Charlotte");
    hygieneItems[3] = new Item("Hygiene", 30, LocalDate.now().plusDays(60), "Benjamin");
    hygieneItems[4] = new Item("Hygiene", 90, LocalDate.now().plusDays(90), "Robert");
    for (int i = 0; i < hygieneItems.length; i++) {
      items.put(hygieneItems[i].getItemId(), hygieneItems[0]);
    }

    Item[] clothingItems = new Item[5]; 
    clothingItems[0] = new Item("Clothing", 5, LocalDate.now().plusDays(180), "Charlie");
    clothingItems[1] = new Item("Clothing", 4, LocalDate.now().plusDays(180), "Olivia");
    clothingItems[2] = new Item("Clothing", 2, LocalDate.now().plusDays(180), "Emma");
    clothingItems[3] = new Item("Clothing", 10, LocalDate.now().plusDays(360), "Amy");
    clothingItems[4] = new Item("Clothing", 8, LocalDate.now().plusDays(360), "Mason");
    for (int i = 0; i < clothingItems.length; i++) {
      items.put(clothingItems[i].getItemId(), clothingItems[0]);
    }

    Item[] medicineItems = new Item[5]; 
    medicineItems[0] = new Item("Medicine", 10, LocalDate.now().plusDays(60), "John");
    medicineItems[1] = new Item("Medicine", 20, LocalDate.now().plusDays(45), "Emma");
    medicineItems[2] = new Item("Medicine", 15, LocalDate.now().plusDays(90), "Lucas");
    medicineItems[3] = new Item("Medicine", 5, LocalDate.now().plusDays(120), "Isabella");
    medicineItems[4] = new Item("Medicine", 25, LocalDate.now().plusDays(30), "Sophia");
    for (int i = 0; i < medicineItems.length; i++) {
      items.put(medicineItems[i].getItemId(), medicineItems[0]);
    }

    Item[] drinkItems = new Item[5];
    drinkItems[0] = new Item("Drink", 50, LocalDate.now().plusDays(14), "Michael");
    drinkItems[1] = new Item("Drink", 100, LocalDate.now().plusDays(10), "Sarah");
    drinkItems[2] = new Item("Drink", 75, LocalDate.now().plusDays(7), "David");
    drinkItems[3] = new Item("Drink", 25, LocalDate.now().plusDays(5), "Amy");
    drinkItems[4] = new Item("Drink", 60, LocalDate.now().plusDays(30), "Amy");
    for (int i = 0; i < drinkItems.length; i++) {
      items.put(drinkItems[i].getItemId(), drinkItems[0]);
    }
    Resource resource1 = new Resource(items, "R_COLUMBIA");
    HashMap<String, Resource> resourceMapping = new HashMap<>();
    resourceMapping.put("R_COLUMBIA", resource1);

    myFileDatabase.setResources(resourceMapping);

    Request[] requests = new Request[5];
    requests[0] = new Request("REQ1", Arrays.asList(foodItems[0].getItemId()), 
    "Pending", "High", "John Doe");
    requests[1] = new Request("REQ2", Arrays.asList(drinkItems[1].getItemId()), 
    "Pending", "Low", "Alice Doe");
    requests[2] = new Request("REQ3", Arrays.asList(clothingItems[2].getItemId(), medicineItems[1].getItemId()), 
    "Pending", "Medium", "John Doe");
    requests[3] = new Request("REQ4", Arrays.asList(clothingItems[2].getItemId()), 
    "Pending", "High", "Jane Doe");
    requests[4] = new Request("REQ5", Arrays.asList(hygieneItems[3].getItemId(), clothingItems[2].getItemId()), 
    "Pending", "High", "Sara Doe");

    myFileDatabase.setRequests(new Scheduler(Arrays.asList(requests)));
  }

  /**
   * This contains all the overheading teardown logic, it will save all the created 
   * user data to a file, so it will be ready for the next setup.
   */
  @PreDestroy
  public void onTermination() {
    System.out.println("Termination");
    if (saveData) {
      myFileDatabase.saveResourcesToFile();
      myFileDatabase.saveRequestsToFile();
    }
  }

  public static MyFileDatabase myFileDatabase;
  private static boolean saveData = true;
}
