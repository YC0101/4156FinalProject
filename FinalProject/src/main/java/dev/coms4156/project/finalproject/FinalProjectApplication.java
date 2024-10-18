package dev.coms4156.project.finalproject;

import jakarta.annotation.PreDestroy;
import java.time.LocalDate;
import java.util.HashMap;
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
        myFileDatabase = new MyFileDatabase(false, "./data.txt"); // Reset data
        resetDataFile();
        System.out.println("System Setup");
        return;
      }
    }
    myFileDatabase = new MyFileDatabase(true, "./data.txt");
    System.out.println("Start up");
  }

  /**
   * Overrides the database reference, used when testing.
   *
   * @param testData A {@code MyFileDatabase} object referencing test data.
   */
  public static void overrideDatabase(MyFileDatabase testData) {
    myFileDatabase = testData;
    saveData = false;
  }

  /**
   * Prepare initial data for the database or allows for data to be reset in 
   * event of errors.
   */
  public void resetDataFile() {
    Item[] foodItem = new Item[5];
    foodItem[0] = new Item("Food", 10, LocalDate.now().plusDays(7), "Robert");
    foodItem[1] = new Item("Food", 5, LocalDate.now().plusDays(7), "Fiona");
    foodItem[2] = new Item("Food", 2, LocalDate.now().plusDays(21), "Cici");
    foodItem[3] = new Item("Food-Seafood", 1, LocalDate.now().plusDays(3), "Robert");
    foodItem[4] = new Item("Food-Seafood", 199, LocalDate.now().plusDays(3), "Amy");
    HashMap<String, Item> items = new HashMap<>();
    for (int i = 0; i < foodItem.length; i++) {
      items.put(foodItem[i].getItemId(), foodItem[0]);
    }

    Item[] hygieneItem = new Item[5]; 
    hygieneItem[0] = new Item("Hygiene", 75, LocalDate.now().plusDays(180), "Charlie");
    hygieneItem[1] = new Item("Hygiene", 120, LocalDate.now().plusDays(150), "Ethan");
    hygieneItem[2] = new Item("Hygiene", 60, LocalDate.now().plusDays(100), "Charlotte");
    hygieneItem[3] = new Item("Hygiene", 30, LocalDate.now().plusDays(60), "Benjamin");
    hygieneItem[4] = new Item("Hygiene", 90, LocalDate.now().plusDays(90), "Robert");
    for (int i = 0; i < hygieneItem.length; i++) {
      items.put(hygieneItem[i].getItemId(), hygieneItem[0]);
    }

    Item[] clothingItem = new Item[5]; 
    clothingItem[0] = new Item("Clothing", 5, LocalDate.now().plusDays(180), "Charlie");
    clothingItem[1] = new Item("Clothing", 4, LocalDate.now().plusDays(180), "Olivia");
    clothingItem[2] = new Item("Clothing", 2, LocalDate.now().plusDays(180), "Emma");
    clothingItem[3] = new Item("Clothing", 10, LocalDate.now().plusDays(360), "Amy");
    clothingItem[4] = new Item("Clothing", 8, LocalDate.now().plusDays(360), "Mason");
    for (int i = 0; i < clothingItem.length; i++) {
      items.put(clothingItem[i].getItemId(), clothingItem[0]);
    }

    Item[] medicineItem = new Item[5]; 
    medicineItem[0] = new Item("Medicine", 10, LocalDate.now().plusDays(60), "John");
    medicineItem[1] = new Item("Medicine", 20, LocalDate.now().plusDays(45), "Emma");
    medicineItem[2] = new Item("Medicine", 15, LocalDate.now().plusDays(90), "Lucas");
    medicineItem[3] = new Item("Medicine", 5, LocalDate.now().plusDays(120), "Isabella");
    medicineItem[4] = new Item("Medicine", 25, LocalDate.now().plusDays(30), "Sophia");
    for (int i = 0; i < medicineItem.length; i++) {
      items.put(medicineItem[i].getItemId(), medicineItem[0]);
    }

    Item[] drinkItem = new Item[5];
    drinkItem[0] = new Item("Drink", 50, LocalDate.now().plusDays(14), "Michael");
    drinkItem[1] = new Item("Drink", 100, LocalDate.now().plusDays(10), "Sarah");
    drinkItem[2] = new Item("Drink", 75, LocalDate.now().plusDays(7), "David");
    drinkItem[3] = new Item("Drink", 25, LocalDate.now().plusDays(5), "Amy");
    drinkItem[4] = new Item("Drink", 60, LocalDate.now().plusDays(30), "Amy");
    for (int i = 0; i < drinkItem.length; i++) {
      items.put(drinkItem[i].getItemId(), drinkItem[0]);
    }
    Resource resource1 = new Resource(items, "columbia_resource");
    HashMap<String, Resource> resourceMapping = new HashMap<>();
    resourceMapping.put("columbia_resource", resource1);

    myFileDatabase.setMapping(resourceMapping);
  }


  /**
   * This contains all the overheading teardown logic, it will save all the created 
   * user data to a file, so it will be ready for the next setup.
   */
  @PreDestroy
  public void onTermination() {
    System.out.println("Termination");
    if (saveData) {
      myFileDatabase.saveContentsToFile();
    }
  }

  public static MyFileDatabase myFileDatabase;
  private static boolean saveData = true;
}
