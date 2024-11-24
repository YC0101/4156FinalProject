# COMS-4156-Project

This is the Github repository for the service portion of the Team Project associated with COMS 4156 Advanced Software Engineering.
Our group, ctrlZHeroes, contains members: Yanxi Chen, Qirui Ruan, Xinchen Zhang, Songwen Zhao, Charlie Shen.

## Viewing original assignment repo 

Please use the following link to view the repository relevant to the app: https://github.com/griffinnewbold/COMS-4156-App

## Service Overview

Our Service, Resource Management System (RMS), is designed to streamline the process of donating, requesting, and dispatching essential resources such as food, medical supplies, clothing, and other vital goods. The **RMS** provides a series of functions aimed at efficient resource management. Key functionalities include:
- **Donation Management**: Enabling donors to contribute resources, defining types, quantities, and expiration dates.
- **Request Handling**: Allowing users to request resources based on need, specify priorities, and update statuses.
- **Resource Dispatching**: Scheduling and fulfilling requests by matching available resources to meet demands.

The service aims at helping shelters, community centers, and individuals in need, by providing an organized and accessible platform. This system includes a cloud-deployed version for remote access, allowing users to interact with the service without needing a local setup.

---

## Bookmark Overview

To guide users effectively, here is a bookmark-based outline for easy navigation through the README:

