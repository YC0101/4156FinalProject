# COMS-4156-Project

This is the Github repository for the service portion of the Team Project associated with COMS 4156 Advanced Software Engineering.
Our group, ctrlZHeroes, contains members: Yanxi Chen, Qirui Ruan, Xinchen Zhang, Songwen Zhao, Charlie Shen.

## Viewing original assignment repo 

Please use the following link to view the repository relevant to the app: https://github.com/griffinnewbold/COMS-4156-App

## Service Overview

Our Service, Resource Management System (RMS), is designed to streamline the process of donating, requesting, and dispatching essential resources such as food, medical supplies, clothing, and other vital goods. The service aims at helping shelters, community centers, and individuals in need, by providing an organized and accessible platform.

## Building and Running a Local Instance

Before using this service, please follow the steps below to set up environment (This guide assumes a MacOS environment, the Maven README provides instructions compatible with both Windows and Mac):

1. Maven 3.9.5: https://maven.apache.org/download.cgi Download and follow the installation instructions.
2. JDK 17: This project used JDK 17 for development https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html 
3. IntelliJ IDE: https://www.jetbrains.com/idea/ Feel free to use any other IDE that you feel comfortable with.
4. After you clone this project, go to /FinalProject and run ```mvn spring-boot:run -Dspring-boot.run.arguments="setup"``` for MacOS. Leave it running until you see “System Setup” in the terminal then terminate the program.
5. After initializes the database in step 4, you may run the project using ```mvn spring-boot:run```
6. If you wish to check the style, run ```mvn checkstyle:check```; to see coverage report, run ```mvn jacoco:report``` and open the coverage report in the target/site/jacoco.

## Steps to Build and Run the Service Locally

1. Clone this repository to your local machine:
   ```git clone https://github.com/YC0101/4156FinalProject_ctrlZHeroes.git```
2. Navigate to the FinalProject directory:
   ```cd 4156FinalProject_ctrlZHeroes/FinalProject```
3. Set up the initial database by running:
   ```mvn spring-boot:run -Dspring-boot.run.arguments="setup"```
4. After the database has been initialized, start the service:
   ```mvn spring-boot:run```   
5. Once the application is running, you can access it via a web browser or Postman at `localhost:8080`.
6. To check for style issues, run:
   ```mvn checkstyle:check```
7. To generate a coverage report, run:
   ```mvn jacoco:report```
   Open the coverage report in ```target/site/jacoco```.

For detailed info about endpoints, check the "Endpoints" section.

## Running a Cloud-based Instance 

This service is also deployed to the cloud leveraging the Google Cloud Platform (GCP). You can conveniently access our service without the need for a local setup through https://ase-team.ue.r.appspot.com/! A welcome message will be displayed upon accessing the URL if the cloud service is functioning correctly. When using Postman for testing, direct your requests to the following format `https://ase-team.ue.r.appspot.com/endpoint?arg=value`. This configuration enables you to fully utilize our service.

Welcome to test and try the functionalities!

## Class Overview

**Item Class**

1. Attributes:

- `itemId` (String): Unique identifier for the item (e.g., "item123").
- `itemType` (String): The type of the item being managed (e.g., "Food", "Clothing").
- `quantity` (int): The quantity of the item available (e.g., 10).
- `expirationDate` (LocalDate): The expiration date of the item, if applicable (e.g., "2024-12-31").
- `status` (String): The current status of the item ("available", "dispatched").
- `donorId` (String): The identifier of the donor who provided the item (e.g., "Donor123").

2. Methods:

- `markAsDispatched()`: Updates the status of the item to `"dispatched"`. This method is used when the item is ready to be sent to a requester.
- `generateUniqueItemId()`: A static method that generates a unique item ID based on UUID to ensure all items are uniquely identified.
- `validateAttributes()`: Ensures that the item has valid attributes, such as a positive quantity and a valid expiration date (if applicable).
- `setQuantity(int quantity)`: Sets the quantity of the item to the given value.
- `setExpirationDate(LocalDate expirationDate)`: Updates the expiration date of the item.
- `setStatus(String status)`: Sets the current status of the item, such as `"available"` or `"dispatched"`.

**Resource Class**

1. Attributes:

- `items` (HashMap<String, Item>): A map that stores all `Item` objects using `itemId` as the key.
- `resourceId` (String): The unique identifier for the resource (e.g., a donation center or a shelter).

2. Methods:

