package dev.coms4156.project.finalproject;

import jakarta.annotation.PreDestroy;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * The main class to start the Spring Boot application for the FinalProject.
 */
@SpringBootApplication
public class FinalProjectApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(FinalProjectApplication.class, args);
  }

  public FinalProjectApplication(DatabaseService database) {
    this.database = database;
    defaultResourceId = "R_COLUMBIA";
  }

  /**
   * This contains all the setup logic, it will mainly be focused on loading up and creating an
   * instance of the database based off a saved file or will create a fresh database if the file is
   * not present.
   *
   * @param args A {@code String[]} of any potential runtime args
   */
  public void run(String[] args) {
    for (String arg : args) {
      if ("Factory Reset".equals(arg)) {
        try (Scanner scanner = new Scanner(System.in)) {
          System.out.print("Are you sure you want to restore to factory settings? (Y/N): ");
          while (true) {
            String input = scanner.nextLine().trim().toUpperCase(Locale.ENGLISH);
            if ("Y".equals(input)) {
              System.out.println(" -- Reseting Database");
              resetData();
              System.out.println("System Setup");
              break;
            } else if ("N".equals(input)) {
              System.out.println("Factory reset has been cancelled.");
              break;
            } else {
              System.out.println("Invalid input. Please enter 'Y' for Yes or 'N' for No.");
            }
          }
        }
      } else {
        System.out.println("Arg detected:" + arg);
        return;
      }
    }
    System.out.println("Start up");
  }

  /**
   * Prepare initial data for the database or allows for data to be reset in event of errors.
   */
  public Pair<List<Request>, Resource> resetData() {
    database.delRequestsByResourceId(defaultResourceId);
    database.delResource(defaultResourceId);

    Item[] foodItemArray = new Item[5];
    foodItemArray[0] = new Item("Food", 10, LocalDate.now().plusDays(7), "Robert");
    foodItemArray[1] = new Item("Food", 5, LocalDate.now().plusDays(7), "Fiona");
    foodItemArray[2] = new Item("Food", 2, LocalDate.now().plusDays(21), "Cici");
    foodItemArray[3] = new Item("Food-Seafood", 1, LocalDate.now().plusDays(3), "Robert");
    foodItemArray[4] = new Item("Food-Seafood", 199, LocalDate.now().plusDays(3), "Amy");
    Map<String, Item> items = new HashMap<>();
    for (Item foodItem : foodItemArray) {
      items.put(foodItem.getItemId(), foodItem);
    }

    Item[] hygieneItemArray = new Item[5];
    hygieneItemArray[0] = new Item("Hygiene", 75, LocalDate.now().plusDays(180), "Charlie");
    hygieneItemArray[1] = new Item("Hygiene", 120, LocalDate.now().plusDays(150), "Ethan");
    hygieneItemArray[2] = new Item("Hygiene", 60, LocalDate.now().plusDays(100), "Charlotte");
    hygieneItemArray[3] = new Item("Hygiene", 30, LocalDate.now().plusDays(60), "Benjamin");
    hygieneItemArray[4] = new Item("Hygiene", 90, LocalDate.now().plusDays(90), "Robert");
    for (Item hygieneItem : hygieneItemArray) {
      items.put(hygieneItem.getItemId(), hygieneItem);
    }

    Item[] clothingItemArray = new Item[5];
    clothingItemArray[0] = new Item("Clothing", 5, LocalDate.now().plusDays(180), "Charlie");
    clothingItemArray[1] = new Item("Clothing", 4, LocalDate.now().plusDays(180), "Olivia");
    clothingItemArray[2] = new Item("Clothing", 2, LocalDate.now().plusDays(180), "Emma");
    clothingItemArray[3] = new Item("Clothing", 10, LocalDate.now().plusDays(360), "Amy");
    clothingItemArray[4] = new Item("Clothing", 8, LocalDate.now().plusDays(360), "Mason");
    for (Item clothingItem : clothingItemArray) {
      items.put(clothingItem.getItemId(), clothingItem);
    }

    Item[] medicineItemArray = new Item[5];
    medicineItemArray[0] = new Item("Medicine", 10, LocalDate.now().plusDays(60), "John");
    medicineItemArray[1] = new Item("Medicine", 20, LocalDate.now().plusDays(45), "Emma");
    medicineItemArray[2] = new Item("Medicine", 15, LocalDate.now().plusDays(90), "Lucas");
    medicineItemArray[3] = new Item("Medicine", 5, LocalDate.now().plusDays(120), "Isabella");
    medicineItemArray[4] = new Item("Medicine", 25, LocalDate.now().plusDays(30), "Sophia");
    for (Item medicineItem : medicineItemArray) {
      items.put(medicineItem.getItemId(), medicineItem);
    }

    Item[] drinkItemArray = new Item[5];
    drinkItemArray[0] = new Item("Drink", 50, LocalDate.now().plusDays(14), "Michael");
    drinkItemArray[1] = new Item("Drink", 100, LocalDate.now().plusDays(10), "Sarah");
    drinkItemArray[2] = new Item("Drink", 75, LocalDate.now().plusDays(7), "David");
    drinkItemArray[3] = new Item("Drink", 25, LocalDate.now().plusDays(5), "Amy");
    drinkItemArray[4] = new Item("Drink", 60, LocalDate.now().plusDays(30), "Amy");
    for (Item drinkItem : drinkItemArray) {
      items.put(drinkItem.getItemId(), drinkItem);
    }

    for (Map.Entry<String, Item> entry : items.entrySet()) {
      database.addItem(entry.getValue(), defaultResourceId);
    }

    Request[] requestArray = new Request[5];
    requestArray[0] = new Request("REQ1", Arrays.asList(foodItemArray[0].getItemId()),
        Arrays.asList(2), "Pending", "High", "John Doe");
    requestArray[1] = new Request("REQ2", Arrays.asList(drinkItemArray[1].getItemId()),
        Arrays.asList(1), "Pending", "Low", "Alice Doe");
    requestArray[2] = new Request("REQ3", Arrays.asList(medicineItemArray[1].getItemId()),
        Arrays.asList(5), "Pending", "Medium", "John Doe");
    requestArray[3] = new Request("REQ4", Arrays.asList(clothingItemArray[2].getItemId()),
        Arrays.asList(6), "Pending", "High", "Jane Doe");
    requestArray[4] = new Request("REQ5",
        Arrays.asList(hygieneItemArray[3].getItemId(), clothingItemArray[2].getItemId()),
        Arrays.asList(7, 2), "Pending", "High", "Sara Doe");

    for (Request request : requestArray) {
      database.addRequest(request, defaultResourceId);
    }
    List<Request> requests = new ArrayList<>(Arrays.asList(requestArray));
    Resource resource = new Resource(items, defaultResourceId);
    return Pair.of(requests, resource);
  }

  /**
   * Overrides the default resource, used when testing.
   *
   * @param resourceId A {@code String} test resource ID.
   */
  public void overriedDefaultResourceId(String resourceId) {
    this.defaultResourceId = resourceId;
  }

  /**
   * This contains all the overheading teardown logic, it will save all the created user data to a
   * file, so it will be ready for the next setup.
   */
  @PreDestroy
  public void onTermination() {
    System.out.println("Termination");
  }

  private DatabaseService database;
  private String defaultResourceId;
}
