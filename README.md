# Initial Thoughts

From the task it was clear that we are dealing with 3 business objects; **Customer**, **Vehicle**, and **Leasing Contract** therefore my thought was to apply REST API principles on every object, so that every model has it's own API with it's set of operations to allow client to apply CRUD calls.

## Customer
Customer has been modeled in the DB level with it's own table, and it has defined a one-to-many relation with the leasing contract.
What was not clear to me is the deletion of a customer while it has more than one contract. in the UI given in the task (section 1.3.4) there is a delete button next to the customer, I can understand that this might be to unlink this contract from this customer in order to assign another customer to this contract (in case of entry mistake), therefore I have implemented in the contract API a method which allow to unlink this customer.
In real cases deletion operation has strict business requirements, but for the sake of this task, I have provided an API to delete a customer by id.

## Vehicle
Given the specified requirement that a vehicle can be only used in a single contract, might be a solution to integrate vehicle model into the contract model to be as a metadata. But I thought that is better to keep them split, for a good reason that business rule can change, and can allow multiple vehicles to be in the same contract. And if this happen, with this modeling this can be achieved easily on the internal schema and api levels.

Additionally, a vehicle in the scope of current requirements has a strong relation with the contract, as it belongs to only one single contract, therefore, when a contract is deleted, the vehicle is deleted too.

I added some business checks on the vehicle:

- VIN is unique
- VIN can be null when the vehicle created, but once updated with a not null value, it cannot be set back to null, but can have a new value (which is validated to be unique)

## Leasing Contract
Leasing contract creation depends on an existing customer and vehicle objects, therefore, a creation of a contract without these data is not possible (Also as mentioned in the images)

I added some business checks on the leasing contract:

- Contract number should be unique (Is provided by the client)
- Customer and Vehicle should be existing when creating a new contract
- Vehicle should not be used in another contract
- Clients of the update API are allowed to update Customer and Vehicle Ids (old objects are not deleted and should be removed from their API)
- Deletion of a contract will delete also the vehicle entry, but not the customer

# Technical Details
- SpringBoot version 2.7.16
- Java 11
- Gradle
- Flyway for creating schema
- Swagger Open API specification annotations and UI
- MariaDB version 10.11
- JUnit for unit and integration tests
- Lombok annotations
- Hibernate and Spring Data
- Spring Rest Controller

# Running the application
1. Clone the repository
> git clone https://github.com/farishudieb/vehicle-leasing-service
2. Start up the database by running docker-compose command in the service folder
> docker-compose up
3. Create the database schema by executing following gradle command
> ./gradlew flywayMigrate
4. Build the project and execute the tests
> ./gradlew clean build
5. After build succeeds, the jar file will be located in the gradle build directory, the directory is located in the checkedout repository folder ./build/libs/vehicle-leasing-service-0.0.1-SNAPSHOT.jar
> java -jar ./build/libs/vehicle-leasing-service-0.0.1-SNAPSHOT.jar com.allane.vehicleleasingservice.VehicleLeasingServiceApplication.java
6. Logs should show that the service is running, to view the swagger UI for the APIs type this URL in the browser
> http://localhost:8080/swagger-ui/index.html

# What can be done more
I made sure all required features are implemented, and the overview page is working in a simple solution (with a direct hibernate query), but following in a real production scenario in my mind to be done (but not done partially or completely in order to save development and review time of the interview task at hand as it is not intended to be production ready)

- Index creation at the DB level to enhance the query. For example we could have an index on the createdAt field in the leasing-contracts table to speed up overview query (as it is used to sort data in desc order)
- I used annotation for DTO validation, that should suffice, but it could be more annotations to add more constraints (like customer birthdate should not 500 years ago)
- Test coverage is absolutely not enough, I made sure to demonstrate writing unit and integration tests, but they don't cover all cases and in a production case a 95% test coverage should be achieved
- Although hibernate is quite convenient, but in the case of the overview query, the performance of a native named query might be better