- `addItem(String itemId, Item item)`: Adds an `Item` to the resource repository, with the given `itemId`.
- `getAllItems()`: Returns a map of all stored items, where each key is an `itemId` and each value is an `Item` object.
- `removeItem(String itemId)`: Removes an item from the repository using its `itemId` and returns the removed `Item`.
- `getItemById(String itemId)`: Retrieves and returns an `Item` from the repository by its `itemId`.
- `listAvailableItems()`: Returns a string containing all items that have the status `"available"`, formatted as a list.
- `toString()`: Provides a string representation of the resource, including its `resourceId` and the associated items.

3. Additional Notes:

- **Serialization**: The `Resource` class implements `Serializable` to allow for object serialization, which is useful for saving resource data to files or databases.
- **Item Management**: This class allows managing multiple items by providing methods to add, retrieve, remove, and list items.

**Request Class**

1. Attributes:

- `requestId` (String): Unique identifier for the request (e.g., "req123").
- `itemIds` (List<String>): List of IDs for the items being requested.
- `status` (String): The current status of the request (e.g., "requested", "fulfilled", "dispatched").
- `priorityLevel` (String): The priority level of the request (e.g., "High", "Medium", "Low").
- `requesterInfo` (String): Information about the person or entity who made the request (e.g., "NGO123", "Hospital001").

2. Methods:

- `getRequestId()`: Returns the unique identifier of the request.
- `getItemIds()`: Returns the list of item IDs involved in the request.
- `getStatus()`: Returns the current status of the request.
- `getPriorityLevel()`: Returns the priority level of the request.
- `getRequesterInfo()`: Provides information about the requester.
- `updateStatus(String newStatus)`: Updates the status of the request (e.g., from "requested" to "fulfilled").
- `updatePriority(String newPriority)`: Adjusts the priority level of the request (e.g., from "Medium" to "High").

**Scheduler Class**

1. Attributes:

- `requests`: A list of Request objects that are being processed by the scheduler. These requests contain information about the resources needed and the requester.
- `resourceRepository`: A map that represents the available resources in the system. The keys are resource IDs, and the values are the quantities available for each resource.

2. Methods:

- `processRequests()`: Processes incoming requests by checking the availability of requested resources in the Resource repository. It prioritizes requests based on urgency and schedules dispatches.
- `checkResourceAvailability(Request request)`: Checks if the requested resource is available in the repository. If the resource is available, the request can proceed to scheduling.
- `scheduleDispatch(Request request)`: Schedules the dispatch of available resources for a specific request. The method assigns volunteers or delivery agents to handle the dispatch logistics.

**Controller (API)**

1. Attributes: 

- The controller does not typically manage data directly but acts as the intermediary between the service and its clients.

2. Methods:

- `index()`: Redirects to the homepage with instructions on how to make API calls.
- `createDonation(String resourceId, String itemType, int quantity, LocalDate expirationDate, String donorId)`: Handles the POST request for creating a new donation.
- `createRequest(String requestId, List<String> itemIds, String status, String priorityLevel, String requesterInfo)`: Handles the POST request for creating a new request.
- `processRequests(String resourceId)`: Attempts to dispatch current requests with specified resource.
- `retrieveResource(String resourceId)`: Returns the details of the resource specified by the resource ID.
- `retrieveItem(String resourceId, String itemId)`: Returns the details of the item specified by the item ID.
- `retrieveAvailableItems(String resourceId)`: Returns all available items, that is, items with status code 'available'.
- `retrieveDispatchedItems(String resourceId)`: Returns all dispatched items, that is, items with status code 'dispatched'.
- `retrieveItemsByDonor(String resourceId, String donorId)`: Returns the details of the items provided by a specified donor.

## Tests

### Unit Tests

1. Item Class: 100% Branch Coverage
2. Resource Class: 100% Branch Coverage
3. Scheduler Class: 88% Branch Coverage
4. Route Controller: 100% Branch Coverage

## Endpoints

This section describes the endpoints that our service provides, as well as their inputs, outputs and status codes. For in-depth examples of use cases and inputs/outputs, you can use Postman to interact with the service.

Any malformed requests, such as incorrect parameter types, missing required parameters, or improperly structured API calls, will result in an error HTTP status.

---

#### GET `/`, `/index`, `/home`

- **Description:**  Redirects to the homepage, providing instructions on how to make API calls using a browser or tools like Postman.
- **Expected Input Parameters:**
  - None
- **Upon Success:**
  - HTTP 200 OK with a welcome message with instructions on how to make API calls.
