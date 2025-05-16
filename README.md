⚡ Virtual Power Plant (Battery API)
A Spring Boot REST API for managing battery data in a Virtual Power Plant (VPP). It supports battery registration, querying by postcode range, and provides total and average watt capacity with names sorted alphabetically.

🚀 Features
Register batteries (name, postcode, watt capacity)

Fetch batteries by postcode range

Return:

Sorted battery names

Total and average watt capacity

Java Streams for processing

≥ 70% unit test coverage

🔌 Example API Usage
➕ Register a Battery :
curl.exe -X POST "http://localhost:8080/api/vvp/battery" ^
  -H "Content-Type: application/json" ^
  -d "{ \"name\": \"Test Battery\", \"postCode\": 1234, \"wattCapacity\": 50000 }"
🔍 Get Batteries in Postcode Range
 
curl.exe -X GET "http://localhost:8080/api/vvp/batteries?minPostCode=1000&maxPostCode=7000"


🔧 Tech Stack
Java 21, Spring Boot 3

Spring Web, Spring Data JPA

PostgreSQL (Render)

Lombok, JUnit 5, Testcontainers

SLF4J, Logback

