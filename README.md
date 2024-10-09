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
4. After you clone this project, go to /IndividualProject and run ```mvn spring-boot:run -Dspring-boot.run.arguments="setup"``` for MacOS. Leave it running until you see “System Setup” in the terminal then terminate the program.
5. After initializes the database in step 4, you may run the project using ```mvn spring-boot:run```
6. If you wish to check the style, run ```mvn checkstyle:check```; to see coverage report, run ```mvn jacoco:report``` and open the coverage report in the target/site/jacoco.

For detailed info about endpoints, chechk the "Endpoints" section.

## Class Overview
**Item Class**
1. Attributes
    - itemId: Unique identifier for the item.
    - itemType: Type of the item (e.g., "Food", "Clothing").
    - quantity: Quantity of the item.
    - expirationDate: Expiration date of the item (if applicable).
    - status: Status of the item ("available", "dispatched").
    - donorId: Identifier of the donor who provided the item.
2. Methods  
    - markAsDispatched(): Updates the status of the item to "dispatched".
    - generateUniqueItemId(): Static method to generate a unique item ID.     
**Resource Class**

**Request Class**

**Scheduler Class**

**Controller (API)**