- **Upon Failure:**
  - HTTP 500 Internal Server Error with "An Error has occurred" if an internal server error occurs.

---

#### POST `/createDonation`

- **Description:**  Handles the POST request for creating a new donation. It validates the input and triggers the donation creation in the service layer.

- **Expected Input Parameters:**
  - `resourceId` (String): The unique ID of the resource the item will be added to.
  - `itemType` (String): The type of the item the donor wants to donate.
  - `quantity` (int): The number of items being donated.
  - `expirationDate` (LocalDate): The expiration date of the donated item.
  - `donorId` (String): The ID of the donor who provided the item.

- **Expected Output:**  
  HTTP 200 OK status with a JSON object containing the donation details.
  - This endpoint allows donors to add new donations to the system.

- **Upon Success:**
  - HTTP 200 OK with the details of the created item as a string.

- **Upon Failure:**
  - HTTP 400 Bad Request with "Invalid Input Item" if the input parameters are invalid.
  - HTTP 200 OK with "An Error has occurred" if an unexpected error occurs during processing.

---

#### POST `/createRequest`

- **Description:**  Handles the POST request for creating a new request by adding it to the scheduler.
- **Expected Input Parameters:**
  - `requestId` (String): The unique ID of the request to be added.
  - `itemIds` (List<String>): A list of item IDs being requested.
  - `status` (String): The current status of the request.
  - `priorityLevel` (String): The priority level of the request.
  - `requesterInfo` (String): Information about the requester.
- **Upon Success:**
  - HTTP 200 OK with the unique ID of the created request.
- **Upon Failure:**
  - HTTP 200 OK with "An Error has occurred" if an unexpected error occurs during processing.

---

#### PATCH `/processRequests`

- **Description:**  Attempts to dispatch current requests using the specified resource.
- **Expected Input Parameters:**
  - `resourceId` (String): The unique ID of the resource to use for dispatching.
- **Upon Success:**
  - HTTP 200 OK with information about successfully dispatched requests.
- **Upon Failure:**
  - HTTP 200 OK with "An Error has occurred" if an unexpected error occurs during processing.

---

#### GET `/retrieveResource`

- **Description:**  Retrieves the details of a specific resource by its unique ID.
- **Expected Input Parameters:**
  - `resourceId` (String): The unique ID of the resource to retrieve.
- **Upon Success:**
  - HTTP 200 OK with the details of all items of the specified resource as a string.
- **Upon Failure:**
  - HTTP 200 OK with "An Error has occurred" if an unexpected error occurs during processing.

---

#### GET `/retrieveItem`

- **Description:**  Retrieves the details of a specific item within a resource by its unique ID.
- **Expected Input Parameters:**
  - `resourceId` (String): The unique ID of the resource containing the item.
  - `itemId` (String): The unique ID of the item to retrieve.
- **Upon Success:**
  - HTTP 200 OK with the details of the specified item as a string.
- **Upon Failure:**
  - **HTTP 404 Not Found**  
    Returns "Item Not Found" if the specified item does not exist.
  - **HTTP 200 OK**  
    Returns "An Error has occurred" if an unexpected error occurs during processing.

---

#### GET `/retrieveAvailableItems`

- **Description:**  Retrieves all available items (status code 'available') within a specified resource.
- **Expected Input Parameters:**
  - `resourceId` (String): The unique ID of the resource to search within.
- **Upon Success:**
  - HTTP 200 OK with a list of available items in plain text format.
- **Upon Failure:**
  - HTTP 404 Not Found with "No Available Items Found" if there are no available items in the specified resource.
  - HTTP 200 OK with "An Error has occurred" if an unexpected error occurs during processing.

---

#### GET `/retrieveDispatchedItems`

- **Description:**  Retrieves all dispatched items (status code 'dispatched') within a specified resource.
- **Expected Input Parameters:**
  - `resourceId` (String): The unique ID of the resource to search within.
- **Upon Success:**
  - HTTP 200 OK with a list of dispatched items in plain text format.
- **Upon Failure:**
  - **HTTP 404 Not Found**  
    Returns "No Dispatched Items Found" if there are no dispatched items in the specified resource.
  - **HTTP 200 OK**  
    Returns "An Error has occurred" if an unexpected error occurs during processing.

---

#### GET `/retrieveItemsByDonor`

- **Description:**  Retrieves all items provided by a specified donor within a given resource.
- **Expected Input Parameters:**
  - `resourceId` (String): The unique ID of the resource to search within.
  - `donorId` (String): The ID of the donor whose items are to be retrieved.