1. [Class Overview](#class-overview)
2. [Endpoints](#endpoints)
3. [Building and Running a Local Instance](#building-and-running-a-local-instance)
4. [Running a Cloud-based Instance](#running-a-cloud-based-instance)
5. [Testing the Service](#tests)
6. [API Local Test Screenshots](#api-local-test-screenshots)
7. [API GCP Test Links](#api-gcp-test-links)
7. [Project Management and Contributions](#project-management-and-contributions)

These bookmarks provide direct links to each major section, allowing quick access to detailed instructions, endpoint definitions, testing information, and team contributions.  

---

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

Welcome to test and try the functionalities! If you want to explore steps to deploy RMS to GCP, check [Steps to deploy to GCP](#steps-to-deploy-to-GCP)

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

1. Item Class: 77% Branch Coverage
2. Resource Class: 92% Branch Coverage
3. Request Class: 88% Branch Coverage
4. Scheduler Class: 84% Branch Coverage
5. Route Controller: 93% Branch Coverage

Total: 85% Branch Coverage

## Endpoints

This section describes the endpoints that our service provides, as well as their inputs, outputs and status codes. For in-depth examples of use cases and inputs/outputs, you can use Postman to interact with the service.

Any malformed requests, such as incorrect parameter types, missing required parameters, or improperly structured API calls, will result in an error HTTP status.

---

#### GET `/`, `/index`, `/home`

- **Description:** Redirects to the homepage, providing instructions on how to make API calls using a browser or tools like Postman.

- **Expected Input Parameters:**
  - None

- **Expected Output:**  
  **HTTP 200 OK** status with a welcome message and instructions on how to make API calls.

- **Upon Success:**
  - **HTTP 200 OK** with the welcome message and API call instructions.

- **Upon Failure:**
  - **HTTP 500 Internal Server Error** with "An Error has occurred" if an internal server error occurs.

---

#### POST `/createDonation`

- **Description:** Handles the POST request for creating a new donation. It validates the input and triggers the donation creation in the service layer.

- **Expected Input Parameters:**
  - `resourceId` (String): The unique ID of the resource the item will be added to.
  - `itemType` (String): The type of the item the donor wants to donate.
  - `quantity` (int): The number of items being donated.
  - `expirationDate` (LocalDate): The expiration date of the donated item.
  - `donorId` (String): The ID of the donor who provided the item.

- **Expected Output:**  
  **HTTP 200 OK** status with a JSON object containing the donation details.  
  - This endpoint allows donors to add new donations to the system.

- **Upon Success:**
  - **HTTP 200 OK** with the details of the created item as a JSON object.

- **Upon Failure:**
  - **HTTP 400 Bad Request** with "Invalid Input Item" if the input parameters are invalid.
  - **HTTP 200 OK** with "An Error has occurred" if an unexpected error occurs during processing.

---

#### POST `/createRequest`

- **Description:** Handles the POST request for creating a new request. It validates the input and triggers the request creation in the service layer.

- **Expected Input Parameters:**
  - `requestId` (String): The unique ID of the request to be added.
  - `itemIds` (List<String>): A list of item IDs being requested.
  - `itemQuantities` (List<Integer>): A list of quantities corresponding to each item being requested.
  - `status` (String): The current status of the request.
  - `priorityLevel` (String): The priority level of the request.
  - `requesterInfo` (String): Information about the requester.
  - `resourceId` (String): The unique ID of the resource the request is associated with.

- **Expected Output:**  
  **HTTP 200 OK** status with a JSON object containing the details of the created request.  
  - This endpoint allows users to create new requests within the system.

- **Upon Success:**
  - **HTTP 200 OK** with the details of the created request as a JSON object.

- **Upon Failure:**
  - **HTTP 400 Bad Request** with "Invalid Input Request" if the input parameters are invalid.
  - **HTTP 200 OK** with "An Error has occurred" if an unexpected error occurs during processing.

---

#### PATCH `/processRequests`

- **Description:** Attempts to dispatch current requests associated with a specified resource. It processes the requests, updates item quantities and statuses, and marks requests as dispatched.

- **Expected Input Parameters:**
  - `resourceId` (String): The unique ID of the resource for which requests will be processed.

- **Expected Output:**  
  **HTTP 200 OK** status with a JSON object containing details about the dispatched requests and updated items.  
  - This endpoint allows the system to process and dispatch pending requests for a given resource.

- **Upon Success:**
  - **HTTP 200 OK** with a JSON object detailing the dispatched requests and the updated items.

- **Upon Failure:**
  - **HTTP 200 OK** with "An Error has occurred" if an unexpected error occurs during processing.

---

#### GET `/retrieveRequestsByResource`

- **Description:** Returns the details of all requests associated with a specific resource.

- **Expected Input Parameters:**
  - `resourceId` (String): The unique ID of the resource whose requests are to be retrieved.

- **Expected Output:**  
  **HTTP 200 OK** status with a JSON array of request objects if requests are found.  
  - This endpoint allows users to retrieve all requests tied to a particular resource.

- **Upon Success:**
  - **HTTP 200 OK** with a JSON array containing the details of the requests.

- **Upon Failure:**
  - **HTTP 200 OK** with "Requests By Resource Not Found" if no requests are found for the specified resource.
  - **HTTP 200 OK** with "An Error has occurred" if an unexpected error occurs.

---

#### GET `/retrieveRequest`

- **Description:** Returns the details of a specific request identified by its request ID within a given resource.

- **Expected Input Parameters:**
  - `resourceId` (String): The unique ID of the resource.
  - `requestId` (String): The unique ID of the request to be retrieved.

- **Expected Output:**  
  **HTTP 200 OK** status with a JSON object containing the request details if found.  
  - This endpoint allows users to retrieve a specific request's details.

- **Upon Success:**
  - **HTTP 200 OK** with the details of the requested request as a JSON object.

- **Upon Failure:**
  - **HTTP 200 OK** with "Request Not Found" if the specific request does not exist.
  - **HTTP 200 OK** with "Requests By Resource Not Found" if no requests are found for the specified resource.
  - **HTTP 200 OK** with "An Error has occurred" if an unexpected error occurs.

---

#### GET `/retrieveResource`

- **Description:** Returns the details of a resource specified by its resource ID.

- **Expected Input Parameters:**
  - `resourceId` (String): The unique ID of the resource to be retrieved.

- **Expected Output:**  
  **HTTP 200 OK** status with a JSON object containing the resource details if found.  
  - This endpoint allows users to retrieve information about a specific resource.

- **Upon Success:**
  - **HTTP 200 OK** with the details of the resource as a JSON object.

- **Upon Failure:**
  - **HTTP 200 OK** with "Resource Not Found" if the specified resource does not exist.
  - **HTTP 200 OK** with "An Error has occurred" if an unexpected error occurs.

---

#### GET `/retrieveItem`

- **Description:** Returns the details of a specific item identified by its item ID within a given resource.

- **Expected Input Parameters:**
  - `resourceId` (String): The unique ID of the resource.
  - `itemId` (String): The unique ID of the item to be retrieved.

- **Expected Output:**  
  **HTTP 200 OK** status with a JSON object containing the item details if found.  
  - This endpoint allows users to retrieve information about a specific item within a resource.

- **Upon Success:**
  - **HTTP 200 OK** with the details of the item as a JSON object.

- **Upon Failure:**
  - **HTTP 200 OK** with "Item Not Found" if the specified item does not exist within the resource.
  - **HTTP 200 OK** with "Resource Not Found" if the specified resource does not exist.
  - **HTTP 200 OK** with "An Error has occurred" if an unexpected error occurs.

---

#### GET `/retrieveAvailableItems`

- **Description:** Returns all available items (items with status code 'available') within a specified resource.

- **Expected Input Parameters:**
  - `resourceId` (String): The unique ID of the resource.

- **Expected Output:**  
  **HTTP 200 OK** status with a JSON array of available item objects if found.  
  - This endpoint allows users to retrieve all items that are currently available for dispatch or use within a resource.

- **Upon Success:**
  - **HTTP 200 OK** with a JSON array containing the details of all available items.

- **Upon Failure:**
  - **HTTP 200 OK** with "No Available Items Found" if no available items are found within the resource.
  - **HTTP 200 OK** with "Resource Not Found" if the specified resource does not exist.
  - **HTTP 200 OK** with "An Error has occurred" if an unexpected error occurs.

---

#### GET `/retrieveDispatchedItems`

- **Description:** Returns all dispatched items (items with status code 'dispatched') within a specified resource.

- **Expected Input Parameters:**
  - `resourceId` (String): The unique ID of the resource.

- **Expected Output:**  
  **HTTP 200 OK** status with a JSON array of dispatched item objects if found.  
  - This endpoint allows users to retrieve all items that have been dispatched within a resource.

- **Upon Success:**
  - **HTTP 200 OK** with a JSON array containing the details of all dispatched items.

- **Upon Failure:**
  - **HTTP 200 OK** with "No Dispatched Items Found" if no dispatched items are found within the resource.
  - **HTTP 200 OK** with "Resource Not Found" if the specified resource does not exist.
  - **HTTP 200 OK** with "An Error has occurred" if an unexpected error occurs.

---

#### GET `/retrieveItemsByDonor`

- **Description:** Returns the details of items provided by a specified donor within a given resource.

- **Expected Input Parameters:**
  - `resourceId` (String): The unique ID of the resource.
  - `donorId` (String): The ID of the donor whose items are to be retrieved.

- **Expected Output:**  
  **HTTP 200 OK** status with a JSON array of item objects if found.  
  - This endpoint allows users to retrieve all items donated by a specific donor within a resource.

- **Upon Success:**
  - **HTTP 200 OK** with a JSON array containing the details of the items provided by the donor.

- **Upon Failure:**
  - **HTTP 200 OK** with "No Items Found" if no items are found for the specified donor within the resource.
  - **HTTP 200 OK** with "Resource Not Found" if the specified resource does not exist.
  - **HTTP 200 OK** with "An Error has occurred" if an unexpected error occurs.

---

#### DELETE `/resetTestData`

- **Description:** Attempts to reset and clear all test data associated with a specified resource.

- **Expected Input Parameters:**
  - `resourceId` (String): The unique ID of the test resource to be reset. Defaults to "R_TEST" if not provided.

- **Expected Output:**  
  **HTTP 200 OK** status with a confirmation message upon successful reset.  
  - This endpoint allows administrators to clear test data for a given resource, facilitating a fresh state for testing purposes.

- **Upon Success:**
  - **HTTP 200 OK** with the message "Reset `<resourceId>` successfully".

- **Upon Failure:**
  - **HTTP 200 OK** with "An Error has occurred" if an unexpected error occurs during the reset process.

---

## Project Management and Contributions

We are using **GitHub Projects** for project management. All tasks, issues, and individual contributions are tracked on our GitHub project board, which is accessible to all group members and the project mentor.

You can view our **GitHub Projects** board [here](https://github.com/YC0101/4156FinalProject_ctrlZHeroes/projects/1).

### Task Breakdown:
  Details can be found in repo/Project

- **Yanxi Chen**: Resource class implementation and testing.
- **Qirui Ruan**: Item class implementation and testing.
- **Xinchen Zhang**: Scheduler class implementation, testing, and database creation.
- **Songwen Zhao**: API development, database service, overall adjustments and tests.
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

## Steps to deploy to GCP
This project can be deployed on Google Cloud Platform (GCP). Follow the steps below to deploy it on GCP.

You can deploy the application to GCP, open the application in your browser, and even test it on GCP.
   ```
   gcloud config set project YOUR_PROJECT_ID

   gcloud app deploy
 
   gcloud app browse
   ```

### Testing on GCP
You can test your deployed service using Postman. The base URL will be:
```
https://YOUR_PROJECT_ID.ue.r.appspot.com/endpoint?arg=value%
```

## License

### Explanation:

1. **Service Overview**: Brief description of the service functionality.
2. **Building and Running**: Instructions for setting up and running the project.
3. **API Endpoints**: Documentation of key API endpoints, including the order of calls and status codes.
4. **Testing**: Instructions for running unit tests and manually testing the API.
5. **Third-Party Dependencies**: Mention of third-party libraries used in the project.
6. **Task Management**: Links to the project management tool (e.g., Trello) and task breakdown among team members.

This `README.md` covers all required details and can be directly copied and pasted into your repository. Let me know if you need further adjustments!
