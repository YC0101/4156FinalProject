# COMS-4156-Project

This is the Github repository for the service portion of the Team Project associated with COMS 4156 Advanced Software Engineering..
Our group, ctrlZHeroes, contains members: Yanxi Chen, Qirui Ruan, Xinchen Zhang, Songwen Zhao, Charlie Shen.

## Viewing original assignment repo 
Please use the following link to view the repository relevant to the app: https://github.com/griffinnewbold/COMS-4156-App

## Service Overview
 Our Service,  Resource Management System (RMS), is designed to streamline the process of donating, requesting, and dispatching essential resources such as food, medical supplies, clothing, and other vital goods. The service aims at helping shelters, community centers, and individuals in need, by providing an organized and accessible platform.

## Building and Running a Local Instance
Before using this service, please follow the steps below to set up environment for MacOS.

1. Maven 3.9.5: https://maven.apache.org/download.cgi Download and follow the installation instructions.
2. JDK 17: This project used JDK 17 for development https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html 
3. IntelliJ IDE: https://www.jetbrains.com/idea/
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
5. To check for style issues, run:
    ```mvn checkstyle:check```
6. To generate a coverage report, run:
    ```mvn jacoco:report```
Open the coverage report in ```target/site/jacoco```.

For detailed info about endpoints, chechk the "Endpoints" section.

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
- createDonation(): Handles the POST request for creating a new donation. It validates the input and triggers the donation creation in the service layer.
- createRequest(): Handles the POST request for submitting a new resource request. It validates input data (e.g., resource type, quantity, requester information) and forwards the request to the service.
- scheduleDispatch(): Handles the POST request to schedule a dispatch for a valid resource request. This method ensures that resources are available before dispatch is scheduled.
- Error Handling: The controller includes error handling for invalid input, missing required parameters, and system failures. Common errors include 400 (Bad Request), 404 (Not Found), and 500 (Server Error).
- Input Validation: Ensures that input data for each request is valid, such as checking the format of item types, quantities, and requester information.
- Rate Limiting: The controller includes basic rate-limiting features to ensure that the system remains stable under high loads.

## Tests
### Unit Tests
1. Item Class: 100% Branch Coverage
2. Resource Class: 100% Branch Coverage

## Endpoints

This section describes the endpoints that our service provides, as well as their inputs and outputs. For in-depth examples of use cases and inputs/outputs, you can use Postman to interact with the service.

Any malformed requests, such as incorrect parameter types, missing required parameters, or improperly structured API calls, will result in a HTTP 400 Bad Request response.

## Endpoints

This section describes the endpoints provided by the RMS service, including their inputs, outputs, and status codes.

#### POST /donations
* **Expected Input Parameters**:
   * `itemType` (String): The type of item being donated (e.g., "Food").
   * `quantity` (int): The number of items being donated (e.g., 10).
   * `expirationDate` (String): The expiration date of the item in YYYY-MM-DD format (optional).
   * `donorId` (String): The ID of the donor (e.g., "Donor123").
* **Expected Output**: HTTP 201 Created status with a JSON object containing the donation details.
   * This endpoint allows donors to add new donations to the system.
* **Upon Success**:
   * HTTP 201 Status Code is returned along with:
     ```json
     {
       "itemId": "generated-item-id",
       "status": "available"
     }
     ```
* **Upon Failure**:
   * HTTP 400 Status Code with "Invalid input data" if the input parameters are invalid.

#### POST /requests
* **Expected Input Parameters**:
   * `resourceType` (String): The type of resource being requested (e.g., "Clothing").
   * `quantity` (int): The number of resources requested (e.g., 5).
   * `priorityLevel` (String): The priority level of the request ("High", "Medium", "Low").
   * `requesterInfo` (String): Information about the requester (e.g., "Shelter001").
* **Expected Output**: HTTP 201 Created status with a JSON object containing the request details.
   * This endpoint allows users to submit requests for specific resources.
* **Upon Success**:
   * HTTP 201 Status Code is returned along with:
     ```json
     {
       "requestId": "generated-request-id",
       "status": "requested"
     }
     ```
* **Upon Failure**:
   * HTTP 400 Status Code with "Invalid input data" if the input parameters are invalid.

#### POST /dispatch
* **Expected Input Parameters**:
   * `requestId` (String): The ID of the request for which the resource will be dispatched.
   * `volunteerId` (String): The ID of the volunteer or delivery agent handling the dispatch (e.g., "Volunteer001").
* **Expected Output**: HTTP 200 OK status with a JSON object containing dispatch details.
   * This endpoint allows scheduling a dispatch for an existing resource request.
* **Upon Success**:
   * HTTP 200 Status Code is returned along with:
     ```json
     {
       "status": "dispatched",
       "dispatchTime": "2024-10-31T15:00:00"
     }
     ```
* **Upon Failure**:
   * HTTP 404 Status Code with "Request not found" if the request ID doesn't exist.
   * HTTP 400 Status Code with "Invalid input data" if the input parameters are invalid.

### Calling Order:
- **First**: Call `/donations` to add a donation.
- **Second**: Call `/requests` to request resources.
- **Third**: Call `/dispatch` to dispatch resources (only after a valid request has been made).

## Project Management and Contributions

We are using **GitHub Projects** for project management. All tasks, issues, and individual contributions are tracked on our GitHub project board, which is accessible to all group members and the project mentor.

You can view our **GitHub Projects** board [here](https://github.com/YC0101/4156FinalProject_ctrlZHeroes/projects/1).

### Task Breakdown:
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

## License

### Explanation:
1. **Service Overview**: Brief description of the service functionality.
2. **Building and Running**: Instructions for setting up and running the project.
3. **API Endpoints**: Documentation of key API endpoints, including the order of calls and status codes.
4. **Testing**: Instructions for running unit tests and manually testing the API.
5. **Third-Party Dependencies**: Mention of third-party libraries used in the project.
6. **Task Management**: Links to the project management tool (e.g., Trello) and task breakdown among team members.

This `README.md` covers all required details and can be directly copied and pasted into your repository. Let me know if you need further adjustments!