- **Upon Success:**
  - **HTTP 200 OK**  
    Returns a list of items donated by the specified donor in plain text format.
- **Upon Failure:**
  - **HTTP 404 Not Found**  
    Returns "No Items Found" if there are no items donated by the specified donor in the resource.
  - **HTTP 200 OK**  
    Returns "An Error has occurred" if an unexpected error occurs during processing.

## Project Management and Contributions

We are using **GitHub Projects** for project management. All tasks, issues, and individual contributions are tracked on our GitHub project board, which is accessible to all group members and the project mentor.

You can view our **GitHub Projects** board [here](https://github.com/YC0101/4156FinalProject_ctrlZHeroes/projects/1).

### Task Breakdown:
  Details can be found in repo/Project

- **Yanxi Chen**: Resource class implementation and testing.
- **Qirui Ruan**: Item class implementation and testing.
- **Xinchen Zhang**: Scheduler class implementation and testing.
- **Songwen Zhao**: API development and request handling.
- **Charlie Shen**: Request class implementation and testing.

The following tasks are tracked via the GitHub project board:

- **Backlog**: Tasks that haven't been started, such as style checking and revising the README.
- **Ready**: Tasks ready to be picked up, such as writing tests for the request and scheduler classes.
- **In Progress**: Tasks actively being worked on, such as designing the request and scheduler classes.
- **In Review**: Tasks under review, such as writing tests for the resource class.
- **Done**: Completed tasks, including designing the architecture, resource class, item class, and writing tests for the item class.

All team members contribute to updating the board regularly to reflect the current status of tasks.

## API Local Test Screenshots:

   Below are each API local test in Postman:
   ### home
    ![Screenshots of API.](/screenshots/api1.png)
   ### createDonation
    ![Screenshots of API.](/screenshots/api2.png)
   ### createRequest
    ![Screenshots of API.](/screenshots/api3.png)
   ### processRequests
    ![Screenshots of API.](/screenshots/api4.png)
   ### retrieveResource
    ![Screenshots of API.](/screenshots/api5.png)
   ### retrieveItem
    ![Screenshots of API.](/screenshots/api6.png)
   ### retrieveAvailableItems
    ![Screenshots of API.](/screenshots/api7.png)
   ### retrieveDispatchedItems
    ![Screenshots of API.](/screenshots/api8.png)
   ### retrieveItemsByDonor
    ![Screenshots of API.](/screenshots/api9.png)

## API GCP Test Links:
    Default/Home: https://ase-team.ue.r.appspot.com/
    createDonation: https://ase-team.ue.r.appspot.com/createDonation?resourceId=R_COLUMBIA&itemType=Food&quantity=3&expirationDate=2025-03-11&donorId=Robert
    createRequest: https://ase-team.ue.r.appspot.com/createRequest?requestId=testRequest&itemIds=fake&status=Pending&priorityLevel=High&requesterInfo=John Doe
    processRequest: https://ase-team.ue.r.appspot.com/processRequests?resourceId=R_COLUMBIA
    retrieveResource: https://ase-team.ue.r.appspot.com/retrieveResource?resourceId=R_COLUMBIA
    retrieveItem: https://ase-team.ue.r.appspot.com/retrieveItem?resourceId=R_COLUMBIA&itemId=90bebb7d-d1a1-4a7c-898b-9cac2530fb6b
    retrieveAvailableItems: https://ase-team.ue.r.appspot.com/retrieveAvailableItems?resourceId=R_COLUMBIA 
    retrieveDispatchedItems: https://ase-team.ue.r.appspot.com/retrieveDispatchedItems?resourceId=R_COLUMBIA 
    retrieveItemsByDonor: https://ase-team.ue.r.appspot.com/retrieveItemsByDonor?resourceId=R_COLUMBIA&donorId=Robert 

## License

### Explanation:

1. **Service Overview**: Brief description of the service functionality.
2. **Building and Running**: Instructions for setting up and running the project.
3. **API Endpoints**: Documentation of key API endpoints, including the order of calls and status codes.
4. **Testing**: Instructions for running unit tests and manually testing the API.
5. **Third-Party Dependencies**: Mention of third-party libraries used in the project.
6. **Task Management**: Links to the project management tool (e.g., Trello) and task breakdown among team members.

This `README.md` covers all required details and can be directly copied and pasted into your repository. Let me know if you need further adjustments!
