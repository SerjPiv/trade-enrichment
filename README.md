See the [TASK](./TASK.md) file for instructions.

## How to run the service
1. Clone the repository.
2. Run the following command in the root directory of the project:
```shell
mvn spring-boot:run
```
3. The service will be available at `http://localhost:8080`.

## How to use the API
The API:
1. accepts a CSV file with the following columns:
   - date
   - product_id
   - currency
   - price
2. translates the `product_id` into `product_name`;
3. performs data validation: Ensure that the date is a valid date in `yyyyMMdd` format, otherwise discard the row and log an error;
4. logs the missing mapping and set the product Name as: `Missing Product Name` if the product name is not available;
5. returns a CSV file with the following columns:
   - date
   - product_name
   - currency
   - price
### Sample input trade data CSV file
#### trade.csv
```csv
date,product_id,currency,price
20160101,1,EUR,10.0
20160101234,2,EUR,20.1
20160101,3,EUR,30.34
20160101,11,EUR,35.34
```
### Sample response
```csv
date,product_name,currency,price
20160101,Treasury Bills Domestic,EUR,10.0
20160101,REPO Domestic,EUR,30.34
20160101,Missing Product Name,EUR,35.34
```

The API can be accessed using the following command:
```shell
curl -F "file=@src/test/resources/trade.csv" http://localhost:8080/api/v1/enrich
```

### Limitations of the code
1. The solution scales only vertically by allocating additional resources to a single machine.
2. When dealing with large CSV files, there's a significant memory consumption due to storing all data in memory.
3. Validations are currently absent for fields other than the data field.
4. Concurrent processing for data enrichment is constrained by the number of available cores.
5. The input CSV size is currently capped at 50MB but can be expanded using the following properties:
````
spring.servlet.multipart.max-file-size=50000KB
spring.servlet.multipart.max-request-size=50000KB
````

### Comment on the design
1. The code employs a straightforward design with a single controller and service.
2. Enrichment logic is parallelized based on the number of cores.
3. The OpenCSV third-party library is utilized for working with CSV files.
4. The design prioritizes speed and memory efficiency, which is why the stream API is avoided for filtration and processing.

### Improvement if there were more time available.
When considering potential improvements for the solution, it’s essential to take into account both functional and non-functional requirements. Let’s explore some enhancements:

#### Horizontal Scalability:
Assuming the solution should be able to scale horizontally to handle high traffic, we can make the following adjustments:
1. Streaming Trade Data: Introduce a messaging system (such as Kafka) to handle real-time trade data. This allows for efficient data ingestion and processing across multiple instances.
2. Database Persistence: Save enriched data to a database. Distribute the workload by using sharding or partitioning.
3. API Enhancements: Provide pagination and filtration options in the API. Users can retrieve specific subsets of data efficiently.

#### Minor Improvements:
1. Error Handling: Enhance error handling mechanisms in the controller. Properly handle exceptions and provide meaningful error messages to clients.
2. Test Coverage: Extend test coverage by adding negative test cases. Ensure that edge cases and error scenarios are thoroughly tested.